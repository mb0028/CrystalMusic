package mb28.crysongs.ui.more_pages

import android.app.Activity
import android.provider.MediaStore
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
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import mb28.crysongs.albums
import mb28.crysongs.artists
import mb28.crysongs.bitrates
import mb28.crysongs.core.Track
import mb28.crysongs.core.pageAnimation
import mb28.crysongs.genres
import mb28.crysongs.icons.arrow_back
import mb28.crysongs.icons.shuffle
import mb28.crysongs.playerQuery
import mb28.crysongs.setAndPlay
import mb28.crysongs.ui.other.EasySegmentedListItem
import mb28.crysongs.ui.other.TrackTile
import mb28.crysongs.updateDisplayQuery

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun CustomTagsPage(listType: String, activity: Activity) {
    var customTabItems = remember { mutableStateSetOf<String>() }
    val customTabOpenedItems = remember { mutableStateListOf<String>() }
    var customTabItemOpened by remember { mutableStateOf(false) }
    var lastClickedCustomTabItem by remember { mutableStateOf("") }

    var loading by remember { mutableStateOf(false) }
    val state = remember { MutableTransitionState(false).apply { targetState = true } }

    fun getList() : MutableList<String> {
        return Track.getByAtt(
            lastClickedCustomTabItem,
            when(listType) {
                "artists" -> MediaStore.Audio.Media.ARTIST
                "albums" -> MediaStore.Audio.Media.ALBUM
                "genres" -> MediaStore.Audio.Media.GENRE
                else -> MediaStore.Audio.Media.BITRATE
            },
            activity
        )
    }

    if (customTabItemOpened) {
        BackHandler { customTabItemOpened = false }
    } else {
        when(listType) {
            "artists" -> customTabItems = artists
            "albums" -> customTabItems = albums
            "genres" -> customTabItems = genres
            else -> customTabItems.addAll(bitrates.map { it.toString() })
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
                    if (customTabItemOpened) (if (listType == "bitrates") (lastClickedCustomTabItem.toInt() / 1000).toString() else lastClickedCustomTabItem)
                    else "${listType[0].uppercase() +
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
                                val folderTracks = getList()
                                folderTracks.shuffle()
                                playerQuery = folderTracks.toMutableStateList()
                                updateDisplayQuery()
                                setAndPlay(playerQuery.first(), false)
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
                            if (listType == "bitrates") (f.toInt() / 1000).toString() else f,
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