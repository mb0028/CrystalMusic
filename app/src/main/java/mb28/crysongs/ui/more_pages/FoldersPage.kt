package mb28.crysongs.ui.more_pages

import android.annotation.SuppressLint
import android.os.Environment
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.ToggleButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import mb28.crysongs.core.Settings
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
import java.io.File


@SuppressLint("SdCardPath")

private val external = Environment.getExternalStorageDirectory().path
private val tree = mutableStateListOf(external)
private val uselessDirs = listOf("Android", "DCIM", "Pictures", "Movies", ".trash-storage")
private val moreUselessDirs = listOf("Podcasts", "Ringtones", "Alarms", "Notifications", "Audiobooks")

private fun isAudioFile(path: String) : Boolean {
    return path.endsWith(".mp3") || path.endsWith(".m4a") ||
        path.endsWith(".flac") || path.endsWith(".wav") || path.endsWith(".ogg")
}

@SuppressLint("SdCardPath")
@Composable
fun FoldersPage() {
    var folderView by remember { mutableStateOf(false) }
    var clickedFolderPath by remember { mutableStateOf("") }
    val state = remember { MutableTransitionState(false).apply { targetState = true } }
    val lastTrunk = tree.last()

    if (folderView) {
        BackHandler { folderView = false }
    } else if (tree.count() > 1) {
        BackHandler { tree.remove(lastTrunk) }
    }

    AnimatedVisibility(
        visibleState = state,
        enter = pageAnimation,
    ) {
        LazyColumn(
            contentPadding = PaddingValues(top = 130.dp, bottom = 200.dp),
        ) {
            // Header
            item {
                Text(
                    if (folderView) clickedFolderPath.substring(
                        clickedFolderPath.lastIndexOf('/') + 1)
                        else "Folders (${folders.count()})",
                    fontSize = 36.sp,
                    textAlign = TextAlign.Center,
                    lineHeight = 40.sp,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(33.dp))
            }

            // Buttons row
            item {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(top = 40.dp, bottom = 10.dp, start = 10.dp, end = 10.dp),
                    horizontalArrangement = Arrangement.Start
                ) {
                    if (folderView) {
                        FilledTonalIconButton(
                            { folderView = false }
                        ) { Icon(arrow_back, null) }
                        FilledTonalIconButton(
                            { //TODO: Reduce ram usage
                                val folderTracks = tracks.toMutableList()
                                folderTracks.removeIf {
                                    !it.startsWith(clickedFolderPath)
                                }
                                playerQuery = folderTracks.shuffled().toMutableStateList()
                                updateDisplayQuery()
                                setAndPlay(playerQuery.first(), false)
                            }
                        ) {
                            Icon(shuffle, null)
                        }
                    } else {
                        ToggleButton(
                            Settings.hierarchyView,
                            { Settings.hierarchyView = it; Settings.save() }
                        ) {
                            Text("Hierarchy")
                        }
                        if (lastTrunk == external && Settings.hierarchyView) {
                            Spacer(Modifier.width(5.dp))
                            ToggleButton(
                                Settings.hideSystemSounds,
                                { Settings.hideSystemSounds = it; Settings.save() }
                            ) {
                                Text("Compact")
                            }
                        }
                        if (Settings.hierarchyView && tree.count() > 1) {
                            Spacer(Modifier.width(5.dp))
                            FilledTonalIconButton(
                                { tree.remove(lastTrunk) }
                            ) {
                                Icon(arrow_back, null)
                            }
                        }
                    }
                }
            }

            when {
                Settings.hierarchyView -> {
                    val files = File(lastTrunk).listFiles()?.toList()?.sortedBy { it.isFile } ?: listOf<File>()
                    val count = files.count()

                    item {
                        Text(
                            lastTrunk.replaceFirst(external, "Storage").replace("/", " > "),
                            modifier = Modifier
                                .padding(10.dp, 5.dp)
                                .horizontalScroll(rememberScrollState())
                        )
                    }

                    items(count) {
                        val file = files[it]
                        val fPath = file.path
                        val isUseless = uselessDirs.contains(fPath.removePrefix("$external/"))
                        val isUseless2 = Settings.hideSystemSounds && moreUselessDirs.contains(fPath.removePrefix("$external/"))
                        if (file.isDirectory && !isUseless && !isUseless2) {
                            EasySegmentedListItem(
                                if (Settings.hideSystemSounds) null else folder,
                                fPath.substring(fPath.lastIndexOf('/') + 1),
                                it, count,
                                Modifier.padding(horizontal = 10.dp)
                            ) { tree.add(fPath) }
                        }
                        else if (isAudioFile(fPath)) {
                            TrackTile(
                                fPath,
                                it, count,
                                resetQueryOnClick = false
                            ) {
                                val q = files.map { f -> f.path }.toMutableList()
                                q.removeIf { !isAudioFile(it) }
                                playerQuery = q.toMutableStateList()
                                updateDisplayQuery()
                            }
                        }
                    }
                }

                folderView -> {
                    val folderTracks = tracks.toMutableList()
                    folderTracks.removeIf {
                        !it.substring(0, it.lastIndexOf('/'))
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
                }

                else -> {
                    val count = folders.count()
                    items(count) { i ->
                        val f = folders.elementAt(i)
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
}