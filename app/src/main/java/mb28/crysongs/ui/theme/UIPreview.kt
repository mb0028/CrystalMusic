package mb28.crysongs.ui.theme

import android.app.WallpaperManager
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.fastForEachIndexed
import androidx.core.content.getSystemService
import androidx.core.graphics.drawable.toBitmap
import mb28.crysongs.R
import mb28.crysongs.core.Settings
import mb28.crysongs.ui.fullscreen_player.fullscreenCoverShape

@Composable
fun UIPreview(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    Box(
        modifier.aspectRatio(9f / 16f).clip(RoundedCornerShape(30.dp))
    ) {
        if (Settings.showWallpaper) {
            val wall = remember { context.getSystemService<WallpaperManager>()?.drawable?.toBitmap()?.asImageBitmap() }
            if (wall != null) {
                Image(
                    wall, null,
                    Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
        }

        Column(
            modifier.fillMaxSize()
                .background(
                    when {
                        Settings.showWallpaper -> Color.Transparent
                        Settings.paintMode && isSystemInDarkTheme() -> Color.Black
                        Settings.paintMode && !isSystemInDarkTheme() -> Color.White
                        else -> MaterialTheme.colorScheme.surfaceContainer
                    },
                    RoundedCornerShape(30.dp)
                ),
            Arrangement.SpaceBetween
        ) {
            Row(Modifier.padding(start = 10.dp, top = 10.dp)) {
                Box(Modifier.size(35.dp)
                    .background(MaterialTheme.colorScheme.surfaceBright, CircleShape))
            }

            Column(Modifier.fillMaxWidth().padding(horizontal = 10.dp)) {
                for (i in 1..5) {
                    Box(Modifier.fillMaxWidth()
                        .height(if (Settings.twoRowTrackCard) 30.dp else 45.dp)
                        .padding(bottom = 3.dp)
                            .background(
                                if (Settings.gradientColoring) Brush.horizontalGradient(
                                    Pair(0f, MaterialTheme.colorScheme.secondaryContainer),
                                        Pair(0.5f, MaterialTheme.colorScheme.surfaceBright)
                                )
                                else Brush.horizontalGradient(
                                    Pair(0f, MaterialTheme.colorScheme.surfaceBright),
                                        Pair(1f, MaterialTheme.colorScheme.surfaceBright)),
                            RoundedCornerShape(8.dp)
                        )
                    )
                }
            }

            Column {
                Box(Modifier.fillMaxWidth().height(50.dp).padding( 5.dp)
                    .background(
                        if (Settings.floatingNavBar) MaterialTheme.colorScheme.inversePrimary.copy(0.9f)
                            else MaterialTheme.colorScheme.surfaceContainerHigh,
                        if (Settings.floatingNavBar) CircleShape else RoundedCornerShape(10.dp)
                    )
                )
                if (Settings.floatingNavBar) {
                    Row(
                        Modifier
                            .padding(10.dp, 0.dp, 10.dp, 10.dp)
                            .fillMaxWidth().height(45.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surfaceContainerHigh.copy(0.95f)),
                        Arrangement.SpaceEvenly,
                        Alignment.CenterVertically
                    ) {
                        NavItems()
                    }
                } else {
                    Row(
                        Modifier.fillMaxWidth().height(45.dp).background(MaterialTheme.colorScheme.surfaceContainerHigh),
                        Arrangement.SpaceEvenly,
                        Alignment.CenterVertically
                    ) {
                        NavItems()
                    }
                }
            }
        }
    }
}

@Composable
private fun RowScope.NavItems() {
    for (i in 1..5) {
        Box(Modifier.size(30.dp, 22.dp)
            .background(Color.White.copy(0.6f), CircleShape))
    }
}

@Composable
fun FSPlayerUIPreview(modifier: Modifier = Modifier) {
    Box(
        modifier.aspectRatio(9f / 16f).clip(RoundedCornerShape(30.dp))
    ) {
        if (Settings.fsStyle == 0) {
            Image(
                painterResource(R.drawable.null_track_cover), null,
                Modifier.fillMaxSize().blur((Settings.backgroundBlurRadius * 0.8f).dp),
                contentScale = ContentScale.Crop
            )
        }

        Column(
            modifier.fillMaxSize()
                .background(
                    when {
                        Settings.fsStyle == 0 -> Color.Transparent
                        Settings.paintMode && isSystemInDarkTheme() -> Color.Black
                        Settings.paintMode && !isSystemInDarkTheme() -> Color.White
                        else -> MaterialTheme.colorScheme.surfaceContainer
                    },
                    RoundedCornerShape(30.dp)
                ),
            Arrangement.SpaceBetween
        ) {
            // Topbar ----------------------
            Row(
                Modifier.fillMaxWidth().padding(10.dp),
                Arrangement.SpaceBetween,
                Alignment.CenterVertically
            ) {
                Box(Modifier.size(30.dp)
                    .background(MaterialTheme.colorScheme.surfaceBright, CircleShape))

                if (Settings.fsStyle == 0) {
                    Box(Modifier.size(100.dp, 30.dp)
                        .background(MaterialTheme.colorScheme.surfaceBright, CircleShape))
                } else {
                    val tabs = listOf("Details", "Player", "Lyrics")
                    Row {
                        tabs.fastForEachIndexed { i, t ->
                            Text(t, Modifier.padding(end = 5.dp).alpha(if (i == 1) 1f else 0.3f),
                                fontSize = 10.sp )
                        }
                    }
                }

                Box(Modifier.size(30.dp)
                    .background(MaterialTheme.colorScheme.surfaceBright.copy(0.5f),
                        CircleShape))
            }

            // Cover ----------------------
            Column(Modifier.fillMaxWidth().padding(horizontal = 10.dp)) {
                Box(Modifier.aspectRatio(1f)
                    .padding(8.dp)
                    .background(MaterialTheme.colorScheme.surfaceBright,
                        fullscreenCoverShape(Settings.coverShapeMode)))
            }

            // For track name
            Spacer(Modifier.height(40.dp))

            // Player ----------------------
            Column {
                Box(Modifier.fillMaxWidth().height(15.dp).padding(25.dp, 5.dp)
                    .background(MaterialTheme.colorScheme.primary, CircleShape))

                Spacer(Modifier.height(5.dp))
                Row(
                    Modifier.fillMaxWidth(),
                    Arrangement.SpaceEvenly,
                    Alignment.CenterVertically
                ) {
                    Box(Modifier.size(25.dp)
                        .background(Color.White.copy(0.4f), CircleShape))
                    Box(Modifier.size(30.dp)
                        .background(Color.White.copy(0.6f), CircleShape))
                    Box(Modifier.size(40.dp)
                        .background(Color.White.copy(0.8f), RoundedCornerShape(10.dp)))
                    Box(Modifier.size(30.dp)
                        .background(Color.White.copy(0.6f), CircleShape))
                    Box(Modifier.size(25.dp)
                        .background(Color.White.copy(0.4f), CircleShape))
                }

                // For lyrics
                Spacer(Modifier.height(30.dp))
            }
        }
    }
}
