package mb28.crysongs.ui.other

import android.app.Activity
import android.view.RoundedCorner
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import mb28.crysongs.core.Settings
import mb28.crysongs.core.Settings.edgeLightingHue1
import mb28.crysongs.core.Settings.edgeLightingHue2
import mb28.crysongs.core.Settings.edgeLightingHue3
import mb28.crysongs.core.Settings.edgeLightingHue4
import mb28.crysongs.core.Settings.edgeLightingLightness
import mb28.crysongs.core.Settings.edgeLightingSaturation
import mb28.crysongs.isPlaying
import mb28.crysongs.visualizationData

@Composable
fun EdgeLightingEffect(activity: Activity, demo: Boolean = false) {
    if (Settings.experimental && Settings.edgeLighting) {
        val alpha by animateFloatAsState(
            if (demo) 1f else audioBand(0.0035),
            animationSpec = TweenSpec(50)
        )
        val corners = activity.window.decorView.rootWindowInsets
            .getRoundedCorner(RoundedCorner.POSITION_TOP_LEFT)?.radius ?: 0
        Box(
            Modifier.fillMaxSize()
                .alpha(alpha)
                .border(
                    BorderStroke(
                        6.dp,
                        Brush.verticalGradient(
                            Pair(0f, Color.hsl(edgeLightingHue1, edgeLightingSaturation, edgeLightingLightness)),
                            Pair(0.35f, Color.hsl(edgeLightingHue2, edgeLightingSaturation, edgeLightingLightness)),
                            Pair(0.65f, Color.hsl(edgeLightingHue3, edgeLightingSaturation, edgeLightingLightness)),
                            Pair(1f, Color.hsl(edgeLightingHue4, edgeLightingSaturation, edgeLightingLightness))
                        )
                    ),
                    RoundedCornerShape((corners * 0.42f).dp)
                )
        )
    }
}

fun audioBand(norm: Double) : Float {
    if (!isPlaying) return 0f
    val samp = visualizationData.resample(2)
    if (samp.count() >= 2) {
        return (samp[1] * norm).toFloat().coerceIn(0f, 1f)
    }
    return 0f
}
