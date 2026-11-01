package mb28.crysongs.ui.other

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.ShortNavigationBarItem
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import mb28.crysongs.core.Settings
import mb28.crysongs.icons.album
import mb28.crysongs.icons.artist
import mb28.crysongs.icons.cottage
import mb28.crysongs.icons.folder
import mb28.crysongs.icons.fps30_select
import mb28.crysongs.icons.library_music
import mb28.crysongs.icons.music_note_2
import mb28.crysongs.icons.queue_music
import mb28.crysongs.icons.search
import mb28.crysongs.icons.theater_comedy

@Composable
fun NavTab(navIndex: Int, selected: Boolean, onClick: (Int) -> Unit) {
    val i = Settings.navTabs[navIndex]

    ShortNavigationBarItem(
        selected,
        { onClick(i) },
        icon = {
            Icon(getCurrentNavItemIcon(i), null)
        },
        label = {
            Text(getCurrentNavItemName(i))
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
                items(9) {
                    EasySegmentedListItem(
                       null,
                       getCurrentNavItemName(it),
                       it, 9,
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

fun getCurrentNavItemName(i: Int) : String =
    when(i) {
        0 -> "Tracks"
        1 -> "Query"
        2 -> "Playlists"
        3 -> "Folders"
        4 -> "Home"
        5 -> "Artists"
        6 -> "Albums"
        7 -> "Genres"
        8 -> "Bitrates"
        9 -> "Search"
        else -> throw Exception()
    }

fun getCurrentNavItemIcon(i: Int) : ImageVector =
    when(i) {
        0 -> music_note_2
        1 -> queue_music
        2 -> library_music
        3 -> folder
        4 -> cottage
        5 -> artist
        6 -> album
        7 -> theater_comedy
        8 -> fps30_select
        9 -> search
        else -> throw Exception()
    }
