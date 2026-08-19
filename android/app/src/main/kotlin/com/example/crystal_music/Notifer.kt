package com.example.crystal_music

import android.Manifest
import android.app.Notification
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationManagerCompat

class Notifer {
    private val CHANNEL_PLAYING = "Playing"

    fun UpdateNowPlayingMusic(context: Context, title : String, subtitle: String, criticalText: String) {
        val manager = NotificationManagerCompat.from(context)

        val channel = NotificationChannelCompat.Builder(CHANNEL_PLAYING, NotificationManagerCompat.IMPORTANCE_HIGH)
        channel.setName("Now Playing")
        channel.setDescription("Shows notification when music is playing")
        channel.setImportance(NotificationManagerCompat.IMPORTANCE_HIGH)
        manager.createNotificationChannelsCompat(mutableListOf(channel.build()))

        val notif = Notification.Builder(context, CHANNEL_PLAYING)
        notif.setContentTitle(title)
        notif.setContentText(subtitle)
        notif.setSmallIcon(R.drawable.now_playing_icon)
        notif.setColor(0xffaa91f2.toInt())
        notif.setOngoing(true)
        notif.setStyle(Notification.BigTextStyle())

        val b = Bundle()
        b.putBoolean("android.requestPromotedOngoing", true)
        notif.addExtras(b)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.BAKLAVA) {
            notif.setShortCriticalText(criticalText)
        }

        if (context.checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            manager.notify(30, notif.build())
        }
    }
}