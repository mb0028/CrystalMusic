package mb28.crysongs.ui.fullscreen_player

import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import kotlinx.coroutines.launch
import mb28.crysongs.core.inverseLerp
import mb28.crysongs.duration
import mb28.crysongs.lastLrcLineI
import mb28.crysongs.lrcParser
import mb28.crysongs.player

@Composable
fun FSLyricsTab(modifier: Modifier = Modifier) {
    val state = rememberLazyListState()
    val scope = rememberCoroutineScope()
    if (lrcParser != null) {
        LazyColumn(
            modifier,
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(vertical = 200.dp),
            state = state
        ) {
            items(lrcParser?.Count ?: 0) {
                val lineAnim = animateFloatAsState(
                    if (it == lastLrcLineI) 0.5f else 0f,
                    TweenSpec(400)
                )
                val line = lrcParser!!.LyricLines[it]
                Box(
                    Modifier
                        .padding(6.dp)
                        .graphicsLayer {
                            val s = 1.05f + (lineAnim.value * 0.18f)
                            scaleX = s
                            scaleY = s
                        }
                        .background(
                            MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = lineAnim.value),
                            RoundedCornerShape(20.dp)
                        )
                        .clip(RoundedCornerShape(20.dp))
                        .clickable {
                            scope.launch {
                                if (lrcParser!!.IsGettingLineInRealtimePossible) {
                                    player.seekTo(
                                        lerp(
                                            0, duration,
                                            inverseLerp(0f, duration / 1000f, line.TimeStomp),
                                        )
                                    )
                                    state.scrollToItem(it, -500)
                                }
                            }
                        },
                ) {
                    Text(
                        when {
                            line.Lyric3 != null -> "${line.Lyric}\n${line.Lyric2!!}\n${line.Lyric3}"
                            line.Lyric2 != null -> "${line.Lyric}\n${line.Lyric2}"
                            else -> line.Lyric
                        },
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
    else {
        Column(
            modifier,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                "(。﹏。*)", fontSize = 40.sp, textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(15.dp))
            Text(
                "No Lyrics...",
                fontSize = 16.sp, textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}