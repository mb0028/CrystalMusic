package mb28.crysongs.ui.other

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import mb28.crysongs.core.Settings
import mb28.crysongs.icons.album
import mb28.crysongs.icons.artist
import mb28.crysongs.icons.folder
import mb28.crysongs.icons.fps30_select
import mb28.crysongs.icons.library_music
import mb28.crysongs.icons.list_2
import mb28.crysongs.icons.music_note_2
import mb28.crysongs.icons.queue_music
import mb28.crysongs.icons.search
import mb28.crysongs.icons.stylus_brush
import mb28.crysongs.icons.theater_comedy

@Composable
fun RowScope.NavTab(navIndex: Int, selected: Boolean, onClick: (Int) -> Unit) {
    val i = Settings.navTabs[navIndex]

    NavigationBarItem(
        selected,
        { onClick(i) },
        icon = {
            Icon(
                when(i) {
                    0 -> music_note_2
                    1 -> queue_music
                    2 -> library_music
                    3 -> folder
                    4 -> search
                    5 -> artist
                    6 -> album
                    7 -> theater_comedy
                    8 -> fps30_select
                    9 -> list_2
                    else -> throw Exception()
                },
                null
            )
        },
        label = {
            Text(getName(i))
        }
    )
}

@Composable
fun EditNavigationItemPopup(navIndex: Int, onDismissRequired: () -> Unit) {
    AlertDialog(
        { onDismissRequired() },
        { },
        title = {
            Text("Choose tab #${navIndex + 1}")
        },
        text = {
            LazyColumn {
                item {
                    ListItem(
                        colors = ListItemDefaults.colors(
                            Color.Transparent
                        ),
                        trailingContent = {
                            Switch(
                                Settings.initTab == navIndex,
                                {
                                    if (Settings.initTab == navIndex) {
                                        Settings.initTab = 0
                                    } else {
                                        Settings.initTab = navIndex
                                    }
                                    Settings.save()
                                    onDismissRequired()
                                }
                            )
                        }
                    ) { Text("Set default tab") }
                }
                items(10) {
                    EasySegmentedListItem(
                       null,
                       getName(it),
                       it, 10,
                       Modifier.padding(horizontal = 15.dp)
                    ) {
                       Settings.navTabs[navIndex] = it
                       Settings.save()
                       onDismissRequired()
                    }
                }
            }
        }
    )
}

private fun getName(i: Int) : String =
    when(i) {
        0 -> "Tracks"
        1 -> "Query"
        2 -> "Playlists"
        3 -> "Folders"
        4 -> "Search"
        5 -> "Artists"
        6 -> "Albums"
        7 -> "Genres"
        8 -> "Bitrates"
        9 -> "Other"
        else -> throw Exception()
    }
