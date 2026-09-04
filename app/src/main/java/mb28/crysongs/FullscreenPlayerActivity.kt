package mb28.crysongs

import android.app.Activity
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.transition.Explode
import android.transition.Slide
import android.transition.Transition
import android.view.RoundedCorner
import android.view.Window
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import androidx.core.graphics.drawable.toBitmap
import kotlinx.coroutines.launch
import mb28.crysongs.core.Settings
import mb28.crysongs.core.Track
import mb28.crysongs.core.formatDurationMs
import mb28.crysongs.core.inverseLerp
import mb28.crysongs.icons.arrow_cool_down
import mb28.crysongs.icons.list_2
import mb28.crysongs.icons.pause_circle
import mb28.crysongs.icons.play_circle
import mb28.crysongs.icons.repeat
import mb28.crysongs.icons.repeat_on
import mb28.crysongs.icons.skip_next
import mb28.crysongs.icons.skip_previous
import mb28.crysongs.icons.sound_detection_loud_sound
import mb28.crysongs.ui.theme.CrySongsTheme
import kotlin.math.roundToInt
import kotlin.time.Duration.Companion.milliseconds

const val EXTRA_SKIP_LOAD = "EXTRA_SKIP_LOAD"

class FullscreenPlayerActivity : ComponentActivity() {
    override fun finish() {
        super.finish()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            overrideActivityTransition(OVERRIDE_TRANSITION_CLOSE, 0,
                R.anim.slide_out)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        with(window) {
            requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS)
            allowEnterTransitionOverlap = true
            allowReturnTransitionOverlap = true
            enterTransition = Slide()
            exitTransition = Slide()
            isNavigationBarContrastEnforced = false
        }

        val skipLoad = intent.getBooleanExtra(EXTRA_SKIP_LOAD, false)
        super.onCreate(savedInstanceState)

        setContent {
            var activityOffset by remember { mutableIntStateOf(0) }
            CrySongsTheme {
                Scaffold(
                    Modifier.fillMaxSize()
                        .offset { IntOffset(0, activityOffset) }
                        .pointerInput(Unit) {
                            fun onRelease() {
                                if (activityOffset > 500) {
                                    finish()
                                } else {
                                    activityOffset = 0
                                }
                            }
                            detectVerticalDragGestures(
                                onDragEnd = { onRelease() },
                                onDragCancel = { onRelease() }
                            ) { _, dragAmount ->
                                activityOffset = (activityOffset + dragAmount.toInt()).coerceAtLeast(0)
                            }

                        },
                    containerColor = MaterialTheme.colorScheme.surfaceBright.copy(1f - (activityOffset / 1000f))
                ) { innerPadding ->
                    Pager(innerPadding, this)
                }
            }
        }
    }
}

@Composable
private fun Pager(innerPadding: PaddingValues, activity: Activity) {
    val roundness = activity.window.decorView.rootWindowInsets?.getRoundedCorner(
        RoundedCorner.POSITION_TOP_LEFT)?.radius ?: 0
    val selectedTab = rememberPagerState(1) { 3 }
    val fsPlayerCover = try {
            BitmapFactory.decodeFile(Track.createOrGetThumbnail(nowPlaying!!.path)).asImageBitmap()
        } catch (_: Exception) {
            activity.resources.getDrawable(R.drawable.null_track_cover).toBitmap().asImageBitmap()
        }

    Box(
        Modifier.clip(RoundedCornerShape((roundness / 3.25f).dp))
    ) {
        Image(
            fsPlayerCover, null,
            contentScale = ContentScale.FillHeight,
            modifier = Modifier
                .fillMaxSize()
                .blur(45.dp)
                .alpha(0.5f),
        )
        HorizontalPager(
            selectedTab
        ) { page ->
            when(page) {
                1 -> {
                   Column(
                       Modifier
                           .fillMaxSize()
                           .padding(innerPadding)
                           .padding(horizontal = 20.dp),
                       horizontalAlignment = Alignment.CenterHorizontally,
                       verticalArrangement = Arrangement.SpaceBetween
                   ) {
                       Spacer(Modifier.height(15.dp))

                       Column(Modifier.fillMaxWidth()) {
                           Cover(Modifier.align(Alignment.CenterHorizontally), fsPlayerCover)
                           Spacer(Modifier.height(15.dp))
                           Text(nowPlaying?.title ?: "", fontSize = 24.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
                           Spacer(Modifier.height(10.dp))
                           Text(nowPlaying?.artist ?: "", fontSize = 16.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
                           Text(nowPlaying?.album ?: "", fontSize = 14.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
                       }

                       Column(
                           Modifier.fillMaxWidth(),
                           horizontalAlignment = Alignment.CenterHorizontally
                       ) {
                           Row {
                               TextButton(
                                   { player.seekTo(position - 5000) }
                               ) {
                                   Text(formatDurationMs(position.milliseconds))
                               }
                               val pos = position.toFloat()
                               Slider(
                                   (pos / duration).takeIf { pos != 0f } ?: 0f,
                                   { player.seekTo((it * duration).roundToInt()) },
                                   modifier = Modifier
                                       .fillMaxWidth(0.75f)
                                       .padding(horizontal = 10.dp)
                               )
                               TextButton(
                                   { player.seekTo(position + 5000) }
                               ) {
                                   Text(formatDurationMs(duration.milliseconds))
                               }
                           }
                           Spacer(Modifier.height(15.dp))
                           PlayerButtonsRow()
                           Spacer(Modifier.height(15.dp))
                           Text(
                               lastLrcLine,
                               Modifier.height(45.dp),
                               textAlign = TextAlign.Center
                           )
                       }
                   }
                }
                2 -> {
                    LyricsTab(Modifier
                        .fillMaxSize()
                        .padding(horizontal = 20.dp))
                }
                0 -> {
                    TagsTab(Modifier
                        .fillMaxSize()
                        .padding(horizontal = 20.dp))
                }
            }
        }
        ChangePageRow(
            Modifier
                .padding(top = innerPadding.calculateTopPadding() + 10.dp)
                .align(Alignment.TopCenter),
            selectedTab, activity
        )
    }
}

@Composable
private fun ChangePageRow(modifier: Modifier = Modifier, selectedTab: PagerState, activity: Activity) {
    val tabs = listOf("Details", "Player", "Lyrics")
    Row(
        modifier.fillMaxWidth().padding(horizontal = 10.dp),
        Arrangement.SpaceBetween,
        Alignment.CenterVertically
    ) {
        IconButton(
            { activity.finish() }
        ) {
            Icon(arrow_cool_down, null)
        }

        Row(
            Modifier
                .size(270.dp, 40.dp)
                .background(
                    MaterialTheme.colorScheme.surfaceContainerHigh.copy(0.5f),
                    RoundedCornerShape(35.dp)
                ),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            tabs.forEachIndexed { i, tab ->
                Surface(
                    onClick = {selectedTab.requestScrollToPage(i)},
                    modifier = Modifier.size(80.dp, 30.dp),
                    color = if (selectedTab.currentPage == i) MaterialTheme.colorScheme.secondaryContainer
                    else MaterialTheme.colorScheme.surface.copy(0.5f),
                    shape = RoundedCornerShape(35.dp)
                ) {
                    Text(
                        tab,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 2.dp)
                    )
                }
            }
        }

        IconButton(
            { }
        ) {
            Icon(list_2, null)
        }
    }
}

@Composable
private fun Cover(modifier: Modifier = Modifier, cover: ImageBitmap) {
    Box(
        modifier.background(
            MaterialTheme.colorScheme.surfaceContainer,
            RoundedCornerShape(30.dp)
        )
    ) {
        Image(
            cover,
            "Track cover",
            contentScale = ContentScale.FillHeight,
            modifier = Modifier
                .size(340.dp, 340.dp)
                .clip(RoundedCornerShape(30.dp))
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PlayerButtonsRow() {
    var showVolSheet by rememberSaveable { mutableStateOf(false) }

    Row(
        Modifier
            .scale(1.4f)
            .padding(vertical = 10.dp)
    ) {
        IconButton(
            {
                showVolSheet = true
            },
            modifier = Modifier.scale(0.8f)
        ) {
            Icon(sound_detection_loud_sound, null)
        }
        IconButton(
            {
                setAndPlay(
                    playerQuery[(playerQuery.indexOf(nowPlaying) - 1).coerceIn(0, playerQuery.count() - 1)],
                    false
                )
            }
        ) {
            Icon(skip_previous, null)
        }
        IconButton(
            {
                if (player.isPlaying) {
                    player.pause()
                } else {
                    player.start()
                }
                isPlaying = player.isPlaying
            },
            modifier = Modifier
                .scale(1.5f)
                .padding(horizontal = 8.dp)
        ) {
            Icon(if (isPlaying) pause_circle else play_circle, null)
        }
        IconButton(
            {
                setAndPlay(
                    playerQuery[(playerQuery.indexOf(nowPlaying) + 1).coerceIn(0, playerQuery.count() - 1)],
                    false
                )
            }
        ) {
            Icon(skip_next, null)
        }
        IconButton(
            {
                Settings.loopTrack = !Settings.loopTrack
                player.isLooping = Settings.loopTrack
                Settings.save()
            },
            modifier = Modifier.scale(0.8f)
        ) {
            Icon(if (Settings.loopTrack) repeat_on else repeat, null)
        }
    }

    if (showVolSheet) {
        ModalBottomSheet(
            {
                showVolSheet = false
            }
        ) {
            Text("Volume: ${(Settings.appVolume * 100f).roundToInt() / 100f}", modifier = Modifier.padding(horizontal = 25.dp))
            Slider(
                Settings.appVolume,
                {
                    Settings.appVolume = it
                    player.setVolume(Settings.appVolume, Settings.appVolume)
                    Settings.save()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 15.dp)
            )
        }
    }
}

@Composable
private fun LyricsTab(modifier: Modifier = Modifier) {
    val state = rememberLazyListState()
    val scope = rememberCoroutineScope()
    if (lrcParser != null) {
        LazyColumn(
            modifier,
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(vertical = 200.dp),
            state = state
        ) {
            items(lrcParser!!.Count) { //TODO: Fix null pointer exc. on this line (i think its because of the animation)
                val textSize = animateIntAsState(
                    if (it == lastLrcLineI) 20 else 16,
                    TweenSpec(500)
                )
                val color = animateFloatAsState(
                    if (it == lastLrcLineI) 0.5f else 0f,
                    TweenSpec(500)
                )
                val line = lrcParser!!.LyricLines[it]
                Surface(
                    onClick = {
                        scope.launch {
                            player.seekTo(
                                lerp(0, duration,
                                    inverseLerp(0f, duration / 1000f, line.TimeStomp),
                                )
                            )
                            state.scrollToItem(it, -500)
                        }
                    },
                    modifier = Modifier.padding(vertical = 1.dp),
                    color = MaterialTheme.colorScheme.tertiaryContainer.copy(color.value),
                    shape = RoundedCornerShape(35.dp)
                ) {
                    Text(
                        line.Lyric,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(5.dp, 8.dp),
                        textAlign = TextAlign.Center,
                        fontSize = textSize.value.sp
                    )
                }
            }
        }
    }
    else {
        Column(
            modifier,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                "(。﹏。*)", fontSize = 40.sp, textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(15.dp))
            Text(
                "No Lyrics...",
                fontSize = 16.sp, textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun TagsTab(modifier: Modifier = Modifier) {
    if (nowPlaying != null) {
        val tags = listOf(
            "Title: ${nowPlaying!!.title}",
            "Artist: ${nowPlaying!!.artist}",
            "Album: ${nowPlaying!!.album}",
            "Composer: ${nowPlaying!!.composer}",
            "Genre: ${nowPlaying!!.genre}",
            " ",
            "Duration: ${nowPlaying!!.duration.milliseconds}",
            "Bitrate: ${(nowPlaying!!.bitrate / 1000f).roundToInt()} kbps",
            "Year: ${nowPlaying!!.year}",
            " ",
            "Album artist: ${nowPlaying!!.albumArtist}",
            " ",
            "Path:\n${nowPlaying!!.path.removePrefix("/storage/emulated/")}",
            "LRC path: ${if (nowPlaying!!.hasLRC) "\n${nowPlaying!!.lrcPath}" else "No lrc file found"}",
        )
        LazyColumn(
            modifier,
            contentPadding = PaddingValues(vertical = 200.dp)
        ) {
            items(tags.count()) { i ->
                Text(
                    tags[i],
                    fontSize = 20.sp,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
        }
    }
}
