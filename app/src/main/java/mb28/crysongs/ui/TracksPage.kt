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
import mb28.crysongs.isReloading
import mb28.crysongs.tracks
import mb28.crysongs.ui.other.ShuffleButton
import mb28.crysongs.ui.other.TrackTile

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun TracksList(modifier: Modifier = Modifier) {
    val state = rememberLazyListState()
    LazyColumn(
        contentPadding = PaddingValues(top = 130.dp, bottom = 200.dp),
        state = state
    ) {
        item {
            Text(
                "All Tracks (${tracks.count()})", fontSize = 36.sp, textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(33.dp))
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
            val count = tracks.count()
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
