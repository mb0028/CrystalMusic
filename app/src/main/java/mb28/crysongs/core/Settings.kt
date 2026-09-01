package mb28.crysongs.core

import android.app.Activity
import android.content.Intent
import android.os.Environment
import android.provider.Settings
import android.widget.Toast
import androidx.core.net.toUri
import java.io.File

object Settings {
    const val appFolder = "/sdcard/Documents/.Crystal"
    const val appCacheFolder = "$appFolder/.SongsTemp"
    const val appCacheThumbsFolder = "$appFolder/Covers"
    const val settingsFile = "$appFolder/Songs Settings.txt"

    val favorites = mutableListOf<String>()
    var sortBy = 0
    var sortOrderDesc = true
    var tagsSpacer = " • "

    fun load() {
        favorites.clear()
        val a = File(appFolder)
        val ac = File(appCacheFolder)
        val atc = File(appCacheThumbsFolder)
        if (!a.exists() || !ac.exists() || !atc.exists()) {
            atc.mkdirs()
        }

        val file = File(settingsFile)
        if (file.exists()) {
            val data = file.readLines()
            data.forEach { s ->
                when {
                    s.startsWith("[Favorite]") -> {
                        val path = s.removePrefix("[Favorite]")
                        if (File(path).exists()) {
                            favorites.add(path)
                        }
                    }
                    s.startsWith("[SortBy]") -> sortBy = s.removePrefix("[SortBy]").toInt()
                    s.startsWith("[SortOrderDesc]") -> sortOrderDesc = s.removePrefix("[SortOrderDesc]").toBooleanStrict()
                    s.startsWith("[TagsSpacer]") -> tagsSpacer = s.removePrefix("[TagsSpacer]")
                }
            }
        } else {
            File("$appFolder/Settings").mkdirs()
            file.createNewFile()
            save()
        }
    }

    fun save() {
        var data = "[Settings]\n"
        data += "[SortBy]$sortBy\n"
        data += "[SortOrderDesc]$sortOrderDesc\n"
        data += "[TagsSpacer]$tagsSpacer\n"

        data += "\n[Favorites]\n"
        favorites.forEach {
            data += "[Favorite]$it\n"
        }

        val file = File(settingsFile)
        file.writeText(data)
    }

    fun Activity.requestAllFilesAccessOrFinish() {
        if (!Environment.isExternalStorageManager()) {
            Toast.makeText(this, "App needs all file access to run", Toast.LENGTH_LONG).show()
            val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                .setData("package:$packageName".toUri())
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }
    }
}