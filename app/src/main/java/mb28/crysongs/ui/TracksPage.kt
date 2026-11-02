package mb28.crysongs.ui

import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ContainedLoadingIndicator
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import mb28.crysongs.core.Settings
import mb28.crysongs.core.pageAnimation
import mb28.crysongs.isReloading
import mb28.crysongs.refreshTracksList
import mb28.crysongs.tracks
import mb28.crysongs.ui.other.CacheCoversCard
import mb28.crysongs.ui.other.ShuffleButton
import mb28.crysongs.ui.other.TrackTile
import kotlin.time.Duration.Companion.milliseconds

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun TracksPage(context: Context) {
    val state = remember { MutableTransitionState(false).apply { targetState = true } }
    val scope = rememberCoroutineScope()

    AnimatedVisibility(
        visibleState = state,
        enter = pageAnimation,
    ) {
        val state = rememberLazyListState()
        var refreshing by remember { mutableStateOf(false) }
        PullToRefreshBox(
            refreshing,
            {
                scope.launch {
                    refreshing = true
                    isReloading = true
                    delay(100.milliseconds)
                    refreshTracksList(context)
                    refreshing = false
                    isReloading = false
                }
            }
        ) {
            LazyColumn(
                contentPadding = PaddingValues(top = 130.dp, bottom = 260.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                state = state,
            ) {
                val count = tracks.count()
                item {
                    if (Settings.tips_cacheThumbs) {
                        CacheCoversCard(
                            Modifier.fillMaxWidth().padding(15.dp)
                        )
                    }
                    Text(
                        "All Tracks (${count})", fontSize = 36.sp, textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.height(33.dp))
                }

                item {
                    Row(
                        Modifier.fillMaxWidth()
                            .padding(top = 40.dp, bottom = 10.dp, start = 15.dp, end = 10.dp),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        ShuffleButton()
                    }
                }

                if (count != 0 && !isReloading) {
                    items(count) { i ->
                        TrackTile(tracks[i], i, count)
                    }
                } else {
                    item {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            ContainedLoadingIndicator()
                        }
                    }
                }
            }
        }
    }
}
