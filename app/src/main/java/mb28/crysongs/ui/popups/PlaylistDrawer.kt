package mb28.crysongs.ui.popups

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import mb28.crysongs.core.Settings
import mb28.crysongs.core.Track
import mb28.crysongs.icons.edit_note
import mb28.crysongs.icons.reorder
import mb28.crysongs.playerQuery
import mb28.crysongs.tracks
import mb28.crysongs.ui.other.TrackTile
import mb28.crysongs.updateDisplayQuery
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaylistDrawer(playlist: String, onDismiss: () -> Unit) {
    val songs = remember { mutableListOf<Track>() }
    if (songs.isEmpty()) {
        var exists = false
        var plPath = ""
        if (playlist == "#fav") {
            tracks.forEach { track ->
                if (Settings.favorites.contains(track.path)) {
                    songs.add(track)
                }
            }
        } else {
            Settings.playlists.forEach { path ->
                if (!exists) {
                    if (path.substring(0, path.length - 7).endsWith(playlist)) {
                        exists = true
                        plPath = path
                    }
                }
            }
            if (exists) {
                val pl = File(plPath).readLines()
                pl.forEach { plItem ->
                    if (plItem.startsWith("Music -> ")) {
                        val path = plItem.substring(9)
                        println(path)
                        if (plItem.contains(path)) {
                            songs.add(tracks.find { it.path == path }!!)
                        }
                    }
                }
            }
        }
    }
    ModalBottomSheet({ onDismiss() }, dragHandle = {}) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(5.dp),
            Arrangement.End,
            Alignment.CenterVertically
        ) {
            Text(
                playlist,
                Modifier.fillMaxWidth(0.7f),
                maxLines = 1
            )
            FilledTonalIconButton(
                { }
            ) {
                Icon(edit_note, null)
            }
            FilledTonalIconButton(
                { }
            ) {
                Icon(reorder, null)
            }
        }
        LazyColumn {
            val count = songs.count()
            items(count) { i ->
                TrackTile(songs[i], i, count, false) {
                    playerQuery = songs.toMutableStateList()
                    updateDisplayQuery()
                }
            }
            item {
                Spacer(Modifier.height(200.dp))
            }
        }
    }
}