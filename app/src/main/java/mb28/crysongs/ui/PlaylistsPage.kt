package mb28.crysongs.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import mb28.crysongs.core.Settings
import mb28.crysongs.icons.download
import mb28.crysongs.icons.favorite
import mb28.crysongs.icons.library_add
import mb28.crysongs.icons.playlist_play
import mb28.crysongs.ui.other.EasySegmentedListItem
import mb28.crysongs.ui.popups.CreatePlaylistPopup
import mb28.crysongs.ui.popups.PlaylistDrawer
import java.io.File

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun PlaylistsPage() {
    var showNewPlaylistPopup by remember { mutableStateOf(false) }
    var lastClickedPlaylist by remember { mutableStateOf("") }
    var showDrawer by remember { mutableStateOf(false) }
    val state = rememberLazyListState()
    LazyColumn(
        contentPadding = PaddingValues(top = 130.dp, bottom = 200.dp),
        state = state
    ) {
        item {
            Text(
                "Playlists", fontSize = 36.sp, textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(33.dp))
        }

        item {
            Row(
                Modifier.fillMaxWidth().padding(top = 40.dp, bottom = 10.dp, start = 10.dp, end = 10.dp),
                horizontalArrangement = Arrangement.Start
            ) {
                FilledTonalIconButton(
                    { }
                ) {
                    Icon(download, null)
                }
                FilledTonalIconButton(
                    { showNewPlaylistPopup = true }
                ) {
                    Icon(library_add, null)
                }
            }
        }

        val count = Settings.playlists.count() + 1
        item {
            EasySegmentedListItem(
                favorite,
                "Favorites (${Settings.favorites.count()})",
                0, count,
                Modifier.padding(horizontal = 15.dp)
            ) {
                lastClickedPlaylist = "#fav"
                showDrawer = true
            }
        }

        items(count - 1) { i ->
            val pl by remember { mutableStateOf(
                File(Settings.playlists[i]).readLines()
            ) }
            EasySegmentedListItem(
                playlist_play,
                if (pl[0].startsWith("Name -> "))
                    pl[0].removePrefix("Name -> ") + " (${pl.count() - 1})"
                    else "??? (Corrupted name)",
                i + 1, count,
                Modifier.padding(horizontal = 15.dp)
            ) {
                lastClickedPlaylist = Settings.playlists[i]
                showDrawer = true
            }
        }
    }

    if (showNewPlaylistPopup) {
        CreatePlaylistPopup {
            showNewPlaylistPopup = false
        }
    }

    if (showDrawer) {
        PlaylistDrawer(
            File(lastClickedPlaylist).nameWithoutExtension
        ) {
            showDrawer = false
        }
    }

}
