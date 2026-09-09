package mb28.crysongs.core

import android.Manifest
import android.app.Activity
import android.app.NotificationManager
import android.content.pm.PackageManager
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.ui.util.fastCoerceAtLeast
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.graphics.drawable.IconCompat
import mb28.crysongs.R
import mb28.crysongs.notificationColor
import kotlin.time.Duration

val pageAnimation = scaleIn(initialScale = 0.85f) + fadeIn(initialAlpha = 0.5f)

const val CHANNEL_NOW_PLAYING = "MusicPlayerLive"

fun formatDurationMs(d: Duration) : String {
    val hours = d.inWholeHours
    return (if(hours > 0) "$hours:".padStart(2, '0') else "") +
            d.inWholeMinutes.rem(60).toString().padStart(2, '0') +
            ":${d.inWholeSeconds.rem(60).toString().padStart(2, '0')}"
}

private var nIcon: IconCompat? = null
private const val colWhite = 0xffaaaaaa.toInt()

fun updateNotification(nm: NotificationManager, context: Activity, title: String, subtitle: String,
                       shortCriticalText: String, progress: Duration,
                       duration: Duration, id: Int = 0) {
    if (nIcon == null) {
        nIcon = IconCompat.createWithResource(context, R.drawable.now_playing_icon)
    }
    val d = duration.inWholeSeconds.toInt()
    val s = progress.inWholeSeconds.toInt().fastCoerceAtLeast(1)
    val style: NotificationCompat.ProgressStyle = NotificationCompat.ProgressStyle()
        .setProgress(s)
        .setProgressSegments(
            listOf(
                NotificationCompat.ProgressStyle.Segment(d).setColor(notificationColor ?: colWhite)
            )
        )

    val n = NotificationCompat.Builder(context, CHANNEL_NOW_PLAYING)
        .setSmallIcon(nIcon!!)
        .setColor(notificationColor ?: colWhite)
        .setContentTitle(title)
        .setContentText(subtitle)
        .setShortCriticalText(shortCriticalText)
        .setStyle(style)
        .setOngoing(true)
        .setRequestPromotedOngoing(true)
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

fun inverseLerp(a: Float, b: Float, value: Float): Float {
    if (a != b)
        return ((value - a) / (b - a)).coerceIn(0f, 1f)
    return 0f
}




