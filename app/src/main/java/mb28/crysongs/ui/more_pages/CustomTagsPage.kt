package mb28.crysongs.ui.more_pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.fastMap
import mb28.crysongs.core.Track
import mb28.crysongs.icons.arrow_back
import mb28.crysongs.icons.list_2
import mb28.crysongs.icons.shuffle
import mb28.crysongs.playerQuery
import mb28.crysongs.setAndPlay
import mb28.crysongs.tracks
import mb28.crysongs.ui.other.EasySegmentedListItem
import mb28.crysongs.ui.other.TrackTile
import mb28.crysongs.updateDisplayQuery
import kotlin.text.lastIndexOf
import kotlin.text.substring

@Composable
fun CustomTagsPage(list: SnapshotStateList<String>, listType: String) {
    var listItemsView by remember { mutableStateOf(false) }
    var clickedListItem by remember { mutableStateOf("") }

    fun getListTracks() : MutableList<Track> {
        val listTracks = tracks.toMutableList()
        when(listType) {
            "artists" -> listTracks.removeIf {
                it.artist != clickedListItem
            }
            "albums" -> listTracks.removeIf {
                it.album != clickedListItem
            }
            "genres" -> listTracks.removeIf {
                it.genre != clickedListItem
            }
            "composers" -> listTracks.removeIf {
                it.composer != clickedListItem
            }
            else -> { listTracks.clear() }
        }
        return listTracks
    }

    LazyColumn(
        contentPadding = PaddingValues(top = 130.dp, bottom = 200.dp),
    ) {
        item {
            Text(
                if (listItemsView) clickedListItem else "${listType[0].uppercase() + listType.substring(1)} (${list.count()})",
                fontSize = 36.sp,
                textAlign = TextAlign.Center,
                lineHeight = 40.sp,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(33.dp))
        }

        item {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(top = 40.dp, bottom = 10.dp, start = 10.dp, end = 10.dp),
                horizontalArrangement = Arrangement.Start
            ) {
                if (listItemsView) {
                    FilledTonalIconButton(
                        { listItemsView = false }
                    ) {
                        Icon(arrow_back, null)
                    }
                    FilledTonalIconButton(
                        {
                            val folderTracks = getListTracks()
                            playerQuery = folderTracks.shuffled().toMutableStateList()
                            updateDisplayQuery()
                            setAndPlay(playerQuery.first(), false)
                        }
                    ) {
                        Icon(shuffle, null)
                    }
                }
            }
        }

        if (listItemsView) {
            val listTracks = getListTracks()
            val count = listTracks.count()
            items(count) { i ->
                TrackTile(
                    listTracks[i],
                    i, count,
                    resetQueryOnClick = false
                ) {
                    playerQuery = listTracks.toMutableStateList()
                    updateDisplayQuery()
                }
            }
        }
        else {
            val count = list.count()
            items(count) { i ->
                val f = list[i]
                EasySegmentedListItem(
                    null,
                    f,
                    i, count,
                    Modifier.padding(horizontal = 10.dp)
                ) {
                    clickedListItem = list[i]
                    listItemsView = true
                }
            }
        }
    }
}