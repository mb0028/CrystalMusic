package mb28.crysongs.ui.fullscreen_player

import android.annotation.SuppressLint
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FloatingToolbarDefaults
import androidx.compose.material3.HorizontalFloatingToolbar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import mb28.crysongs.R
import mb28.crysongs.core.Settings
import mb28.crysongs.core.inverseLerp
import mb28.crysongs.duration
import mb28.crysongs.icons.align_justify_flex_end
import mb28.crysongs.icons.list_2
import mb28.crysongs.icons.move_down
import mb28.crysongs.icons.swipe_vertical
import mb28.crysongs.isPlaying
import mb28.crysongs.lastLrcLineI
import mb28.crysongs.lrcParser
import mb28.crysongs.player
import mb28.crysongs.position
import mb28.crysongs.shouldScrollLyrics

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun FSLyricsTab(modifier: Modifier = Modifier) {
    val state = rememberLazyListState()
    val scope = rememberCoroutineScope()
    if (lrcParser != null) {

        if (Settings.lyricsAutoScroll && shouldScrollLyrics && lrcParser!!.IsGettingLineInRealtimePossible && !state.isScrollInProgress) {
            scope.launch {
                shouldScrollLyrics = false
                state.animateScrollToItem(lrcParser!!.LineIndex(position), -400)
            }
        }

        Box(
            modifier,
            Alignment.BottomCenter
        ) {
            if (Settings.verticalLyrics) {
                LazyRow(
                    Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically,
                    contentPadding = PaddingValues(horizontal = 0.dp),
                    reverseLayout = true,
                    state = state
                ) {
                    items(lrcParser?.Count ?: 0) {
                        LyricText(it, state, scope)
                    }
                }
            }
            else {
                LazyColumn(
                    Modifier.padding(horizontal = 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    contentPadding = PaddingValues(vertical = 200.dp),
                    state = state
                ) {
                    items(lrcParser?.Count ?: 0) {
                        LyricText(it, state, scope)
                    }
                }
            }
            HorizontalFloatingToolbar(
                false,
                Modifier.navigationBarsPadding().padding(bottom = 5.dp),
                colors = FloatingToolbarDefaults.vibrantFloatingToolbarColors(
                    MaterialTheme.colorScheme.primaryContainer.copy(0.8f)
                )
            ) {
                IconButton(
                    { Settings.verticalLyrics = !Settings.verticalLyrics; Settings.save() }
                ) {
                    Icon(
                        if (Settings.verticalLyrics) align_justify_flex_end else list_2,
                        null
                    )
                }
                FilledIconButton(
                    {
                        if (player.isPlaying) {
                            player.pause()
                        } else {
                            player.play()
                        }
                        isPlaying = player.isPlaying
                    },
                ) {
                    Icon(
                        if (isPlaying) painterResource(R.drawable.pause)
                        else painterResource(R.drawable.play),
                        "Play / Pause",
                    )
                }
                IconButton(
                    { Settings.lyricsAutoScroll = !Settings.lyricsAutoScroll; Settings.save() }
                ) {
                    Icon(
                        if (Settings.lyricsAutoScroll) move_down else swipe_vertical,
                        null
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

@Composable
private fun LyricText(i: Int, state: LazyListState, scope: CoroutineScope) {
    val lineAnim = animateFloatAsState(
        if (i == lastLrcLineI) 0.5f else 0f,
        TweenSpec(400)
    )
    val line = lrcParser!!.LyricLines[i]
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
                        if (!isPlaying) { player.play() }
                        if (Settings.lyricsAutoScroll) {
                            state.scrollToItem(i, -400)
                        }
                    }
                }
            },
    ) {
        Text(
            if (Settings.verticalLyrics) {
                line.Lyric.asSequence().joinToString("\n")
            } else {
                when {
                    line.Lyric3 != null -> "${line.Lyric}\n${line.Lyric2!!}\n${line.Lyric3}"
                    line.Lyric2 != null -> "${line.Lyric}\n${line.Lyric2}"
                    else -> line.Lyric
                }
            },

            modifier = Modifier.padding(8.dp, 6.dp),
            textAlign = TextAlign.Center
        )
    }
}