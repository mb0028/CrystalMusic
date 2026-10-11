package mb28.crysongs.ui.popups

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import mb28.crysongs.EXTRA_PATH
import mb28.crysongs.TagEditorActivity
import mb28.crysongs.core.Settings
import mb28.crysongs.icons.edit
import mb28.crysongs.icons.playlist_add
import mb28.crysongs.icons.queue_music
import mb28.crysongs.nowPlaying
import mb28.crysongs.nowPlayingI
import mb28.crysongs.playerQuery
import mb28.crysongs.setAndPlay
import mb28.crysongs.ui.other.EasySegmentedListItem
import mb28.crysongs.updateDisplayQuery

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrackMoreOptionsPopup(path: String, title: String? = null) {
    val context = LocalContext.current
    var showAddToPL by remember { mutableStateOf(false) }
    val count = remember { 3 }

    Card(
        Modifier.padding(horizontal = 15.dp)
            .alpha(if (Settings.enableBlur) 0.85f else 1f),
        shape = RoundedCornerShape(30.dp)
    ) {
        Text(
            title ?: "???",
            maxLines = 2,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth().padding(10.dp, 10.dp, 10.dp, 0.dp)
        )
        Column(Modifier.padding(15.dp).clip(RoundedCornerShape(28.dp))) {
            EasySegmentedListItem(
                edit,
                "Edit tags",
                0, count,
            ) {
                val intent = Intent(context, TagEditorActivity::class.java)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    .putExtra(EXTRA_PATH, path)
                context.startActivity(intent)
            }
            EasySegmentedListItem(
                playlist_add,
                "Add to playlist",
                1, count,
            ) {
                showAddToPL = true
            }
//            val contains = Settings.favorites.contains(path)
//            EasySegmentedListItem(
//                if (contains) favorite else heart_plus,
//                if (contains) "Remove from favorites" else "Add to favorites",
//                2, count,
//            ) {
//                Settings.addOrRemoveFavorite(path)
//            }
            EasySegmentedListItem(
                queue_music,
                "Play next",
                count - 1, 4,
            ) {
                when {
                    playerQuery.isEmpty() -> {
                        playerQuery.add(path)
                        setAndPlay(path, false)
                    }
                    nowPlaying == path -> {
                        Toast.makeText(context, "Already playing!", Toast.LENGTH_SHORT).show()
                    }
                    playerQuery.contains(path) -> {
                        playerQuery.run {
                            add(nowPlayingI + 1, removeAt(indexOf(path)))
                        }
                    }
                    else -> {
                        playerQuery.add(nowPlayingI + 1, path)
                    }
                }
                updateDisplayQuery()
            }
        }
    }

    if (showAddToPL) {
        AddToPlaylistPopup(path) {
            showAddToPL = false
        }
    }
}