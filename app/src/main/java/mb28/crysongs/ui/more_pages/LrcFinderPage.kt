package mb28.crysongs.ui.more_pages

import android.content.ClipData
import android.content.Context
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ContainedLoadingIndicator
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ClipEntry
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import mb28.crysongs.core.openLink
import mb28.crysongs.core.pageAnimation
import mb28.crysongs.icons.search
import mb28.crysongs.ui.other.ShuffleButton
import mb28.music.LRCLIB

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun LrcFinderPage(context: Context) {
    val state = remember { MutableTransitionState(false).apply { targetState = true } }
    val scope = rememberCoroutineScope { Dispatchers.IO }
    val clipboard = LocalClipboard.current

    var searching by remember { mutableStateOf(false) }
    var searchQ by remember { mutableStateOf("") }
    val searchResult = remember { mutableStateListOf<LRCLIB.LyricsRecord>() }

    AnimatedVisibility(
        visibleState = state,
        enter = pageAnimation,
    ) {
        LazyColumn(
            contentPadding = PaddingValues(top = 120.dp, bottom = 260.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Text(
                    "LRC Finder (${searchResult.count()})",
                    fontSize = 36.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(50.dp))
            }

            item {
                OutlinedTextField(
                    searchQ,
                    { searchQ = it },
                    label = {
                        Text("lrclib.net: Search for lyrics...")
                    },
                    keyboardOptions = KeyboardOptions(
                        showKeyboardOnFocus = true
                    ),
                    trailingIcon = {
                        FilledIconButton(
                            {
                                scope.launch {
                                    searching = true
                                    searchResult.clear()
                                    val result = LRCLIB.search(searchQ)
                                    if (result != null) {
                                        searchResult.addAll(result)
                                    }
                                    searching = false
                                }
                            }
                        ) { Icon(search, null) }
                    },
                    shape = RoundedCornerShape(35.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 15.dp, vertical = 5.dp)
                )
            }

            item {
                Row(
                    Modifier.fillMaxWidth().padding(top = 5.dp, start = 15.dp, end = 10.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    FilledTonalButton(
                        { openLink(context, "https://github.com/tranxuanthang/") }
                    ) {
                        Text("lrclib dev")
                    }
                    FilledTonalButton(
                        { openLink(context, "https://lrclib.net/") }
                    ) {
                        Text("Open lrclib.net")
                    }
                }
                Row(
                    Modifier.fillMaxWidth().padding(top = 5.dp, bottom = 10.dp, start = 15.dp, end = 10.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        { openLink(context, "https://www.buymeacoffee.com/thangtran") }
                    ) {
                        Text("Support lrclib dev (buymeacoffee)")
                    }
                }
            }

            if (!searching) {
                val count = searchResult.count()
                items(count) { i ->
                    val lyricsRecord = searchResult[i]
                    Card(
                        Modifier.fillMaxWidth().padding(35.dp, 10.dp),
                        colors = CardDefaults.cardColors(
                            MaterialTheme.colorScheme.secondaryContainer,
                            MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    ) {
                        LazyColumn(
                            Modifier.height(120.dp).padding(8.dp),
                        ) {
                            item {
                                Text(
                                    lyricsRecord.toString(),
                                )
                            }
                        }
                        Spacer(Modifier.height(5.dp))
                        Row(
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Button(
                                {
                                    scope.launch {
                                        clipboard.setClipEntry(ClipEntry(ClipData.newPlainText(
                                            lyricsRecord.trackName, lyricsRecord.syncedLyrics)))
                                    }
                                }
                            ) {
                                Text("Copy synced")
                            }
                            Button(
                                {
                                    scope.launch {
                                        clipboard.setClipEntry(ClipEntry(ClipData.newPlainText(
                                            lyricsRecord.trackName, lyricsRecord.plainLyrics)))
                                    }
                                }
                            ) {
                                Text("Copy plain")
                            }
                        }
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