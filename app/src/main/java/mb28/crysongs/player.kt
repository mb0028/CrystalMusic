package mb28.crysongs

import android.app.Activity
import android.app.NotificationManager
import android.media.MediaPlayer
import androidx.compose.ui.graphics.Color
import androidx.core.app.NotificationManagerCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import mb28.crysongs.core.Settings.tagsSpacer
import mb28.crysongs.core.Track
import mb28.crysongs.core.updateNotification
import mb28.music.LrcParser
import kotlin.time.Duration.Companion.milliseconds

lateinit var player: MediaPlayer

var nowPlaying: Track? = null
var lrcParser: LrcParser? = null
var lastLrcLine = ""

private var hasLrc = false
private var lastNowPlaying: Track? = null
private val scope = CoroutineScope(Dispatchers.Main)

fun setAndPlay(track: Track) {
    try {
        if (player.isPlaying) {
            player.stop()
        }
        player.reset()
        player.setDataSource(track.path)
        player.prepare()
        player.start()
        nowPlaying = track
    }
    catch (_: Exception) {

    }
}

const val NO_LYRIC = "No lyric..."

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

