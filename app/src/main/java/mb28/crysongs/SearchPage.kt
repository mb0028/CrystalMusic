package mb28.crysongs

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ContainedLoadingIndicator
import androidx.compose.material3.ExpandedDockedSearchBar
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberSearchBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.toLowerCase
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.fastForEachIndexed
import mb28.crysongs.core.Track
import mb28.crysongs.ui.other.ShuffleButton
import mb28.crysongs.ui.other.TrackTile
import java.util.Locale
import java.util.Locale.getDefault

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun SearchPage(modifier: Modifier = Modifier) {
    val state = rememberSearchBarState()
    val searchResult = rememberSaveable { mutableStateListOf<Track>() }
    var search by remember { mutableStateOf("") }
    LazyColumn(
        contentPadding = PaddingValues(top = 120.dp, bottom = 200.dp),
    ) {
        item {
            Text(
                "Search (${searchResult.count()})", fontSize = 36.sp, textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(50.dp))
        }

        item {
            OutlinedTextField(
                search,
                {
                    search = it
                    searchResult.clear()
                    if (it.isNotBlank()) {
                        tracks.fastForEachIndexed { i, track ->
                            if (track.title.lowercase().contains(it.lowercase())
                                || track.artist.lowercase().contains(it.lowercase())) {
                                searchResult.add(track)
                            }
                        }
                    }
                },
                label = {
                    Text("Search title or artist (case insensitive)")
                },
                keyboardOptions = KeyboardOptions(
                    showKeyboardOnFocus = true
                ),
                shape = RoundedCornerShape(35.dp),
                modifier = Modifier.fillMaxWidth().padding(horizontal = 15.dp, vertical = 5.dp)
            )
        }

        val count = searchResult.count()
        items(count) { i ->
            TrackTile(searchResult[i], i, count, false) {
                playerQuery = searchResult.toMutableStateList()
                updateDisplayQuery()
            }
        }
    }
}