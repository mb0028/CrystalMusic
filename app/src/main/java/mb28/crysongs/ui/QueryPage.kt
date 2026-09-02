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
import androidx.compose.material3.ContainedLoadingIndicator
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import mb28.crysongs.displayQuery
import mb28.crysongs.icons.queue_music
import mb28.crysongs.isReloading
import mb28.crysongs.playerQuery
import mb28.crysongs.setAndPlay
import mb28.crysongs.tracks
import mb28.crysongs.ui.other.ShuffleButton
import mb28.crysongs.ui.other.TrackTile

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun QueryPage() {
    LazyColumn(
        contentPadding = PaddingValues(top = 100.dp, bottom = 200.dp),
    ) {
        item {
            Text(
                "Query", fontSize = 36.sp, textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(15.dp))
            Text(
                "Query only shows last 4 & next 10 tracks.\nTo see more, play one of next/previous tracks",
                fontSize = 16.sp, textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

        }

        item {
            Row(
                Modifier.fillMaxWidth().padding(top = 40.dp, bottom = 10.dp, start = 10.dp, end = 10.dp),
                horizontalArrangement = Arrangement.Start
            ) {
                ShuffleButton()
            }
        }

        if (isReloading) {
            item {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    ContainedLoadingIndicator()
                }
            }
        }
        else if (displayQuery.isNotEmpty() && !isReloading) {
            val count = displayQuery.count()
            items(count) { i ->
                TrackTile(displayQuery[i], i, count, false)
            }
        }
        else {
            item {
                Spacer(Modifier.height(100.dp))
                Text(
                    "(┬┬﹏┬┬)", fontSize = 40.sp, textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(15.dp))
                Text(
                    "Playing query is empty!\nPlay something or click randomize button",
                    fontSize = 16.sp, textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

    }
}