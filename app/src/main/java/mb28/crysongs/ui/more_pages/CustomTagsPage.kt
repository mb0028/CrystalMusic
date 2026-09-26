package mb28.crysongs.ui.more_pages

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ContainedLoadingIndicator
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.mutableStateSetOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.fastForEach
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import mb28.crysongs.core.Track
import mb28.crysongs.core.pageAnimation
import mb28.crysongs.icons.arrow_back
import mb28.crysongs.icons.shuffle
import mb28.crysongs.playerQuery
import mb28.crysongs.setAndPlay
import mb28.crysongs.tracks
import mb28.crysongs.ui.other.EasySegmentedListItem
import mb28.crysongs.ui.other.TrackTile
import mb28.crysongs.updateDisplayQuery

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun CustomTagsPage(listType: String, activity: Activity) {
    val customTabItems = remember { mutableStateSetOf<String>() }
    val customTabOpenedItems = remember { mutableStateListOf<String>() }
    var customTabItemOpened by remember { mutableStateOf(false) }
    var lastClickedCustomTabItem by remember { mutableStateOf("") }

    var loading by remember { mutableStateOf(true) }
    val state = remember { MutableTransitionState(false).apply { targetState = true } }
    val scope = rememberCoroutineScope { Dispatchers.IO }

    suspend fun getList() : MutableList<String> {
        val t = mutableListOf<String>()
        tracks.fastForEach { path ->
            val track = Track.getTags(path, activity)
            val add = when(listType) {
                "artists" -> track.artist == lastClickedCustomTabItem
                "albums" -> track.album == lastClickedCustomTabItem
                "genres" -> track.genre == lastClickedCustomTabItem
                else -> (track.bitrate / 1000).toString() == lastClickedCustomTabItem
            }
            if (add) t.add(path)
        }
        return t
    }

    LaunchedEffect(Unit) {
        tracks.fastForEach { path ->
            val track = Track.getTags(path, activity)
            when(listType) {
                "artists" -> customTabItems.add(track.artist)
                "albums" -> customTabItems.add(track.album)
                "genres" -> customTabItems.add(track.genre)
                else -> customTabItems.add((track.bitrate / 1000).toString())
            }
        }
        loading = false
    }

    if (customTabItemOpened) {
        BackHandler { customTabItemOpened = false }
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
                    if (customTabItemOpened) lastClickedCustomTabItem else "${listType[0].uppercase() +
                        listType.substring(1)} (${customTabItems.count()})",
                    fontSize = 36.sp,
                    textAlign = TextAlign.Center,
                    lineHeight = 40.sp,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(33.dp))
            }

            item {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(top = 40.dp, bottom = 10.dp, start = 10.dp, end = 10.dp),
                    horizontalArrangement = Arrangement.Start
                ) {
                    if (customTabItemOpened) {
                        FilledTonalIconButton(
                            { customTabItemOpened = false }
                        ) {
                            Icon(arrow_back, null)
                        }
                        FilledTonalIconButton(
                            {
                                scope.launch {
                                    val folderTracks = getList()
                                    folderTracks.shuffle()
                                    playerQuery = folderTracks.toMutableStateList()
                                    updateDisplayQuery()
                                    setAndPlay(playerQuery.first(), false)
                                }
                            }
                        ) {
                            Icon(shuffle, null)
                        }
                    }
                }
            }

            when {
                loading -> {
                    item {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            ContainedLoadingIndicator()
                        }
                    }
                }
                customTabItemOpened -> {
                    val count = customTabOpenedItems.count()
                    if (count == 0) {
                        item {
                            LaunchedEffect(Unit) {
                                val temp = getList()
                                customTabOpenedItems.clear()
                                customTabOpenedItems.addAll(temp)
                            }
                            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                ContainedLoadingIndicator()
                            }
                        }
                    } else {
                        items(count) { i ->
                            TrackTile(
                                customTabOpenedItems[i],
                                i, count,
                                resetQueryOnClick = false
                            ) {
                                playerQuery = customTabOpenedItems.toMutableStateList()
                                updateDisplayQuery()
                            }
                        }
                    }
                }
                else -> {
                    val count = customTabItems.count()
                    items(count) { i ->
                        val f = customTabItems.elementAt(i)
                        EasySegmentedListItem(
                            null,
                            f,
                            i, count,
                            Modifier.padding(horizontal = 10.dp)
                        ) {
                            customTabOpenedItems.clear()
                            lastClickedCustomTabItem = customTabItems.elementAt(i)
                            customTabItemOpened = true
                        }
                    }
                }
            }
        }
    }
}