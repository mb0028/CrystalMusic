package mb28.crysongs

import android.Manifest
import android.app.Activity
import android.app.NotificationManager
import android.content.ComponentName
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.audiofx.Visualizer
import android.provider.MediaStore
import android.widget.Toast
import androidx.annotation.OptIn
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.mutableStateSetOf
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
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import mb28.crysongs.core.PlayerService
import mb28.crysongs.core.Settings
import mb28.crysongs.core.Settings.loopTrack
import mb28.crysongs.core.Track
import mb28.crysongs.core.VisualizerData
import mb28.crysongs.core.aaa
import mb28.crysongs.core.favoriteAddButton
import mb28.crysongs.core.favoriteRemoveButton
import mb28.crysongs.core.mediaSession
import mb28.crysongs.core.nextButton
import mb28.crysongs.core.previousButton
import mb28.crysongs.core.updateNotification
import mb28.crysongs.glance.PlayerWidget
import mb28.crysongs.ui.theme.trackCoverPrimary
import mb28.music.LrcParser
import java.io.File
import kotlin.time.Duration.Companion.milliseconds

lateinit var player: MediaController
var nowPlayingI by mutableIntStateOf(-1)
var nowPlaying: String? by mutableStateOf(null)
var nowPlayingTags: Track? by mutableStateOf(null)
var privateNowPlayingCover: Bitmap? by mutableStateOf(null)
val nowPlayingCover get() = privateNowPlayingCover?.asImageBitmap() ?: noCoverBitmap!!

var lrcParser: LrcParser? by mutableStateOf(null)
var lastLrcLine by mutableStateOf("")
var lastLrcLineI by mutableIntStateOf(-1)
var memUsage by mutableLongStateOf(0L)



var tracks = mutableStateListOf<String>()
var playerQuery = mutableStateListOf<String>()
var displayQuery = mutableStateListOf<String>()
var folders = mutableStateSetOf<String>()
var albums = mutableStateSetOf<String>()
var artists = mutableStateSetOf<String>()
var genres = mutableStateSetOf<String>()
var bitrates = mutableStateSetOf<Int>()

var displayQueryMB by mutableIntStateOf(0)
var displayQueryMA by mutableIntStateOf(0)


var isPlayerLoopStarted by mutableStateOf(false)
var isReloading by mutableStateOf(false)
var canChangeTrack by mutableStateOf(true)
var isPlaying by mutableStateOf(false)
var position by mutableLongStateOf(0L)
var duration by mutableLongStateOf(0L)
var visualizationData by mutableStateOf(VisualizerData())

private const val NO_LYRIC = "No lyrics..."
private val playerLoopDelay = 150.milliseconds
private var hasLrc = false
private var lastNowPlaying: String? = null
private val scope = CoroutineScope(Dispatchers.Main)
private var visualizer: Visualizer? = null
private var lastWaveFormDataCapture: Long? = null

fun setAndPlay(path: String, resetQuery: Boolean) {
    try {
        isReloading = true
        if (playerQuery.isEmpty() || resetQuery) {
            playerQuery = tracks.toMutableStateList()
        }
        player.apply {
            if (isPlaying) {
                stop()
            }
            setMediaItem(MediaItem.fromUri(path))
            prepare()
            play()
        }
        nowPlaying = path
        nowPlayingI = playerQuery.indexOf(path)
        updateDisplayQuery()
    }
    catch (_: Exception) { }
    isReloading = false
}

fun playNextOrPrevious(next: Boolean = true) {
    val add = if (next) 1 else -1
    val count = playerQuery.count()
    var i = (nowPlayingI + add).coerceIn(0, count)
    if (i == count) {
        i = 0
    }
    setAndPlay(
        playerQuery[i],
        false
    )
}

inline fun Activity.setupPlayer(crossinline onFinished: () -> Unit = {}) {
    val sessionToken = SessionToken(this, ComponentName(this, PlayerService::class.java))
    val controllerFuture = MediaController.Builder(this, sessionToken).buildAsync()
    controllerFuture.addListener({
        if (!isPlayerLoopStarted) {
            player = controllerFuture.get()

            val nm = getSystemService<NotificationManager>()!!

            player.addListener(
                object : Player.Listener {
                    override fun onPlaybackStateChanged(playbackState: Int) {
                        if (playbackState == Player.STATE_ENDED) {
                            try {
                                playNextOrPrevious(true)
                            } catch (_: Exception) { }
                        }
                        super.onPlaybackStateChanged(playbackState)
                    }
                    override fun onIsPlayingChanged(isP: Boolean) {
                        super.onIsPlayingChanged(isP)
                        isPlaying = isP
                    }
                }
            )

            player.repeatMode = if (loopTrack) Player.REPEAT_MODE_ALL else Player.REPEAT_MODE_OFF
            player.volume = Settings.appVolume
            playerLoop(nm, this)
        }
        isPlayerLoopStarted = true

        if (Settings.waveformDataCapture) {
            setupVisu()
        }
        onFinished() },
        ContextCompat.getMainExecutor(this)
    )
}

@OptIn(UnstableApi::class)
fun playerLoop(nm: NotificationManager, context: Activity) = scope.launch {
    val rt = Runtime.getRuntime()

    while (true) {
        position = player.currentPosition
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
                canChangeTrack = false
                PlayerWidget().updateAll(context)
                nowPlayingTags = Track.getTags(nowPlaying, context)
                val coverPath = Track.createOrGetThumbnail(nowPlaying!!)

                if (coverPath != null) {
                    withContext(Dispatchers.IO) {
                        privateNowPlayingCover = BitmapFactory.decodeFile(coverPath)
                        if (Settings.useCoverColor && privateNowPlayingCover != null) {
                            val color = File("${Settings.appCacheThumbsFolder}/${nowPlaying!!.hashCode()}.color")
                            trackCoverPrimary = if (color.exists()) {
                                Color(color.readText().toInt())
                            } else {
                                val acc = privateNowPlayingCover!!.asImageBitmap().themeColors(
                                    1, Color.Blue).first()
                                color.writeText(acc.toArgb().toString())
                                acc
                            }
                            notificationColor = trackCoverPrimary?.toArgb()
                        } else {
                            notificationColor = null
                            trackCoverPrimary = null
                        }
                        return@withContext
                    }
                } else privateNowPlayingCover = null

                hasLrc = Track.hasLRC(nowPlaying!!)
                lrcParser = if (hasLrc) { LrcParser(Track.lrcPath(nowPlaying!!)) } else { null }

                val notifBtn = listOf(if (Settings.favorites.contains(nowPlaying!!))
                    favoriteRemoveButton else favoriteAddButton, nextButton, aaa, previousButton)
                mediaSession?.setMediaButtonPreferences(notifBtn)

                duration = player.duration
                lastNowPlaying = nowPlaying
                canChangeTrack = true
            }
        }

        if (Settings.experimental) {
            memUsage = rt.totalMemory() - rt.freeMemory()
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

suspend fun refreshTracksList(context: Context) = withContext(Dispatchers.IO) {
    println("Refreshing")
    val temp = mutableListOf<String>()
    val tempFolders = mutableSetOf<String>()
    val tempAlbums = mutableSetOf<String>()
    val tempArtists = mutableSetOf<String>()
    val tempGenres = mutableSetOf<String>()
    val tempBit = mutableSetOf<Int>()

    val projection = arrayOf(
        MediaStore.MediaColumns.DATA, MediaStore.Audio.Media.ARTIST,
        MediaStore.Audio.Media.ALBUM, MediaStore.Audio.Media.GENRE,
        MediaStore.Audio.Media.BITRATE
    )

    context.contentResolver.query(
        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
        projection,
        null,
        null,
        when(Settings.sortBy) {
            0 -> MediaStore.Audio.Media.DATE_MODIFIED
            else -> MediaStore.Audio.Media.TITLE
        } + " ${Settings.getSorting}",

        )?.use { cursor ->
        val pc = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)
        val artistC = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
        val albumC = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM)
        val genreC = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.GENRE)
        val bitrateC = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.BITRATE)

        while (cursor.moveToNext()) {
            val path = cursor.getString(pc)
            val album = cursor.getString(albumC) ?: "???"
            val artist = cursor.getString(artistC) ?: "???"
            val genre = cursor.getString(genreC) ?: "???"
            val bitrate = cursor.getInt(bitrateC)

            temp.add(path)
            File(path).parent?.let { tempFolders.add(it) }
            tempAlbums.add(album)
            tempArtists.add(artist)
            tempGenres.add(genre)
            tempBit.add(bitrate)
        }
    }

    withContext(Dispatchers.Main) {
        tracks.clear(); tracks.addAll(temp)
        folders.clear(); folders.addAll(tempFolders)
        albums.clear(); albums.addAll(tempAlbums)
        artists.clear(); artists.addAll(tempArtists)
        genres.clear(); genres.addAll(tempGenres)
        bitrates.clear(); bitrates.addAll(tempBit)
    }
    println("Refresh completed")
}

@OptIn(UnstableApi::class)
fun Activity.setupVisu() {
    if (checkSelfPermission(Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
        requestPermissions(arrayOf(Manifest.permission.RECORD_AUDIO), 2)
        if (Settings.waveformDataCapture) {
            Settings.waveformDataCapture = false
            Settings.save()
            Toast.makeText(this, "Enable waveform capture turned off because Microphone permission is denied",
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
