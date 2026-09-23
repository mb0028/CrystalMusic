@file:Suppress("ConstPropertyName")

package mb28.crysongs.core

import android.annotation.SuppressLint
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import java.io.File

object Settings {
    @SuppressLint("SdCardPath")
    const val appFolder = "/sdcard/Documents/.Crystal"
    const val appCacheThumbsFolder = "$appFolder/Covers"
    const val settingsFile = "$appFolder/Songs Settings.txt"

    val favorites = mutableStateListOf<String>()
    val playlists = mutableStateListOf<String>()
    var loopTrack by mutableStateOf(false)
    var verticalLyrics by mutableStateOf(false)
    var appVolume by mutableFloatStateOf(1f)
    var coverShapeMode by mutableIntStateOf(2)
    var sortBy = 0
    var sortOrderDesc by mutableStateOf(true)
    var tagsSpacer by mutableStateOf(" • ")
    var backgroundBlurRadius by mutableIntStateOf(45)
    var useCoverColor by mutableStateOf(false)
    var experimental by mutableStateOf(false)
    var whiteText by mutableStateOf(false)
    var gradientColoring by mutableStateOf(false)
    var coverParallax by mutableStateOf(true)
    var edgeLighting by mutableStateOf(false)
    var edgeLightingSaturation by mutableFloatStateOf(0.6f)
    var edgeLightingLightness by mutableFloatStateOf(0.75f)
    var edgeLightingHue1 by mutableFloatStateOf(0f)
    var edgeLightingHue2 by mutableFloatStateOf(90f)
    var edgeLightingHue3 by mutableFloatStateOf(180f)
    var edgeLightingHue4 by mutableFloatStateOf(320f)
    var waveformDataCapture by mutableStateOf(false)
    var windEffect by mutableStateOf(false)

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
        val atc = File(appCacheThumbsFolder)
        if (!a.exists() || !atc.exists()) {
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
                    s.startsWith("[FSBlur]") -> backgroundBlurRadius = s.removePrefix("[FSBlur]").toInt()
                    s.startsWith("[UseCoverColor]") -> useCoverColor = s.removePrefix("[UseCoverColor]").toBooleanStrict()
                    s.startsWith("[FeatureFlags]") -> experimental = s.removePrefix("[FeatureFlags]").toBooleanStrict()
                    s.startsWith("[WhiteTexts]") -> whiteText = s.removePrefix("[WhiteTexts]").toBooleanStrict()
                    s.startsWith("[GradientColoring]") -> gradientColoring = s.removePrefix("[GradientColoring]").toBooleanStrict()
                    s.startsWith("[EdgeLighting]") -> edgeLighting = s.removePrefix("[EdgeLighting]").toBooleanStrict()
                    s.startsWith("[ELSaturation]") -> edgeLightingSaturation = s.removePrefix("[ELSaturation]").toFloat()
                    s.startsWith("[ELLightness]") -> edgeLightingLightness = s.removePrefix("[ELLightness]").toFloat()
                    s.startsWith("[ELHue1]") -> edgeLightingHue1 = s.removePrefix("[ELHue1]").toFloat()
                    s.startsWith("[ELHue2]") -> edgeLightingHue2 = s.removePrefix("[ELHue2]").toFloat()
                    s.startsWith("[ELHue3]") -> edgeLightingHue3 = s.removePrefix("[ELHue3]").toFloat()
                    s.startsWith("[ELHue4]") -> edgeLightingHue4 = s.removePrefix("[ELHue4]").toFloat()
                    s.startsWith("[SCWEL]") -> coverParallax = s.removePrefix("[SCWEL]").toBooleanStrict()
                    s.startsWith("[WaveCapture]") -> waveformDataCapture = s.removePrefix("[WaveCapture]").toBooleanStrict()
                    s.startsWith("[Wind]") -> windEffect = s.removePrefix("[Wind]").toBooleanStrict()
                }
            }
        } else {
            file.createNewFile()
            save()
        }
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
        data += "[UseCoverColor]$useCoverColor\n"
        data += "[FeatureFlags]$experimental\n"
        data += "[WhiteTexts]$whiteText\n"
        data += "[FSBlur]$backgroundBlurRadius\n"
        data += "[GradientColoring]$gradientColoring\n"
        data += "[WaveCapture]$waveformDataCapture\n"
        data += "[EdgeLighting]$edgeLighting\n"
        data += "[SCWEL]$coverParallax\n"
        data += "[Wind]$windEffect\n"
        data += "[ELSaturation]$edgeLightingSaturation\n"
        data += "[ELLightness]$edgeLightingLightness\n"
        data += "[ELHue1]$edgeLightingHue1\n"
        data += "[ELHue2]$edgeLightingHue2\n"
        data += "[ELHue3]$edgeLightingHue3\n"
        data += "[ELHue4]$edgeLightingHue4\n"

        data += "\n"
        playlists.forEach {
            data += "[crym3u]$it\n"
        }

        data += "\n"
        favorites.forEach {
            data += "[Favorite]$it\n"
        }

        val file = File(settingsFile)
        file.writeText(data)
    }
}