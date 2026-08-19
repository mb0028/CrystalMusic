package com.example.crystal_music

import android.app.Notification
import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK
import android.os.IBinder
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.ServiceCompat

class PlayerServe : Service() {
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val manager = NotificationManagerCompat.from(this)
        val channel = NotificationChannelCompat.Builder("PlayerFG", NotificationManagerCompat.IMPORTANCE_NONE)
        channel.setName("Player Service")
        channel.setDescription("Shows notification when music service is running")
        channel.setImportance(NotificationManagerCompat.IMPORTANCE_NONE)
        manager.createNotificationChannelsCompat(mutableListOf(channel.build()))

        val n = Notification.Builder(this, "PlayerFG").setSubText("You can disable this notification category, tap to open settings")
        ServiceCompat.startForeground(this, 28, n.build(), FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK);

        return super.onStartCommand(intent, flags, startId)
    }
}