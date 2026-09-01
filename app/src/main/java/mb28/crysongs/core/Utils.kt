package mb28.crysongs.core

import android.Manifest
import android.app.Activity
import android.app.NotificationManager
import android.content.pm.PackageManager
import androidx.compose.ui.graphics.toArgb
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.graphics.drawable.IconCompat
import mb28.crysongs.R
import kotlin.time.Duration

const val CHANNEL_NOW_PLAYING = "MusicPlayerLive"

fun formatDurationMs(d: Duration) : String {
    val hours = d.inWholeHours
    return (if(hours > 0) "$hours:".padStart(2, '0') else "") +
            d.inWholeMinutes.rem(60).toString().padStart(2, '0') +
            ":${d.inWholeSeconds.rem(60).toString().padStart(2, '0')}"
}

private var progressIcon: IconCompat? = null
private var nIcon: IconCompat? = null

fun updateNotification(nm: NotificationManager, context: Activity, title: String, subtitle: String,
                          shortCriticalText: String, color: androidx.compose.ui.graphics.Color, progress: Duration,
                          duration: Duration, id: Int = 0) {
    if (progressIcon == null) {
        progressIcon = IconCompat.createWithResource(context, R.drawable.current_pos)
    }
    if (nIcon == null) {
        nIcon = IconCompat.createWithResource(context, R.drawable.now_playing_icon)
    }
    val d = duration.inWholeSeconds.toInt()
    val style: NotificationCompat.ProgressStyle = NotificationCompat.ProgressStyle()
        .setStyledByProgress(false)
        .setProgress(progress.inWholeSeconds.toInt())
        .setProgressTrackerIcon(progressIcon!!)
        .setProgressSegments(
            listOf(
                NotificationCompat.ProgressStyle.Segment(d).setColor(color.toArgb())
            )
        )

    val n = NotificationCompat.Builder(context, CHANNEL_NOW_PLAYING)
        .setColor(color.toArgb())
        .setContentTitle(title)
        .setContentText(subtitle)
        .setShortCriticalText(shortCriticalText)
        .setOngoing(true)
        .setRequestPromotedOngoing(true)
        .setSmallIcon(nIcon!!)
        .setStyle(style)
        .build()


    if (context.checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
        nm.notify(id, n)
    }
}

fun Activity.setupPermissions() {
    if (checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
        requestPermissions(arrayOf(Manifest.permission.POST_NOTIFICATIONS), 0)
    }

    val nm = NotificationManagerCompat.from(this)
    val channel = NotificationChannelCompat.Builder(CHANNEL_NOW_PLAYING, NotificationManagerCompat.IMPORTANCE_LOW)
        .setName("Now playing")
        .setDescription("Shows now playing track info as live notification")
        .build()

    nm.createNotificationChannel(channel)
}






