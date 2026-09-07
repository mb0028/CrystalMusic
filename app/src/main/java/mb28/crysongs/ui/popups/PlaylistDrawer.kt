package mb28.crysongs.ui.popups

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.FilledTonalToggleButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
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
import androidx.compose.ui.unit.dp
import mb28.crysongs.core.Settings
import mb28.crysongs.core.Track
import mb28.crysongs.icons.edit_note
import mb28.crysongs.icons.reorder
import mb28.crysongs.icons.swipe_down_alt
import mb28.crysongs.icons.swipe_up_alt
import mb28.crysongs.playerQuery
import mb28.crysongs.tracks
import mb28.crysongs.ui.other.TrackTile
import mb28.crysongs.updateDisplayQuery
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaylistDrawer(playlist: String, onDismiss: () -> Unit) {
    var reorderMode by remember { mutableStateOf(false) }
    var plPath by remember { mutableStateOf("") }
    val songs = remember { mutableStateListOf<Track>() }

    fun save() {
        val pl = File(plPath).readLines()
        val newPl = mutableListOf<String>()
        newPl.add(pl.first())
        songs.forEach {
            newPl.add("Music -> " + it.path)
        }
        File(plPath).writeText(newPl.joinToString("\n"))
    }

    if (songs.isEmpty()) {
        var exists = false
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
                if (playlist == "#fav") "Favorites" else playlist,
                Modifier.fillMaxWidth(if (playlist == "#fav") 1f else 0.7f).padding(horizontal = 10.dp),
                maxLines = 1
            )

            if (playlist != "#fav") {
                FilledTonalIconButton(
                    { }
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
        LazyColumn() {
            val count = songs.count()
            items(count) { i ->
                Row(
                    Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
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
            item {
                Spacer(Modifier.height(200.dp))
            }
        }
    }
}