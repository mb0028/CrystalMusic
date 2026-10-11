package mb28.crysongs

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ContainedLoadingIndicator
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedListItem
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import mb28.crysongs.core.Settings.tagsSpacer
import mb28.crysongs.core.Track
import mb28.crysongs.core.formatDurationMs
import mb28.crysongs.icons.arrow_back
import mb28.crysongs.ui.theme.CrySongsTheme
import kotlin.math.roundToInt
import kotlin.time.Duration.Companion.milliseconds

class SoundPickerActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
//        window.isNavigationBarContrastEnforced = false
        super.onCreate(savedInstanceState)

        setContent {
            CrySongsTheme {
                Scaffold(
                    Modifier.fillMaxSize(),
                    topBar = {
                        TopAppBar(
                            { Text(intent.getStringExtra(Intent.EXTRA_TITLE) ?: "Choose File") },
                            navigationIcon = {
                                IconButton(
                                    {
                                        setResult(RESULT_CANCELED)
                                        finish()
                                    },
                                    colors = IconButtonDefaults.iconButtonColors().copy(
                                        MaterialTheme.colorScheme.surfaceContainerHigh
                                    ),
                                    modifier = Modifier.padding(horizontal = 15.dp)
                                ) { Icon(arrow_back, null) }
                            },
                        )
                    }
                ) { paddingValues ->
                    Picker(paddingValues, this)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun Picker(padding: PaddingValues, activity: ComponentActivity) {
    var searchText by remember { mutableStateOf("") }
    val searchResult = remember { mutableStateListOf<String>() }
    var researching by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope { Dispatchers.IO }

    LaunchedEffect(Unit) {
        researching = true
        refreshTracksList(activity)
        searchResult.clear()
        searchResult.addAll(tracks.toMutableStateList())
        researching = false
    }

    LazyColumn(contentPadding = padding) {
        item {
            OutlinedTextField(
                searchText,
                {
                    searchText = it
                    if (!researching && !isReloading) {
                        scope.launch {
                            researching = true
                            searchResult.clear()
                            delay(40.milliseconds)
                            searchResult.addAll(if (searchText.isNotBlank()) {
                                Track.search(searchText, activity)
                            } else tracks)
                            researching = false
                        }
                    }
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

        val count = searchResult.count()
        if (count != 0 && !researching) {
            items(count) { i ->
                val path = searchResult[i]
                val state = remember { MutableTransitionState(false).apply { targetState = true }}
                var t: Track? by remember { mutableStateOf(null) }
                var cover: ImageBitmap? by remember { mutableStateOf(null) }

                LaunchedEffect(Unit) {
                    t = Track.getTags(path, activity)
                    withContext(Dispatchers.IO) {
                        val coverPath = Track.createOrGetThumbnail(path)
                        if (coverPath != null)
                            cover = BitmapFactory.decodeFile(coverPath).asImageBitmap()
                    }
                }

                AnimatedVisibility(
                    visibleState = state,
                    enter = scaleIn(initialScale = 0.75f) + fadeIn(initialAlpha = 0.1f),
                ) {
                    SegmentedListItem(
                        verticalAlignment = Alignment.CenterVertically,
                        shapes = ListItemDefaults.segmentedShapes(i, count),
                        colors = ListItemDefaults.segmentedColors(
                            MaterialTheme.colorScheme.surfaceContainerLowest,
                            MaterialTheme.colorScheme.onSurface,
                        ),
                        modifier = Modifier
                            .padding(bottom = 3.dp, start = 5.dp, end = 5.dp)
                            .height(80.dp),
                        onClick = {
                            activity.setResult(
                                RESULT_OK,
                                Intent().setData(Track.getUri(path, activity))
                                    .putExtra(EXTRA_PATH, path)
                            )
                            activity.finish()
                        },
                        contentPadding = PaddingValues(5.dp),
                        leadingContent = {
                            Box(Modifier.clip(RoundedCornerShape(10.dp))) {
                                Image(
                                    cover ?: noCoverBitmap!!,
                                    "Track cover",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .aspectRatio(1f)
                                        .clip(RoundedCornerShape(10.dp))
                                )
                            }
                        },
                    ) {
                        Column(
                            Modifier.fillMaxHeight(),
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                t?.title ?: "Loading...",
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                fontSize = 16.sp
                            )
                            Text(
                                t?.artist ?: "🕒",
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                fontSize = 13.sp,
                                modifier = Modifier.fillMaxWidth()
                            )

                            val dura = formatDurationMs(t?.duration?.milliseconds ?: 1.milliseconds)
                            val bitrate = ((t?.bitrate ?: 1) / 1000f).roundToInt()
                            Text(
                                "$dura${tagsSpacer}${bitrate} kbps${tagsSpacer}${t?.year}${tagsSpacer}${t?.genre}" +
                                        if (Track.hasLRC(path)) "${tagsSpacer}LRC" else "",
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                fontSize = 13.sp,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }
        }
        else {
            item {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    ContainedLoadingIndicator()
                }
            }
        }
    }
}
