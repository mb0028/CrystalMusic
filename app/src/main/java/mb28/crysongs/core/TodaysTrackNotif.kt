package mb28.crysongs.core

import android.Manifest
import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.graphics.drawable.IconCompat
import mb28.crysongs.R
import mb28.crysongs.refreshTracksList
import mb28.crysongs.tracks
import kotlin.random.Random

class TodaysTrackNotif : BroadcastReceiver() {
    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    override fun onReceive(context: Context, intent: Intent) {
        refreshTracksList(context)
        val track = tracks.shuffled(Random(System.currentTimeMillis())).first()
        val nm = NotificationManagerCompat.from(context)
        val n = NotificationCompat.Builder(context, CHANNEL_TODAYS_MUSIC)
            .setSmallIcon(IconCompat.createWithResource(context, R.drawable.music_note_24px))
            .setContentTitle("Today's music")
            .setContentText("${track.title} - ${track.artist}")
            .build()

        if (context.checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            nm.notify(0, n)
        }

        scheduleNotifications(context)
    }
}