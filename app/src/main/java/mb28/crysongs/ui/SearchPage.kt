package mb28.crysongs

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.fastForEachIndexed
import mb28.crysongs.core.Settings
import mb28.crysongs.core.Track
import mb28.crysongs.ui.other.TrackTile

private var searchPageSearchText by mutableStateOf("")
private var searchResult = mutableStateListOf<Track>()

fun searchPageResearch(input: String) {
    searchPageSearchText = input
    searchResult.clear()
    if (input == "#pl:fav") {
        tracks.fastForEachIndexed { i, track ->
            if (Settings.favorites.contains(track.path)) {
                searchResult.add(track)
            }
        }
    }
    else if (input.isNotBlank()) {
        tracks.fastForEachIndexed { i, track ->
            if (track.title.lowercase().contains(input.lowercase())
                || track.artist.lowercase().contains(input.lowercase())) {
                searchResult.add(track)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun SearchPage() {
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
                searchPageSearchText,
                {
                    searchPageResearch(it)
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