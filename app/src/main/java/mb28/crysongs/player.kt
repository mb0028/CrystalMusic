package mb28.crysongs

import android.app.Activity
import android.app.NotificationManager
import android.content.ContentUris
import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.provider.MediaStore
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import mb28.crysongs.core.Settings
import mb28.crysongs.core.Settings.tagsSpacer
import mb28.crysongs.core.Track
import mb28.crysongs.core.updateNotification
import mb28.music.LrcParser
import kotlin.time.Duration.Companion.milliseconds

var player by mutableStateOf(MediaPlayer())

var nowPlayingI by mutableIntStateOf(-1)
var nowPlaying: Track? by mutableStateOf(null)
var lrcParser: LrcParser? by mutableStateOf(null)
var lastLrcLine by mutableStateOf("")
var isReloading by mutableStateOf(false)

private var hasLrc = false
private var lastNowPlaying: Track? = null
private val scope = CoroutineScope(Dispatchers.Main)

var tracks = mutableStateListOf<Track>()
var displayQuery = mutableStateListOf<Track>()
var playerQuery = mutableStateListOf<Track>()

fun setAndPlay(track: Track, resetQuery: Boolean) {
    try {
        isReloading = true
        if (playerQuery.isEmpty() || resetQuery) {
            playerQuery = tracks.toMutableStateList()
        }
        if (player.isPlaying) {
            player.stop()
        }
        player.reset()
        player.setDataSource(track.path)
        player.prepare()
        player.start()
        nowPlaying = track
        nowPlayingI = playerQuery.indexOf(nowPlaying)
        updateDisplayQuery()
    }
    catch (_: Exception) {

    }
    isReloading = false
}



fun playerLoop(nm: NotificationManager, context: Activity, color: Color) = scope.launch {
    while (true) {
        if (nowPlaying != null) {
            val pos = player.currentPosition

            if (lrcParser != null) {
                val line = lrcParser!!.LineByAudioPosition(pos)
                // On lyric line changes
                if (line != lastLrcLine) {
                    updateNotification(nm, context, nowPlaying!!.title + "$tagsSpacer${nowPlaying!!.artist}", line,
                        line, color, pos.milliseconds, player.duration.milliseconds)
                    lastLrcLine = line
                }
            } else {
                lastLrcLine = NO_LYRIC
                updateNotification(nm, context, nowPlaying!!.title, nowPlaying!!.artist,
                    nowPlaying!!.title, color, pos.milliseconds, player.duration.milliseconds)
            }

            // On track changed
            if (nowPlaying != lastNowPlaying) {
                hasLrc = nowPlaying!!.hasLRC
                lrcParser = if (hasLrc) {
                    LrcParser(nowPlaying!!.lrcPath)
                } else {
                    null
                }

                lastNowPlaying = nowPlaying
            }
        }

        delay(if (hasLrc) 100.milliseconds else 300.milliseconds)
    }
}

fun updateDisplayQuery() {
    displayQuery = playerQuery.subList(
        (nowPlayingI - 4).coerceAtLeast(0),
        (nowPlayingI + 11).coerceAtMost(playerQuery.count())
    ).toMutableStateList()
}

fun refreshTracksList(context: Context) {
    tracks.clear()

    val projection = arrayOf(
        MediaStore.MediaColumns.DATA,
        MediaStore.Video.Media._ID,
        MediaStore.Video.Media.TITLE,
        MediaStore.Video.Media.ARTIST,
        MediaStore.Video.Media.ALBUM,
        MediaStore.Video.Media.GENRE,
        MediaStore.Video.Media.COMPOSER,
        MediaStore.Video.Media.DURATION,
        MediaStore.Video.Media.BITRATE
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
            )
            tracks += track
        }
    }
}

const val NO_LYRIC = "No lyric..."
