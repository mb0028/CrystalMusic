package mb28.crysongs.ui.popups

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import mb28.crysongs.core.Settings
import mb28.crysongs.core.Track
import mb28.crysongs.icons.list_2
import mb28.crysongs.icons.playlist_add
import mb28.crysongs.icons.playlist_add_check_circle
import mb28.crysongs.icons.queue_music
import mb28.crysongs.ui.other.EasySegmentedListItem
import java.io.File

@Composable
fun AddToPlaylistPopup(track: Track, onDismissRequired: () -> Unit) {
    AlertDialog(
        { onDismissRequired() },
        {
            OutlinedButton({ onDismissRequired() }) {
                Text("Ok")
            }
        },
        title = {
            Text("Add to playlist")
        },
        text = {
            LazyColumn {
                val count = Settings.playlists.count()
                items(count) { i ->
                    val playlistItem by remember { mutableStateOf("Music -> " + track.path) }
                    val pl = remember { File(Settings.playlists[i]).readLines().toMutableStateList() }
                    var isIn by remember { mutableStateOf(pl.contains(playlistItem)) }
                    EasySegmentedListItem(
                        if (isIn) playlist_add_check_circle else playlist_add,
                        if (pl[0].startsWith("Name -> ")) pl[0].removePrefix("Name -> ")
                        else "??? (Corrupted name)",
                        i, count,
                        Modifier.padding(horizontal = 15.dp)
                    ) {
                        if (isIn) {
                            pl.remove(playlistItem)
                        } else {
                            pl.add(1, playlistItem)
                        }

                        isIn = pl.contains(playlistItem)
                        File(Settings.playlists[i]).writeText(pl.joinToString("\n"))
                    }
                }
            }
        }
    )
}