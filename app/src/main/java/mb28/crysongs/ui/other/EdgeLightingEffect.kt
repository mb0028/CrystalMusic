package mb28.crysongs.ui.other

import android.app.Activity
import android.view.RoundedCorner
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import mb28.crysongs.core.Settings
import mb28.crysongs.core.Settings.edgeLightingLightness
import mb28.crysongs.core.Settings.edgeLightingSaturation

@Composable
fun EdgeLightingEffect(activity: Activity) {
    if (Settings.experimental && Settings.edgeLighting) {
        val corners = activity.window.decorView.rootWindowInsets
            .getRoundedCorner(RoundedCorner.POSITION_TOP_LEFT)?.radius ?: 0
        Box(
            Modifier.fillMaxSize()
                .border(
                    BorderStroke(
                        6.dp,
                        Brush.verticalGradient(
                            Pair(0f, Color.hsl(0f, edgeLightingSaturation, edgeLightingLightness)),
                            Pair(0.35f, Color.hsl(90f, edgeLightingSaturation, edgeLightingLightness)),
                            Pair(0.65f, Color.hsl(180f, edgeLightingSaturation, edgeLightingLightness)),
                            Pair(1f, Color.hsl(320f, edgeLightingSaturation, edgeLightingLightness))
                        )
                    ),
                    RoundedCornerShape((corners * 0.42f).dp)
                )
        )
    }
}