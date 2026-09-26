package mb28.crysongs.core

import android.Manifest
import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.icu.util.Calendar
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.graphics.drawable.IconCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import mb28.crysongs.R
import mb28.crysongs.refreshTracksList
import mb28.crysongs.tracks
import kotlin.random.Random

class TodaysTrackNotif : BroadcastReceiver() {
    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    override fun onReceive(context: Context, intent: Intent) {
        CoroutineScope(Dispatchers.Main).launch {
            val h = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
            if (h in 7..8) {
                refreshTracksList(context)
                try {
                    val path = tracks.shuffled(Random(System.currentTimeMillis())).first()
                    val track = Track.getTags(path, context)
                    val nm = NotificationManagerCompat.from(context)
                    val n = NotificationCompat.Builder(context, CHANNEL_TODAYS_MUSIC)
                        .setSmallIcon(IconCompat.createWithResource(context, R.drawable.music_note_24px))
                        .setContentTitle("Today's music")
                        .setContentText("${track.title} - ${track.artist}")
                        .build()

                    if (context.checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
                        nm.notify(8, n)
                    }
                } catch (_: Exception) { }
            }
        }
    }
}