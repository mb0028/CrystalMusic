package mb28.crysongs.ui.fullscreen_player

import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.graphics.shapes.CornerRounding
import androidx.graphics.shapes.RoundedPolygon
import androidx.graphics.shapes.star
import mb28.crysongs.core.Settings
import mb28.crysongs.nowPlayingCover
import mb28.crysongs.ui.other.audioBand

@Composable
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
fun fullscreenCoverShape(shape: Int) = when(shape) {
    0 -> RoundedCornerShape(5.dp)
    1 -> RoundedCornerShape(40.dp)
    2 -> {
        val size = 0.53f
        RoundedPolygon.star(
            12,
            radius = size,
            innerRadius = 0.43f,
            centerX = size * 0.5f,
            centerY = size * 0.5f,
            rounding = CornerRounding(80f)
        ).toShape()
    }
    3 -> {
        val size = 0.55f
        RoundedPolygon.star(
            7,
            radius = size,
            innerRadius = 0.42f,
            centerX = size * 0.5f,
            centerY = size * 0.5f,
            rounding = CornerRounding(80f)
        ).toShape()
    }
    4 -> CircleShape
    else -> throw Exception("Invalid shape")
}

@Composable
fun FSCover(modifier: Modifier = Modifier) {
    val animScale by animateFloatAsState(
        if (Settings.waveformDataCapture && Settings.coverParallax)
            1f + audioBand(0.1f) else 1f,
        animationSpec = TweenSpec(100)
    )

    Box(
        modifier
            .aspectRatio(1f)
            .scale(animScale)
            .background(
                MaterialTheme.colorScheme.surfaceContainer,
                fullscreenCoverShape(Settings.coverShapeMode)
            )
            .clip(fullscreenCoverShape(Settings.coverShapeMode))
            .clickable {
                Settings.coverShapeMode = when(Settings.coverShapeMode) {
                    0 -> 1
                    1 -> 2
                    2 -> 3
                    3 -> 4
                    else -> 0
                }
                Settings.save()
            }
    ) {
        Image(
            nowPlayingCover,
            "Track cover",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxWidth()
        )
    }
}
