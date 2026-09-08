package mb28.crysongs.ui.fullscreen_player

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearWavyProgressIndicator
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.WavyProgressIndicatorDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import mb28.crysongs.core.formatDurationMs
import mb28.crysongs.duration
import mb28.crysongs.isPlaying
import mb28.crysongs.player
import mb28.crysongs.position
import kotlin.math.roundToInt
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun FSProgressBarRow(modifier: Modifier = Modifier) {
    val pos = position.toFloat()
    val animatedPos = animateFloatAsState(
        (pos / duration).takeIf { pos != 0f } ?: 0f,
        WavyProgressIndicatorDefaults.ProgressAnimationSpec
    )
    Row(modifier) {
        TextButton(
            { player.seekTo(position - 5000) }
        ) {
            Text(formatDurationMs(position.milliseconds))
        }
        Box(
            Modifier
                .fillMaxWidth(0.77f)
                .padding(horizontal = 10.dp),
            Alignment.Center
        ) {
            LinearWavyProgressIndicator(
                { animatedPos.value },
                wavelength = 24.dp,
                amplitude = { if (isPlaying) 1f else 0f }
            )
            Slider(
                0f,
                { player.seekTo((it * duration).roundToInt()) },
                Modifier.alpha(0f)
            )
        }
        TextButton(
            { player.seekTo(position + 5000) }
        ) {
            Text(formatDurationMs(duration.milliseconds))
        }
    }
}