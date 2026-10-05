package mb28.crysongs.ui.more_pages

import android.app.Activity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ContainedLoadingIndicator
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import mb28.crysongs.core.Track
import mb28.crysongs.core.pageAnimation
import mb28.crysongs.playerQuery
import mb28.crysongs.ui.other.TrackTile
import mb28.crysongs.updateDisplayQuery
import kotlin.time.Duration.Companion.milliseconds

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun SearchPage(activity: Activity) {
    val state = remember { MutableTransitionState(false).apply { targetState = true } }
    var searchPageSearchText by remember { mutableStateOf("") }
    var searchResult = remember { mutableStateListOf<String>() }
    var researching by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope { Dispatchers.IO }

    fun research(input: String) {
        scope.launch {
            researching = true
            delay(50.milliseconds)
            if (searchPageSearchText.isNotBlank()) {
                searchResult = Track.search(input, activity).toMutableStateList()
            }
            else searchResult.clear()
            researching = false
        }
    }

    AnimatedVisibility(
        visibleState = state,
        enter = pageAnimation,
    ) {
        LazyColumn(
            contentPadding = PaddingValues(top = 120.dp, bottom = 260.dp),
        ) {
            item {
                Text(
                    "Search (${searchResult.count()})",
                    fontSize = 36.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(50.dp))
            }

            item {
                OutlinedTextField(
                    searchPageSearchText,
                    {
                        searchPageSearchText = it
                        research(searchPageSearchText)
                    },
                    label = {
                        Text("Search title, artist or album (case insensitive)")
                    },
                    keyboardOptions = KeyboardOptions(
                        showKeyboardOnFocus = true
                    ),
                    shape = RoundedCornerShape(35.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 15.dp, vertical = 5.dp)
                )
            }

            if (!researching) {
                val count = searchResult.count()
                items(count) { i ->
                    TrackTile(searchResult[i], i, count, false) {
                        playerQuery = searchResult.toMutableStateList()
                        updateDisplayQuery()
                    }
                }
            } else {
                item {
                    Box(Modifier.fillMaxSize(), Alignment.Center) {
                        ContainedLoadingIndicator()
                    }
                }
            }

        }
    }
}