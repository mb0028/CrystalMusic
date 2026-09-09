@file:Suppress("ConstPropertyName")

package mb28.crysongs.core

import android.annotation.SuppressLint
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import mb28.crysongs.player
import java.io.File

object Settings {
    @SuppressLint("SdCardPath")
    const val appFolder = "/sdcard/Documents/.Crystal"
    const val appCacheFolder = "$appFolder/.SongsTemp"
    const val appCacheThumbsFolder = "$appFolder/Covers"
    const val settingsFile = "$appFolder/Songs Settings.txt"

    val favorites = mutableStateListOf<String>()
    val playlists = mutableStateListOf<String>()
    var loopTrack by mutableStateOf(false)
    var verticalLyrics by mutableStateOf(false)
    var appVolume by mutableFloatStateOf(1f)
    var coverShapeMode by mutableIntStateOf(0)
    var sortBy = 0
    var sortOrderDesc = true
    var tagsSpacer = " • "

    fun addOrRemoveFavorite(path: String) {
        if (favorites.contains(path)) {
            favorites.remove(path)
        } else {
            favorites.add(path)
        }
        save()
    }

    fun load() {
        favorites.clear()
        playlists.clear()
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
                    s.startsWith("[crym3u]") -> {
                        val path = s.removePrefix("[crym3u]")
                        if (File(path).exists()) {
                            playlists.add(path)
                        }
                    }
                    s.startsWith("[SortBy]") -> sortBy = s.removePrefix("[SortBy]").toInt()
                    s.startsWith("[SortOrderDesc]") -> sortOrderDesc = s.removePrefix("[SortOrderDesc]").toBooleanStrict()
                    s.startsWith("[Loop]") -> loopTrack = s.removePrefix("[Loop]").toBooleanStrict()
                    s.startsWith("[VerticalLyrics]") -> verticalLyrics = s.removePrefix("[VerticalLyrics]").toBooleanStrict()
                    s.startsWith("[TagsSpacer]") -> tagsSpacer = s.removePrefix("[TagsSpacer]")
                    s.startsWith("[Volume]") -> appVolume = s.removePrefix("[Volume]").toFloat()
                    s.startsWith("[CoverShape]") -> coverShapeMode = s.removePrefix("[CoverShape]").toInt()
                }
            }
        } else {
            file.createNewFile()
            save()
        }
        player.isLooping = loopTrack
    }

    fun save() {
        var data = "[Settings]\n"
        data += "[SortBy]$sortBy\n"
        data += "[SortOrderDesc]$sortOrderDesc\n"
        data += "[Loop]$loopTrack\n"
        data += "[Volume]$appVolume\n"
        data += "[TagsSpacer]$tagsSpacer\n"
        data += "[CoverShape]$coverShapeMode\n"
        data += "[VerticalLyrics]$verticalLyrics\n"

        data += "\n[Playlists]\n"
        playlists.forEach {
            data += "[crym3u]$it\n"
        }

        data += "\n[Favorites]\n"
        favorites.forEach {
            data += "[Favorite]$it\n"
        }

        val file = File(settingsFile)
        file.writeText(data)
    }
}