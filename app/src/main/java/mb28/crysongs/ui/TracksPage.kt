package mb28.crysongs.ui

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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import mb28.crysongs.core.Settings
import mb28.crysongs.isReloading
import mb28.crysongs.memUsage
import mb28.crysongs.tracks
import mb28.crysongs.ui.other.CacheCoversCard
import mb28.crysongs.ui.other.ShuffleButton
import mb28.crysongs.ui.other.TrackTile

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun TracksList() {
    val state = rememberLazyListState()
    LazyColumn(
        contentPadding = PaddingValues(top = 130.dp, bottom = 200.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        state = state,
    ) {
        val count = tracks.count()
        item {
            if (Settings.tips_cacheThumbs) {
                CacheCoversCard(
                    Modifier.fillMaxWidth().padding(horizontal = 15.dp)
                )
            }
            Text(
                "All Tracks (${count})", fontSize = 36.sp, textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            if (Settings.experimental) {
                Spacer(Modifier.height(10.dp))
                Text("experimental features are on\njvm memory usage: ${memUsage / 1024 / 1024}mb",
                    textAlign = TextAlign.Center, fontSize = 13.sp)
            } else {
                Spacer(Modifier.height(33.dp))
            }
        }

        item {
            Row(
                Modifier.fillMaxWidth().padding(top = 40.dp, bottom = 10.dp, start = 10.dp, end = 10.dp),
                horizontalArrangement = Arrangement.Start
            ) {
                ShuffleButton()
            }
        }

        if (!isReloading) {
            items(count) { i ->
                TrackTile(tracks[i], i, count)
            }
        }
        else {
            item {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    ContainedLoadingIndicator()
                }
            }
        }
    }
}
