package mb28.crysongs.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import mb28.crysongs.core.Settings
import mb28.crysongs.icons.favorite
import mb28.crysongs.icons.library_music
import mb28.crysongs.searchPageResearch
import mb28.crysongs.ui.other.EasySegmentedListItem

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun PlaylistsPage(selectedIndex: MutableIntState) {
    val state = rememberLazyListState()
    LazyColumn(
        contentPadding = PaddingValues(top = 130.dp, bottom = 200.dp),
        state = state
    ) {
        item {
            Text(
                "Playlists", fontSize = 36.sp, textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(33.dp))
        }

        item {
            Row(
                Modifier.fillMaxWidth().padding(top = 40.dp, bottom = 10.dp, start = 10.dp, end = 10.dp),
                horizontalArrangement = Arrangement.Start
            ) {
                FilledTonalIconButton(
                    { }
                ) {
                    Icon(library_music, null)
                }
                FilledTonalIconButton(
                    { }
                ) {
                    Icon(library_music, null)
                }
            }
        }

        item {
            EasySegmentedListItem(
                favorite,
                "Favorites (${Settings.favorites.count()})",
                0, 2,
                Modifier.padding(horizontal = 15.dp)
            ) {
                searchPageResearch("#pl:fav")
                selectedIndex.intValue = 4
            }
        }

    }
}
