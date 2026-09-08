package mb28.crysongs.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.FilledTonalToggleButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import mb28.crysongs.core.Settings
import mb28.crysongs.core.Track
import mb28.crysongs.icons.arrow_back
import mb28.crysongs.icons.delete_sweep
import mb28.crysongs.icons.download
import mb28.crysongs.icons.edit_note
import mb28.crysongs.icons.favorite
import mb28.crysongs.icons.library_add
import mb28.crysongs.icons.playlist_play
import mb28.crysongs.icons.reorder
import mb28.crysongs.icons.swipe_down_alt
import mb28.crysongs.icons.swipe_up_alt
import mb28.crysongs.playerQuery
import mb28.crysongs.tracks
import mb28.crysongs.ui.other.EasySegmentedListItem
import mb28.crysongs.ui.other.TrackTile
import mb28.crysongs.ui.popups.CreatePlaylistPopup
import mb28.crysongs.ui.popups.RenamePlaylistPopup
import mb28.crysongs.updateDisplayQuery
import java.io.File

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun PlaylistsPage() {
    val state = rememberLazyListState()
    var playlistView by remember { mutableStateOf(false) }
    var showNewPlaylistPopup by remember { mutableStateOf(false) }
    var showRenamePopup by remember { mutableStateOf(false) }
    var reorderMode by remember { mutableStateOf(false) }

    val songs = remember { mutableStateListOf<Track>() }
    var lastClickedPlPath by remember { mutableStateOf("") }
    var plName by remember { mutableStateOf("") }

    fun save(newName: String = plName) {
        plName = newName
        val newPl = mutableListOf<String>()
        newPl.add("Name -> $newName")
        songs.forEach {
            newPl.add("Music -> " + it.path)
        }
        File(lastClickedPlPath).writeText(newPl.joinToString("\n"))
    }

    LazyColumn(
        contentPadding = PaddingValues(top = 130.dp, bottom = 200.dp),
        state = state
    ) {
        item {
            Text(
                if (playlistView) (if (lastClickedPlPath == "#fav") "Favorites" else plName)
                    else "Playlists",
                fontSize = 36.sp,
                maxLines = 1,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(33.dp))
        }

        item {
            Row(
                Modifier.fillMaxWidth().padding(top = 40.dp, bottom = 10.dp, start = 10.dp, end = 10.dp),
                horizontalArrangement = Arrangement.Start
            ) {
                if (playlistView) {
                    FilledTonalIconButton(
                        { playlistView = false }
                    ) {
                        Icon(arrow_back, null)
                    }
                    if (lastClickedPlPath != "#fav") {
                        FilledTonalIconButton(
                            { showRenamePopup = true }
                        ) {
                            Icon(edit_note, null)
                        }
                        FilledTonalToggleButton(
                            reorderMode,
                            { reorderMode = it }
                        ) {
                            Icon(reorder, null)
                        }
                    }
                }
                else {
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
        }

        if (playlistView) {
            if (songs.isEmpty()) {
                if (lastClickedPlPath == "#fav") {
                    tracks.forEach { track ->
                        if (Settings.favorites.contains(track.path)) {
                            songs.add(track)
                        }
                    }
                } else {
                    val pl = File(lastClickedPlPath).readLines()
                    plName = pl.first().removePrefix("Name -> ")
                    pl.forEach { plItem ->
                        if (plItem.startsWith("Music -> ")) {
                            val path = plItem.substring(9)
                            if (plItem.contains(path)) {
                                val t = tracks.find { it.path == path }
                                if (t != null) {
                                    songs.add(t)
                                }
                            }
                        }
                    }
                }
            }

            val count = songs.count()
            items(count) { i ->
                Row(
                    Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (reorderMode) {
                        IconButton(
                            {
                                songs.removeAt(i)
                                save()
                            }
                        ) {
                            Icon(delete_sweep, null)
                        }
                    }

                    TrackTile(
                        songs[i],
                        i, count,
                        false,
                        Modifier.fillMaxWidth(if (reorderMode) 0.85f else 1f)
                    ) {
                        playerQuery = songs.toMutableStateList()
                        updateDisplayQuery()
                    }

                    if (reorderMode) {
                        Column {
                            IconButton(
                                {
                                    songs.add(i - 1, songs.removeAt(i))
                                    save()
                                },
                                enabled = i > 0
                            ) {
                                Icon(swipe_up_alt, null)
                            }
                            IconButton(
                                {
                                    songs.add(i + 1, songs.removeAt(i))
                                    save()
                                },
                                enabled = i < songs.count() - 1
                            ) {
                                Icon(swipe_down_alt, null)
                            }
                        }
                    }
                }
            }
        }
        else {
            val count = Settings.playlists.count() + 1
            item {
                EasySegmentedListItem(
                    favorite,
                    "Favorites (${Settings.favorites.count()})",
                    0, count,
                    Modifier.padding(horizontal = 15.dp)
                ) {
                    songs.clear()
                    lastClickedPlPath = "#fav"
                    playlistView = true
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
                    songs.clear()
                    lastClickedPlPath = Settings.playlists[i]
                    playlistView = true
                }
            }
        }

    }

    if (showNewPlaylistPopup) {
        CreatePlaylistPopup {
            showNewPlaylistPopup = false
        }
    }

    if (showRenamePopup) {
        RenamePlaylistPopup(plName) { newName, saved ->
            showRenamePopup = false
            if (saved) {
                save(newName)
            }
        }
    }

}
