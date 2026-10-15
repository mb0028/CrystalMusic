package mb28.crysongs

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.PowerManager
import android.transition.Slide
import android.util.Log
import android.view.RoundedCorner
import android.view.Window
import androidx.activity.ComponentActivity
import androidx.activity.compose.PredictiveBackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.getSystemService
import androidx.core.graphics.drawable.toBitmap
import mb28.crysongs.core.Settings
import mb28.crysongs.core.Settings.fsStyle
import mb28.crysongs.ui.EXTRA_ARTIST_ALBUM
import mb28.crysongs.ui.FS_RESULT_OPEN_ALBUM
import mb28.crysongs.ui.FS_RESULT_OPEN_ARTIST
import mb28.crysongs.ui.PermissionsPage
import mb28.crysongs.ui.fullscreen_player.FSChangePageRow
import mb28.crysongs.ui.fullscreen_player.FSCover
import mb28.crysongs.ui.fullscreen_player.FSLyricsTab
import mb28.crysongs.ui.fullscreen_player.FSPlayerButtonsRow
import mb28.crysongs.ui.fullscreen_player.FSProgressBarRow
import mb28.crysongs.ui.fullscreen_player.FSTagsTab
import mb28.crysongs.ui.other.fadingEdgeVertical
import mb28.crysongs.ui.theme.CrySongsTheme
import kotlin.coroutines.cancellation.CancellationException

const val EXTRA_LOAD_FROM_OTHER_APPS = "EXTRA_LOAD_FROM_OTHER_APPS"
const val EXTRA_PATH = "EXTRA_PATH"

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
            decorView.keepScreenOn = true
        }

        if (intent.getBooleanExtra(EXTRA_LOAD_FROM_OTHER_APPS, false)) {
            val path = intent.getStringExtra(EXTRA_PATH)
            if (path == null) {
                Log.wtf("Crystal Music", "App crashes because intent.getStringExtra(EXTRA_PATH) is null. " +
                        "Normally this should never happens. open an issue on app's github page if you see this error")
                throw UnsupportedOperationException()
            }
            if (!Environment.isExternalStorageManager() ||
                !getSystemService<PowerManager>()!!.isIgnoringBatteryOptimizations(packageName)) {
                super.onCreate(savedInstanceState)
                setContent { CrySongsTheme { PermissionsPage(Modifier.fillMaxSize(), this) } }
                return
            }
            Settings.load()

            setupPlayer {
                playerQuery.clear()
                playerQuery.add(path)
                setAndPlay(playerQuery.first(), false)
            }
        }

        if (noCoverBitmap == null) {
            noCoverBitmap = resources.getDrawable(R.drawable.null_track_cover).toBitmap().asImageBitmap()
        }
        super.onCreate(savedInstanceState)

        if (resources.configuration.orientation == 2) {
            finish()
        }

        setContent {
            var backHeld by remember { mutableStateOf(false) }
            var activityOffset by remember { mutableIntStateOf(0) }
            val animateOffset by animateIntAsState(if (backHeld) 800 else 0)
            val roundness = window.decorView.rootWindowInsets?.getRoundedCorner(
                RoundedCorner.POSITION_TOP_LEFT)?.radius ?: 0
            val deviceCornersShape = RoundedCornerShape((roundness / 3.25f).dp)
            CrySongsTheme(
                Modifier.offset { IntOffset(0, activityOffset + animateOffset) },
                Modifier.clip(deviceCornersShape)
            ) {
                PredictiveBackHandler { progress ->
                    backHeld = true
                    try { progress.collect { }; finish() }
                    catch (_: CancellationException) { backHeld = false }
                }
                Scaffold(
                    Modifier
                        .fillMaxSize().clip(deviceCornersShape)
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
                    containerColor = if (fsStyle == 0) MaterialTheme.colorScheme.background else Color.Transparent,
                    contentColor = if (Settings.whiteText) Color.White else MaterialTheme.colorScheme.onSurface
                ) { innerPadding ->
                    Pager(innerPadding, this)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        window.decorView.keepScreenOn = true
    }

    override fun onDestroy() {
        super.onDestroy()
        window.decorView.keepScreenOn = false
    }
}

@Composable
private fun Pager(innerPadding: PaddingValues, activity: FullscreenPlayerActivity) {
    val selectedTab = rememberPagerState(1) { 3 }

    Box {
        if (fsStyle == 0) {
            Image(
                nowPlayingCover, null,
                contentScale = ContentScale.FillHeight,
                modifier = Modifier
                    .fillMaxSize()
                    .blur(Settings.backgroundBlurRadius.dp)
                    .alpha(0.5f),
            )
        }

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
                           FSCover()
                           Spacer(Modifier.height(15.dp))

                           Text(nowPlayingTags?.title ?: "", fontSize = 24.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
                           Spacer(Modifier.height(10.dp))
                           Text(
                               nowPlayingTags?.artist ?: "", fontSize = 16.sp, maxLines = 1, overflow = TextOverflow.Ellipsis,
                               modifier = Modifier.clickable {
                                   activity.setResult(FS_RESULT_OPEN_ARTIST, Intent()
                                       .putExtra(EXTRA_ARTIST_ALBUM, nowPlayingTags?.artist))
                                   activity.finish()
                               }
                           )
                           Text(
                               nowPlayingTags?.album ?: "", fontSize = 14.sp, maxLines = 1, overflow = TextOverflow.Ellipsis,
                               modifier = Modifier.clickable {
                                   activity.setResult(FS_RESULT_OPEN_ALBUM, Intent()
                                       .putExtra(EXTRA_ARTIST_ALBUM, nowPlayingTags?.album))
                                   activity.finish()
                               }
                           )
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
                    FSLyricsTab(Modifier.fillMaxSize())
                }
                0 -> {
                    FSTagsTab(
                        Modifier
                        .fillMaxSize()
                        .fadingEdgeVertical()
                        .padding(horizontal = 20.dp)
                    )
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
