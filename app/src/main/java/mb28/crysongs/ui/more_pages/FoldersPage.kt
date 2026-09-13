package mb28.crysongs.ui.more_pages

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
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
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import mb28.crysongs.core.pageAnimation
import mb28.crysongs.folders
import mb28.crysongs.icons.arrow_back
import mb28.crysongs.icons.folder
import mb28.crysongs.icons.shuffle
import mb28.crysongs.playerQuery
import mb28.crysongs.setAndPlay
import mb28.crysongs.tracks
import mb28.crysongs.ui.other.EasySegmentedListItem
import mb28.crysongs.ui.other.TrackTile
import mb28.crysongs.updateDisplayQuery

@Composable
fun FoldersPage() {
    var folderView by remember { mutableStateOf(false) }
    var clickedFolderPath by remember { mutableStateOf("") }
    val state = remember {
        MutableTransitionState(false).apply {
            targetState = true
        }
    }

    AnimatedVisibility(
        visibleState = state,
        enter = pageAnimation,
    ) {
        LazyColumn(
            contentPadding = PaddingValues(top = 130.dp, bottom = 200.dp),
        ) {
            item {
                Text(
                    if (folderView) clickedFolderPath.substring(
                        clickedFolderPath.lastIndexOf('/') + 1
                    )
                    else "Folders (${folders.count()})",
                    fontSize = 36.sp,
                    textAlign = TextAlign.Center,
                    lineHeight = 40.sp,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(33.dp))
            }

            item {
                Row(
                    Modifier.fillMaxWidth()
                        .padding(top = 40.dp, bottom = 10.dp, start = 10.dp, end = 10.dp),
                    horizontalArrangement = Arrangement.Start
                ) {
                    if (folderView) {
                        FilledTonalIconButton(
                            { folderView = false }
                        ) {
                            Icon(arrow_back, null)
                        }
                        FilledTonalIconButton(
                            {
                                val folderTracks = tracks.toMutableList()
                                folderTracks.removeIf {
                                    !it.path.startsWith(clickedFolderPath)
                                }
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

            if (folderView) {
                val folderTracks = tracks.toMutableList()
                folderTracks.removeIf {
                    !it.path.substring(0, it.path.lastIndexOf('/'))
                        .endsWith(clickedFolderPath)
                }
                val count = folderTracks.count()
                items(count) { i ->
                    TrackTile(
                        folderTracks[i],
                        i, count,
                        resetQueryOnClick = false
                    ) {
                        playerQuery = folderTracks.toMutableStateList()
                        updateDisplayQuery()
                    }
                }
            } else {
                val count = folders.count()
                items(count) { i ->
                    val f = folders[i]
                    EasySegmentedListItem(
                        folder,
                        f.substring(f.lastIndexOf('/') + 1),
                        i, count,
                        Modifier.padding(horizontal = 10.dp)
                    ) {
                        clickedFolderPath = f
                        folderView = true
                    }
                }
            }
        }
    }
}