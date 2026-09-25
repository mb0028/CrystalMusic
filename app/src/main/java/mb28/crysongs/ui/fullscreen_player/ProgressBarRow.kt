package mb28.crysongs.ui.fullscreen_player

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.SpringSpec
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.LinearWavyProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.WavyProgressIndicatorDefaults
import androidx.compose.material3.rememberSliderState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastCoerceAtLeast
import mb28.crysongs.core.formatDurationMs
import mb28.crysongs.duration
import mb28.crysongs.isPlaying
import mb28.crysongs.player
import mb28.crysongs.position
import kotlin.math.roundToLong
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun FSProgressBarRow(modifier: Modifier = Modifier) {
    val pos = position.toFloat()
    val state = rememberSliderState(pos)
    val animatedPos by animateFloatAsState(
        (pos.fastCoerceAtLeast(1f) / duration.fastCoerceAtLeast(1)),
        WavyProgressIndicatorDefaults.ProgressAnimationSpec
    )
    val seekThumbHeight by animateDpAsState(
        if (isPlaying) 30.dp else 15.dp,
        SpringSpec(
            Spring.DampingRatioMediumBouncy,
            Spring.StiffnessMediumLow
        )
    )
    Row(modifier) {
        TextButton(
            { player.seekTo((position - 5000).toLong()) }
        ) {
            Text(formatDurationMs(position.milliseconds))
        }
        Box(
            Modifier
                .fillMaxWidth(0.77f)
                .padding(horizontal = 10.dp),
            Alignment.Center
        ) {
            Slider(
                state,
                onValueChange = {
                    state.value = it
                    player.seekTo((it * duration).roundToLong())
                },
                track = {
                    LinearWavyProgressIndicator(
                        { it.value },
                        wavelength = 24.dp,
                        amplitude = { if (isPlaying) 1f else 0f }
                    )
                },
                thumb = {
                    Box(
                        Modifier.size(5.dp, seekThumbHeight)
                            .background(
                                MaterialTheme.colorScheme.secondary,
                                CircleShape
                            )
                    )
                }
            )
        }
        TextButton(
            { player.seekTo((position + 5000).toLong()) }
        ) {
            Text(formatDurationMs(duration.milliseconds))
        }
    }
    state.value = animatedPos
}