package mb28.crysongs

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.os.PowerManager
import android.view.Window
import androidx.activity.ComponentActivity
import androidx.activity.compose.PredictiveBackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.FloatingToolbarDefaults
import androidx.compose.material3.HorizontalFloatingToolbar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.getSystemService
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import mb28.crysongs.core.Settings
import mb28.crysongs.core.scheduleNotifications
import mb28.crysongs.core.setupPermissions
import mb28.crysongs.icons.settings
import mb28.crysongs.ui.MiniPlayer
import mb28.crysongs.ui.PermissionsPage
import mb28.crysongs.ui.more_pages.PlaylistsPage
import mb28.crysongs.ui.more_pages.QueryPage
import mb28.crysongs.ui.more_pages.SearchPage
import mb28.crysongs.ui.TracksList
import mb28.crysongs.ui.more_pages.CustomTagsPage
import mb28.crysongs.ui.more_pages.FoldersPage
import mb28.crysongs.ui.other.EdgeLightingEffect
import mb28.crysongs.ui.other.NavTab
import mb28.crysongs.ui.theme.CrySongsTheme
import kotlin.coroutines.cancellation.CancellationException

var noCoverBitmap: ImageBitmap? = null
var notificationColor: Int? = null

class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @OptIn(ExperimentalMaterial3ExpressiveApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        window.isNavigationBarContrastEnforced = false
        window.requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS)

        if (!Environment.isExternalStorageManager() ||
            !getSystemService<PowerManager>()!!.isIgnoringBatteryOptimizations(packageName)) {
            super.onCreate(savedInstanceState)
            setContent { CrySongsTheme { Scaffold(Modifier.fillMaxSize()) {
                PermissionsPage(Modifier.fillMaxSize(), this) }  } }
            return
        }

        Settings.load()
        setupPermissions()
        setupPlayer()
        super.onCreate(savedInstanceState)

        if (noCoverBitmap == null) {
            noCoverBitmap = resources.getDrawable(R.drawable.null_track_cover).toBitmap().asImageBitmap()
        }

        lifecycleScope.launch {
            refreshTracksList(this@MainActivity)
            scheduleNotifications(this@MainActivity)
        }


        setContent {
            var backHeld by remember { mutableStateOf(false) }
            val animatedOpacity by animateFloatAsState(if (backHeld) 0.8f else 1f)
            PredictiveBackHandler { progress ->
                backHeld = true
                try { progress.collect { }; finish() }
                catch (_: CancellationException) { }
                finally { backHeld = false }
            }

            CrySongsTheme {
                if (notificationColor == null) { notificationColor = MaterialTheme.colorScheme.primary.toArgb() }
                val selectedIndex = rememberSaveable { mutableIntStateOf(Settings.navTabs[Settings.initTab]) }
                val selectedSet = remember { mutableStateOf(Settings.initTab > 4) }
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                        .scale(animatedOpacity).alpha(animatedOpacity),
                    containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                    bottomBar = {
                        Column {
                            MiniPlayer(selectedSet, this@MainActivity)
                            Spacer(Modifier.height(3.dp))
                            NavBar(selectedIndex, selectedSet)
                        }
                    },
                    topBar = {
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .statusBarsPadding()
                                .padding(5.dp),
                            Arrangement.End
                        ) {
                            FilledTonalIconButton(
                                {
                                    startActivity(Intent(this@MainActivity, SettingsActivity::class.java))
                                }
                            ) {
                                Icon(settings, null)
                            }
                        }
                    }
                ) {
                    Box {
                        when (selectedIndex.intValue) {
                            0 -> {
                                var refreshing by remember { mutableStateOf(false) }
                                PullToRefreshBox(
                                    refreshing,
                                    {
                                        refreshing = true
                                        lifecycleScope.launch {
                                            refreshTracksList(this@MainActivity)
                                            refreshing = false
                                        }
                                    }
                                ) {
                                    TracksList()
                                }
                            }
                            1 -> QueryPage()
                            2 -> PlaylistsPage()
                            3 -> FoldersPage()
                            4 -> SearchPage(this@MainActivity)
                            5 -> CustomTagsPage("artists", this@MainActivity)
                            6 -> CustomTagsPage("albums", this@MainActivity)
                            7 -> CustomTagsPage("genres", this@MainActivity)
                            8 -> CustomTagsPage("bitrates", this@MainActivity)
                            else -> {
                                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                    Text("Coming soon!")
                                }
                            }
                        }
                    }

                    EdgeLightingEffect(this)
                }
            }
        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (!hasFocus && !isPlaying) {
            NotificationManagerCompat.from(this).cancel(0)
        }
    }
}

@Composable
fun NavBar(selectedIndex: MutableIntState, secondSet: MutableState<Boolean>) {
    HorizontalFloatingToolbar(
        expanded = true,
        contentPadding = PaddingValues(horizontal = 10.dp),
        modifier = Modifier
            .navigationBarsPadding()
            .padding(horizontal = 15.dp)
            .padding(bottom = 2.dp)
            .height(65.dp),
        colors = FloatingToolbarDefaults.standardFloatingToolbarColors().copy(
            toolbarContainerColor = MaterialTheme.colorScheme.surfaceBright.copy(0.95f)
        ),
    ) {
        if (!secondSet.value) {
            val state = remember { MutableTransitionState(false).apply { targetState = true } }
            AnimatedVisibility(
                state,
                enter = slideInHorizontally { -100 }
            ) {
                Row {
                    for (i in 0..4)
                        NavTab(i, selectedIndex.intValue == Settings.navTabs[i]) { selectedIndex.intValue = it }
                }
            }
        }
        else {
            val state = remember { MutableTransitionState(false).apply { targetState = true } }
            AnimatedVisibility(
                state,
                enter = slideInHorizontally { 100 }
            ) {
                Row {
                    for (i in 5..9)
                        NavTab(i, selectedIndex.intValue == Settings.navTabs[i]) { selectedIndex.intValue = it }
                }
            }
        }
    }
}
