package mb28.crysongs.ui.popups

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import mb28.crysongs.core.Settings
import mb28.crysongs.icons.playlist_add
import mb28.crysongs.icons.playlist_add_check_circle
import mb28.crysongs.ui.other.EasySegmentedListItem
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddToPlaylistPopup(path: String, onDismissRequired: () -> Unit) {
    ModalBottomSheet(
        { onDismissRequired() },
        dragHandle = {
            Text(
                "Add to playlist",
                maxLines = 1,
                fontSize = 24.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth().padding(10.dp)
            )
        },
        content = {
            LazyColumn(contentPadding = PaddingValues(15.dp)) {
                val count = Settings.playlists.count()
                items(count) { i ->
                    val playlistItem by remember { mutableStateOf("Music -> $path") }
                    val pl = remember { File(Settings.playlists[i]).readLines().toMutableStateList() }
                    var isIn by remember { mutableStateOf(pl.contains(playlistItem)) }
                    EasySegmentedListItem(
                        if (isIn) playlist_add_check_circle else playlist_add,
                        if (pl[0].startsWith("Name -> ")) pl[0].removePrefix("Name -> ")
                        else "??? (Corrupted name)",
                        i, count,
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

                item { Spacer(Modifier.height(150.dp)) }
            }
        }
    )
}