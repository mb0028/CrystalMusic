package com.example.crystal_music

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.widget.Toast
import androidx.core.app.ServiceCompat
import androidx.core.net.toUri
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel

class MainActivity : FlutterActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        if (!Environment.isExternalStorageManager()) {
            Toast.makeText(this, "All files access is denied", Toast.LENGTH_SHORT).show()
            val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION,
                "package:$packageName".toUri())
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(Manifest.permission.POST_NOTIFICATIONS), 0)
            }
        }

        super.onCreate(savedInstanceState)
    }

    private val player = MediaPlayer()

    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)

        MethodChannel(flutterEngine.dartExecutor.binaryMessenger, "mb28.CrystalSongs/MediaPlayer"
        ).setMethodCallHandler { call, result ->
            when (call.method) {
                "prepare" -> {
                    player.reset()
                    player.setDataSource(call.argument<String>("path")!!)
                    player.prepare()
                    result.success(true)
                }
                "prepareAndPlay" -> {
                    if (player.isPlaying) {
                        player.stop()
                    }
                    player.reset()
                    player.setDataSource(call.argument<String>("path")!!)
                    player.prepare()
                    player.start()
                    result.success(true)
                }
                "play" -> {
                    player.start()
                    result.success(true)
                }
                "pause" -> {
                    player.pause()
                    result.success(true)
                }
                "stop" -> {
                    player.stop()
                    result.success(true)
                }
                "isPlaying" -> {
                    result.success(player.isPlaying)
                }
                "getPosition" -> {
                    result.success(player.currentPosition)
                }
                "setVolume" -> {
                    val vol = call.argument<Float>("volume")!!
                    player.setVolume(vol, vol)
                    result.success(true)
                }
                "seekTo" -> {
                    player.seekTo(call.argument<Int>("msec")!!)
                    result.success(true)
                }
                "setIsLooping" -> {
                    player.isLooping = call.argument<Boolean>("loop")!!
                    result.success(true)
                }
                "isLooping" -> {
                    result.success(player.isLooping)
                }
                "dispose" -> {
                    player.release()
                    result.success(true)
                }
                "updateNotif" -> {
                    Notifer().UpdateNowPlayingMusic(
                        this,
                        call.argument<String>("title")!!,
                        call.argument<String>("subtitle")!!,
                        call.argument<String>("sst")!!
                    )
                    result.success(true)
                }
                "startFG" -> {
                    val intent = Intent(this, PlayerServe::class.java)
                    context.startForegroundService(intent)
                    result.success(true)
                }
                "stopFG" -> {
                    val intent = Intent(this, PlayerServe::class.java)
                    context.stopService(intent)
                    result.success(true)
                }
            }
        }
    }
}
