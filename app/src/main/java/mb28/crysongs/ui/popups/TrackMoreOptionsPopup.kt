package mb28.crysongs.ui.popups

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import mb28.crysongs.core.Settings
import mb28.crysongs.core.Track
import mb28.crysongs.icons.favorite
import mb28.crysongs.icons.heart_plus
import mb28.crysongs.icons.playlist_add
import mb28.crysongs.icons.queue_music
import mb28.crysongs.nowPlayingI
import mb28.crysongs.playerQuery
import mb28.crysongs.setAndPlay
import mb28.crysongs.ui.other.EasySegmentedListItem
import mb28.crysongs.updateDisplayQuery

@Composable
fun TrackMoreOptionsPopup(track: Track, onDismissRequired: () -> Unit) {
    var showAddToPL by remember { mutableStateOf(false) }
    AlertDialog(
        { onDismissRequired() },
        { },
        title = {
            Text(track.title, maxLines = 1)
        },
        text = {
            LazyColumn {
                val count = 3
                item {
                    val contains = Settings.favorites.contains(track.path)
                    EasySegmentedListItem(
                        if (contains) favorite else heart_plus,
                        if (contains) "Remove from favorites" else "Add to favorites",
                        0, count,
                    ) {
                        Settings.addOrRemoveFavorite(track.path)
                    }
                    EasySegmentedListItem(
                        playlist_add,
                        "Add to playlist",
                        1, count,
                    ) {
                        showAddToPL = true
                    }
                    EasySegmentedListItem(
                        queue_music,
                        "Play next",
                        2, count,
                    ) {
                        if (playerQuery.isNotEmpty()) {
                            playerQuery.add(nowPlayingI + 1, track)
                        } else {
                            playerQuery.add(track)
                            setAndPlay(track, false)
                        }
                        updateDisplayQuery()
                        onDismissRequired()
                    }
                }
            }
        }
    )

    if (showAddToPL) {
        AddToPlaylistPopup(track) {
            showAddToPL = false
        }
    }
}