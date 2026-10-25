package mb28.crysongs.ui.theme

import android.app.WallpaperManager
import android.os.Environment
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.getSystemService
import androidx.core.graphics.drawable.toBitmap
import mb28.crysongs.core.Settings

var wallpaperIB by mutableStateOf<ImageBitmap?>(null)

@Composable
fun MicaBG(modifier: Modifier = Modifier) {
    val color = when {
        Settings.mica -> MaterialTheme.colorScheme.background.copy(0.8f)
        Settings.paintMode && isSystemInDarkTheme() -> Color.Black
        Settings.paintMode && !isSystemInDarkTheme() -> Color.White
        else -> MaterialTheme.colorScheme.surfaceContainerLow
    }

    if (Settings.mica) {
        val wm = LocalContext.current.getSystemService<WallpaperManager>()

        LaunchedEffect(Unit) {
            if (wallpaperIB == null && Environment.isExternalStorageManager()) {
                wallpaperIB = wm?.drawable?.toBitmap()?.asImageBitmap()
            }
        }

        if (wallpaperIB != null) {
            Image(
                wallpaperIB!!,
                null,
                modifier.fillMaxSize().blur(45.dp),
                contentScale = ContentScale.Crop,
                filterQuality = FilterQuality.None
            )
        }
    }

    Canvas(modifier.fillMaxSize()) {
        drawRect(color)
    }
}
