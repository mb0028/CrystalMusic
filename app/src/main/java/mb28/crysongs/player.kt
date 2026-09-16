package mb28.crysongs

import android.Manifest
import android.app.Activity
import android.app.NotificationManager
import android.content.ComponentName
import android.content.ContentUris
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.audiofx.Visualizer
import android.net.Uri
import android.provider.MediaStore
import android.widget.Toast
import androidx.annotation.OptIn
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.toArgb
import androidx.core.content.ContextCompat
import androidx.core.content.getSystemService
import androidx.glance.appwidget.updateAll
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.materialkolor.ktx.themeColors
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import mb28.crysongs.core.PlayerService
import mb28.crysongs.core.Settings
import mb28.crysongs.core.Settings.loopTrack
import mb28.crysongs.core.Track
import mb28.crysongs.core.VisualizerData
import mb28.crysongs.core.updateNotification
import mb28.crysongs.glance.PlayerWidget
import mb28.crysongs.ui.theme.trackCoverPrimary
import mb28.music.LrcParser
import java.io.File
import kotlin.time.Duration.Companion.milliseconds

lateinit var player: MediaController
var nowPlayingI by mutableIntStateOf(-1)
var nowPlaying: Track? by mutableStateOf(null)
private var pNowPlayingCover: Bitmap? by mutableStateOf(null)
val nowPlayingCover get() = pNowPlayingCover?.asImageBitmap() ?: noCoverBitmap!!

var lrcParser: LrcParser? by mutableStateOf(null)
var lastLrcLine by mutableStateOf("")
var lastLrcLineI by mutableIntStateOf(-1)


var albums = mutableStateListOf<String>()
var artists = mutableStateListOf<String>()
var genres = mutableStateListOf<String>()
var composers = mutableStateListOf<String>()
var folders = mutableStateListOf<String>()
var tracks = mutableStateListOf<Track>()
var playerQuery = mutableStateListOf<Track>()
var displayQuery = mutableStateListOf<Track>()

var displayQueryMB by mutableIntStateOf(0)
var displayQueryMA by mutableIntStateOf(0)


var isPlayerLoopStarted by mutableStateOf(false)
var isReloading by mutableStateOf(false)
var isPlaying by mutableStateOf(false)
var position by mutableIntStateOf(0)
var duration by mutableIntStateOf(0)
var visualizationData by mutableStateOf(VisualizerData())

private const val NO_LYRIC = "No lyrics..."
private val playerLoopDelay = 120.milliseconds
private var hasLrc = false
private var lastNowPlaying: Track? = null
private val scope = CoroutineScope(Dispatchers.Main)
private var visualizer: Visualizer? = null
private var lastWaveFormDataCapture: Long? = null

fun setAndPlay(track: Track, resetQuery: Boolean) {
    try {
        isReloading = true
        if (playerQuery.isEmpty() || resetQuery) {
            playerQuery = tracks.toMutableStateList()
        }
        player.apply {
            if (isPlaying) {
                stop()
            }
            setMediaItem(MediaItem.fromUri(track.path))
            prepare()
            play()
        }
        nowPlaying = track
        nowPlayingI = playerQuery.indexOf(nowPlaying)
        updateDisplayQuery()
    }
    catch (_: Exception) { }
    isReloading = false
}

fun playNextOrPrevious(next: Boolean = true) {
    val add = if (next) 1 else -1
    setAndPlay(
        playerQuery[(nowPlayingI + add).coerceIn(0, playerQuery.count() - 1)],
        false
    )
}

fun playerLoop(nm: NotificationManager, context: Activity) = scope.launch {
    while (true) {
        isPlaying = player.isPlaying
        // Pos needs to update even when player is paused for seekbar
        position = player.currentPosition.toInt()

        if (nowPlaying != null && isPlaying) {
            if (lrcParser != null) {
                val line = lrcParser!!.LineByAudioPosition(position)
                lastLrcLineI = lrcParser!!.LineIndex(position)

                // On lyric line changes
                if (line != lastLrcLine) {
                    PlayerWidget().updateAll(context)
                    updateNotification(nm, context, line)
                    lastLrcLine = line
                }
            } else {
                lastLrcLine = NO_LYRIC
                nm.cancel(0)
            }

            // On track changed
            if (nowPlaying != lastNowPlaying) {
                val coverPath = Track.createOrGetThumbnail(nowPlaying!!.path)
                pNowPlayingCover = if (coverPath == null) null
                    else BitmapFactory.decodeFile(coverPath)

                if (Settings.useCoverColor && coverPath != null) {
                    trackCoverPrimary = if (pNowPlayingCover != null) {
                        pNowPlayingCover!!.asImageBitmap().themeColors(fallback = Color.Blue).first()
                    } else null
                    notificationColor = trackCoverPrimary?.toArgb()
                }

                PlayerWidget().updateAll(context)
                hasLrc = nowPlaying!!.hasLRC
                lrcParser = if (hasLrc) { LrcParser(nowPlaying!!.lrcPath) } else { null }

                duration = player.duration.toInt()
                lastNowPlaying = nowPlaying
            }
        }

        delay(playerLoopDelay)
    }
}

fun updateDisplayQuery() {
    val pqc = playerQuery.count()
    val first = (nowPlayingI - 4).coerceAtLeast(0)
    val last = (nowPlayingI + 11).coerceAtMost(pqc)
    displayQuery = playerQuery.subList(first,last).toMutableStateList()
    displayQueryMB = playerQuery.subList(0, first).count()
    displayQueryMA = playerQuery.subList(last, pqc).count()
}

fun refreshTracksList(context: Context) {
    tracks.clear(); folders.clear(); albums.clear()
    artists.clear(); genres.clear(); composers.clear()

    val projection = arrayOf(
        MediaStore.MediaColumns.DATA,
        MediaStore.Video.Media._ID,
        MediaStore.Video.Media.TITLE,
        MediaStore.Video.Media.ARTIST,
        MediaStore.Video.Media.ALBUM,
        MediaStore.Video.Media.GENRE,
        MediaStore.Video.Media.COMPOSER,
        MediaStore.Video.Media.DURATION,
        MediaStore.Video.Media.BITRATE,
        MediaStore.Video.Media.YEAR,
        MediaStore.Video.Media.ALBUM_ARTIST,
    )

    context.contentResolver.query(
        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
        projection,
        null,
        null,
        when(Settings.sortBy) {
            0 -> MediaStore.Audio.Media.DATE_MODIFIED
            else -> MediaStore.Audio.Media.TITLE
        } + " ${if (Settings.sortOrderDesc) "DESC" else "ASC"}",

        )?.use { cursor ->
        val idc = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
        val pc = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)
        val titleC = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
        val artistC = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
        val albumC = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM)
        val genreC = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.GENRE)
        val composerC = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.COMPOSER)
        val durationC = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)
        val bitrateC = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.BITRATE)
        val yearC = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.YEAR)
        val aaC = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ARTIST)

        while (cursor.moveToNext()) {
            val id = cursor.getLong(idc)
            val path = cursor.getString(pc)
            val contentUri: Uri = ContentUris.withAppendedId(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                id
            )

            val track = Track(
                contentUri,
                path,
                cursor.getString(titleC) ?: "???",
                cursor.getString(artistC) ?: "???",
                cursor.getString(albumC) ?: "???",
                cursor.getString(genreC) ?: "???",
                cursor.getString(composerC) ?: "???",
                cursor.getLong(durationC),
                cursor.getInt(bitrateC),
                cursor.getString(yearC) ?: "???",
                cursor.getString(aaC) ?: "???",
            )
            tracks += track

            val folder = File(path).parent
            if (folder != null && !folders.contains(folder)) {
                folders.add(folder)
            }
            track.run {
                if (album != "???" && !albums.contains(album)) {
                    albums.add(album)
                }
                if (artist != "???" && !artists.contains(artist)) {
                    artists.add(artist)
                }
                if (genre != "???" && !genres.contains(genre)) {
                    genres.add(genre)
                }
                if (composer != "???" && !composers.contains(composer)) {
                    composers.add(composer)
                }
            }
        }
    }
    folders.sort()
    artists.sort()
    albums.sort()
    genres.sort()
    composers.sort()
}

fun Activity.setupPlayer(onFinished: () -> Unit = {}) {
    val sessionToken = SessionToken(this, ComponentName(this, PlayerService::class.java))
    val controllerFuture = MediaController.Builder(this, sessionToken).buildAsync()
    controllerFuture.addListener(
        {
            if (!isPlayerLoopStarted) {
                player = controllerFuture.get()

                val nm = getSystemService<NotificationManager>()!!

                player.addListener(
                    object : Player.Listener {
                        override fun onPlaybackStateChanged(playbackState: Int) {
                            if (playbackState == Player.STATE_ENDED) {
                                try {
                                    setAndPlay(
                                        playerQuery[(playerQuery.indexOf(nowPlaying) + 1).coerceIn(0, playerQuery.count() - 1)],
                                        false
                                    )
                                } catch (_: Exception) { }
                            }
                            super.onPlaybackStateChanged(playbackState)
                        }
                    }
                )

                player.repeatMode = if (loopTrack) Player.REPEAT_MODE_ALL else Player.REPEAT_MODE_OFF
                playerLoop(nm, this)
                onFinished()
            }
            isPlayerLoopStarted = true

            if (Settings.experimental && Settings.edgeLighting) {
                setupVisu()
            }
        },
        ContextCompat.getMainExecutor(this)
    )
}

@OptIn(UnstableApi::class)
fun Activity.setupVisu() {
    if (checkSelfPermission(Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
        requestPermissions(arrayOf(Manifest.permission.RECORD_AUDIO), 2)
        if (Settings.edgeLighting) {
            Settings.edgeLighting = false
            Settings.save()
            Toast.makeText(this, "Edge lighting turned off because Microphone permission is denied",
                Toast.LENGTH_LONG).show()
        }
    } else {
        visualizer?.release()
        visualizer = Visualizer(player.audioSessionId).apply {
            enabled = false
            val captureS = Visualizer.getCaptureSizeRange()[1]
            captureSize = captureS
            setDataCaptureListener(
                object : Visualizer.OnDataCaptureListener {
                    override fun onFftDataCapture(visualizer: Visualizer, fft: ByteArray, samplingRate: Int) {
                    }
                    override fun onWaveFormDataCapture(visualizer: Visualizer, waveform: ByteArray, samplingRate: Int
                    ) {
                        val now = System.currentTimeMillis()
                        val durationSinceLastCapture = lastWaveFormDataCapture?.let { now - it } ?: 0
                        if (lastWaveFormDataCapture == null || durationSinceLastCapture > 100) {
                            visualizationData = VisualizerData(
                                rawWaveform = waveform.clone(),
                                captureSize = captureS,
                            )
                            lastWaveFormDataCapture = now
                        }
                    }
                },
                Visualizer.getMaxCaptureRate(),
                true,
                true
            )
            enabled = true
        }
    }
}
