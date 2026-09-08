package mb28.crysongs

import android.app.Activity
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.transition.Slide
import android.view.RoundedCorner
import android.view.Window
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
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
import androidx.core.graphics.drawable.toBitmap
import mb28.crysongs.core.Track
import mb28.crysongs.ui.fullscreen_player.FSChangePageRow
import mb28.crysongs.ui.fullscreen_player.FSLyricsTab
import mb28.crysongs.ui.fullscreen_player.FSPlayerButtonsRow
import mb28.crysongs.ui.fullscreen_player.FSProgressBarRow
import mb28.crysongs.ui.fullscreen_player.FSTagsTab
import mb28.crysongs.ui.theme.CrySongsTheme

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

//        val skipLoad = intent.getBooleanExtra(EXTRA_SKIP_LOAD, false)
        super.onCreate(savedInstanceState)

        if (noCoverBitmap == null) {
            noCoverBitmap = resources.getDrawable(R.drawable.null_track_cover).toBitmap().asImageBitmap()
        }

        setContent {
            var activityOffset by remember { mutableIntStateOf(0) }
            CrySongsTheme {
                Scaffold(
                    Modifier
                        .fillMaxSize()
                        .offset { IntOffset(0, activityOffset) }
                        .pointerInput(Unit) {
                            fun onRelease() {
                                if (activityOffset > 200) {
                                    finish()
                                } else {
                                    activityOffset = 0
                                }
                            }
                            detectVerticalDragGestures(
                                onDragEnd = { onRelease() },
                                onDragCancel = { onRelease() }
                            ) { _, dragAmount ->
                                activityOffset =
                                    (activityOffset + dragAmount.toInt()).coerceAtLeast(0)
                            }

                        },
                    containerColor = Color.Transparent,
                    contentColor = MaterialTheme.colorScheme.onSurface
                ) { innerPadding ->
                    Pager(innerPadding, this, activityOffset)
                }
            }
        }
    }
}

@Composable
private fun Pager(innerPadding: PaddingValues, activity: Activity, activityOffset: Int) {
    val roundness = activity.window.decorView.rootWindowInsets?.getRoundedCorner(
        RoundedCorner.POSITION_TOP_LEFT)?.radius ?: 0
    val selectedTab = rememberPagerState(1) { 3 }
    val fsPlayerCover = try {
            BitmapFactory.decodeFile(Track.createOrGetThumbnail(nowPlaying!!.path)).asImageBitmap()
        } catch (_: Exception) {
            noCoverBitmap!!
        }
    val shape = RoundedCornerShape((roundness / 3.25f).dp)

    Box(
        Modifier
            .clip(shape)
            .background(MaterialTheme.colorScheme.surfaceBright.copy(1f - (activityOffset / 1000f)))
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
                       Column(Modifier.fillMaxWidth()) {
                           Spacer(Modifier.height(75.dp))
                           Cover(cover = fsPlayerCover)
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
                           FSProgressBarRow(Modifier.fillMaxWidth())
                           Spacer(Modifier.height(15.dp))
                           FSPlayerButtonsRow()
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
                    FSLyricsTab(Modifier
                        .fillMaxSize()
                        .padding(horizontal = 20.dp))
                }
                0 -> {
                    FSTagsTab(Modifier
                        .fillMaxSize()
                        .padding(horizontal = 20.dp))
                }
            }
        }
        FSChangePageRow(
            Modifier
                .padding(top = innerPadding.calculateTopPadding() + 10.dp)
                .align(Alignment.TopCenter),
            selectedTab, activity
        )
    }
}

@Composable
private fun Cover(modifier: Modifier = Modifier, cover: ImageBitmap) {
    Box(
        modifier
            .fillMaxWidth()
            .background(
            MaterialTheme.colorScheme.surfaceContainer,
            RoundedCornerShape(30.dp)
        )
    ) {
        Image(
            cover,
            "Track cover",
            contentScale = ContentScale.FillWidth,
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(30.dp))
        )
    }
}
