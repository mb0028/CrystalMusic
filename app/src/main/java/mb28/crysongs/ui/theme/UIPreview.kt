package mb28.crysongs.ui.theme

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.LinearWavyProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.fastForEachIndexed
import mb28.crysongs.R
import mb28.crysongs.core.Settings
import mb28.crysongs.core.Settings.fsStyle
import mb28.crysongs.ui.fullscreen_player.fullscreenCoverShape
import mb28.monoP.icons.add_2

@Composable
fun UIPreview(modifier: Modifier = Modifier) {
    Box(
        modifier.aspectRatio(9f / 16f).clip(RoundedCornerShape(30.dp))
    ) {
        Column(
            modifier.fillMaxSize()
                .background(
                    when {
                        Settings.mica -> Color.Transparent
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
                        .height(if (Settings.twoRowTrackCard) 35.dp else 45.dp)
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
                val color = MaterialTheme.colorScheme.surfaceContainerHigh
                val bgShape = if (Settings.hideNavBar) RoundedCornerShape(15.dp) else
                    RoundedCornerShape(15.dp, 15.dp ,0.dp, 0.dp)
                Box(
                    Modifier.fillMaxWidth().height(40.dp)
                    .padding(horizontal = if (Settings.hideNavBar) 10.dp else 0.dp)
                    .background(
                        Brush.horizontalGradient(
                            0f to if (Settings.gradientColoring)
                                MaterialTheme.colorScheme.secondaryContainer else color,
                            1f to color
                        ),
                        bgShape
                    )
                )
                if (!Settings.hideNavBar) {
                    Row(
                        Modifier.fillMaxWidth().height(45.dp)
                            .background(MaterialTheme.colorScheme.surfaceContainerHigh),
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
private fun NavItems() {
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
        val bg = MaterialTheme.colorScheme.background
        if (fsStyle != 1) {
            Image(
                painterResource(R.drawable.null_track_cover), null,
                Modifier.fillMaxSize()
                    .blur((Settings.backgroundBlurRadius * 0.8f).dp)
                    .drawWithContent {
                        drawContent()
                        if (fsStyle == 2) {
                            drawRect(
                                Brush.verticalGradient(
                                    0f to Color.Transparent,
                                    0.7f to bg
                                )
                            )
                        }
                    },
                contentScale = ContentScale.Crop,
            )
        }

        Column(
            modifier.fillMaxSize()
                .background(
                    when {
                        fsStyle != 1 || Settings.mica -> Color.Transparent
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
                Box(Modifier.size(30.dp).background(
                    MaterialTheme.colorScheme.surfaceBright.copy(
                        if (fsStyle == 1) 1f else 0.5f
                    ),
                    CircleShape))

                if (fsStyle == 0) {
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

                Box(Modifier.size(30.dp).background(
                    MaterialTheme.colorScheme.surfaceBright.copy(0.5f), CircleShape))
            }

            // Cover ----------------------
            Column(Modifier.fillMaxWidth().padding(horizontal = 10.dp)) {
                Box(Modifier.aspectRatio(1f)
                    .padding(8.dp)
                    .background(MaterialTheme.colorScheme.surfaceBright,
                        fullscreenCoverShape(Settings.coverShapeMode)))
            }

            // For track name
            Spacer(Modifier.height(30.dp))

            // Player ----------------------
            Column {
                if (fsStyle == 2) {
                    LinearProgressIndicator({0.5f}, Modifier.padding(horizontal = 15.dp))
                } else {
                    LinearWavyProgressIndicator({0.5f}, Modifier.padding(horizontal = 15.dp))
                }

                Spacer(Modifier.height(10.dp))
                Row(
                    Modifier.fillMaxWidth(),
                    Arrangement.SpaceEvenly,
                    Alignment.CenterVertically
                ) {
                    when (fsStyle) {
                        0 -> {
                            Box(Modifier.size(25.dp)
                                .background(Color.White.copy(0.4f), CircleShape))
                            Box(Modifier.size(30.dp)
                                .background(Color.White.copy(0.6f), CircleShape))
                            Box(Modifier.size(40.dp)
                                .background(Color.White.copy(0.8f),RoundedCornerShape(10.dp)))
                            Box(Modifier.size(30.dp)
                                .background(Color.White.copy(0.6f), CircleShape))
                            Box(Modifier.size(25.dp)
                                .background(Color.White.copy(0.4f), CircleShape))
                        }
                        1 -> {
                            Column(
                                Modifier.fillMaxWidth(),
                                Arrangement.Center,
                                Alignment.CenterHorizontally
                            ) {
                                Box(Modifier.size(120.dp, 30.dp).background(Color.White.copy(0.8f),
                                        RoundedCornerShape(10.dp)))
                                Row(
                                    Modifier.fillMaxWidth(),
                                    Arrangement.Center,
                                    Alignment.CenterVertically
                                ) {
                                    Box(Modifier.size(25.dp)
                                        .background(Color.White.copy(0.4f), CircleShape))
                                    Box(Modifier.size(30.dp)
                                        .background(Color.White.copy(0.6f), CircleShape))
                                    Box(Modifier.size(30.dp)
                                        .background(Color.White.copy(0.6f), CircleShape))
                                    Box(Modifier.size(25.dp)
                                        .background(Color.White.copy(0.4f), CircleShape))
                                }
                            }
                        }
                        else -> {
                            Icon(add_2, null)
                            Icon(add_2, null)
                            Icon(add_2, null)
                            Icon(add_2, null)
                            Icon(add_2, null)
                        }
                    }
                }

                // For lyrics
                Spacer(Modifier.height(30.dp))
            }
        }
    }
}
