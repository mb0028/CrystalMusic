package mb28.crysongs

import android.annotation.SuppressLint
import android.app.ComponentCaller
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
import androidx.compose.animation.core.EaseOutCubic
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ShortNavigationBar
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.getSystemService
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import mb28.crysongs.core.Settings
import mb28.crysongs.core.checkForUpdate
import mb28.crysongs.core.openLink
import mb28.crysongs.core.scheduleNotifications
import mb28.crysongs.core.setupPermissions
import mb28.crysongs.icons.arrow_cool_down
import mb28.crysongs.icons.menu
import mb28.crysongs.icons.search
import mb28.crysongs.icons.settings
import mb28.crysongs.ui.EXTRA_ARTIST_ALBUM
import mb28.crysongs.ui.FS_RESULT_OPEN_ALBUM
import mb28.crysongs.ui.FS_RESULT_OPEN_ARTIST
import mb28.crysongs.ui.LandscapePage
import mb28.crysongs.ui.MiniPlayer
import mb28.crysongs.ui.PermissionsPage
import mb28.crysongs.ui.REQUEST_CODE_FS_PLAYER
import mb28.crysongs.ui.TracksPage
import mb28.crysongs.ui.more_pages.CustomTagsPage
import mb28.crysongs.ui.more_pages.FoldersPage
import mb28.crysongs.ui.more_pages.HomePage
import mb28.crysongs.ui.more_pages.PlaylistsPage
import mb28.crysongs.ui.more_pages.QueryPage
import mb28.crysongs.ui.more_pages.SearchPage
import mb28.crysongs.ui.more_pages.customTabHighlightedItem
import mb28.crysongs.ui.other.CrystalDrawerItem
import mb28.crysongs.ui.other.EdgeLightingEffect
import mb28.crysongs.ui.other.NavTab
import mb28.crysongs.ui.other.getCurrentNavItemIcon
import mb28.crysongs.ui.other.getCurrentNavItemName
import mb28.crysongs.ui.theme.CrySongsTheme
import kotlin.coroutines.cancellation.CancellationException

var noCoverBitmap: ImageBitmap? = null
var notificationColor: Int? = null
var requestBlur by mutableStateOf(false)

class MainActivity : ComponentActivity() {
    val selectedIndex = mutableIntStateOf(0)

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @OptIn(ExperimentalMaterial3ExpressiveApi::class, ExperimentalMaterial3Api::class)
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
        selectedIndex.intValue = Settings.navTabs[Settings.initTab]
        setupPermissions()
        setupPlayer {
            if (Settings.recents.isNotEmpty() && !isPlayerLoopStarted) {
                playerQuery.addAll(Settings.recents)
                setAndPlay(Settings.recents.first(), resetQuery = false, play = false)
            }
        }
        super.onCreate(savedInstanceState)

        if (noCoverBitmap == null) {
            noCoverBitmap = resources.getDrawable(R.drawable.null_track_cover).toBitmap().asImageBitmap()
        }

        lifecycleScope.launch {
            refreshTracksList(this@MainActivity)
            scheduleNotifications(this@MainActivity)
            if (Settings.updateState == 0) {
                checkForUpdate(this@MainActivity)
            }
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
                val isLandscape = LocalConfiguration.current.orientation == 2
                if (notificationColor == null) { notificationColor = MaterialTheme.colorScheme.primary.toArgb() }
                val drawerState = rememberDrawerState(DrawerValue.Closed)

                ModalNavigationDrawer(
                    drawerState = drawerState,
                    drawerContent = {
                        ModalDrawerSheet(
                            drawerState,
                            Modifier
                                .widthIn(Dp.Unspecified, 250.dp)
                                .padding(5.dp),
                            RoundedCornerShape(30.dp)
                        ) {
                            Drawer(drawerState)
                        }
                    }
                ) {
                    val blur by animateDpAsState(
                        if (requestBlur) 8.dp else 0.dp,
                        animationSpec = TweenSpec(
                            300,
                            easing = EaseOutCubic
                        )
                    )
                    Scaffold(
                        modifier = Modifier
                            .fillMaxSize()
                            .scale(animatedOpacity)
                            .alpha(animatedOpacity)
                            .blur(blur),
                        containerColor = androidx.compose.ui.graphics.Color.Transparent,
                        contentColor = MaterialTheme.colorScheme.onSurface,
                        bottomBar = {
                            if (isLandscape) {
                                Row(
                                    Modifier
                                        .fillMaxWidth()
                                        .padding(bottom = 10.dp, start = 15.dp)
                                        .padding(
                                            end = WindowInsets.navigationBars.asPaddingValues()
                                                .calculateEndPadding(LayoutDirection.Ltr)
                                        ),
                                    Arrangement.End
                                ) {
                                    Box(Modifier.fillMaxWidth(0.5f)) {
                                        MiniPlayer(this@MainActivity)
                                    }
                                }
                            }
                            else Column {
                                MiniPlayer(this@MainActivity)
                                if (Settings.hideNavBar) {
                                    Spacer(Modifier.navigationBarsPadding())
                                } else {
                                    NavBar(selectedIndex)
                                }
                            }
                        },
                        topBar = {
                            Topbar(drawerState)
                        }
                    ) {
                        if (isLandscape) {
                            LandscapePage(it, this)
                        } else {
                            MainContent()
                        }

                        EdgeLightingEffect(this)
                    }
                }
            }
        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (!hasFocus && !isPlaying) {
            NotificationManagerCompat.from(this).cancel(0)
        }
        if (hasFocus) requestBlur = false
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?,
                                  caller: ComponentCaller) {
        super.onActivityResult(requestCode, resultCode, data, caller)
        requestBlur = false
        if (requestCode == REQUEST_CODE_FS_PLAYER) when(resultCode) {
            FS_RESULT_OPEN_ARTIST -> {
                customTabHighlightedItem = data?.getStringExtra(EXTRA_ARTIST_ALBUM)
                selectedIndex.intValue = 5
            }
            FS_RESULT_OPEN_ALBUM -> {
                customTabHighlightedItem = data?.getStringExtra(EXTRA_ARTIST_ALBUM)
                selectedIndex.intValue = 6
            }
        }
    }

    @Composable
    fun MainContent() {
        when (selectedIndex.intValue) {
            0 -> TracksPage(this)
            1 -> QueryPage()
            2 -> PlaylistsPage()
            3 -> FoldersPage()
            4 -> HomePage(this)
            5 -> CustomTagsPage("artists", this)
            6 -> CustomTagsPage("albums", this)
            7 -> CustomTagsPage("genres", this)
            8 -> CustomTagsPage("bitrates", this)
            9 -> SearchPage(this)
            else -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Coming soon!")
                }
            }
        }
    }

    @Composable
    private fun Drawer(drawerState: DrawerState) {
        val scope = rememberCoroutineScope()

        Text("Crystal Songs", Modifier.padding(16.dp), fontSize = 26.sp)
        HorizontalDivider(Modifier.padding(vertical = 10.dp))

        if (Settings.updateState == 1) {
            CrystalDrawerItem(
                "Click to update",
                containerColor = MaterialTheme.colorScheme.errorContainer
            ) {
                openLink(
                    this,
                    "https://github.com/mb0028/CrystalMusic/releases/latest"
                )
            }
        }
        CrystalDrawerItem("Settings", icon = settings) {
            startActivity(Intent(this,
                SettingsActivity::class.java))
        }
        HorizontalDivider(Modifier.padding(vertical = 10.dp))

        LazyColumn {
            items(9) { i ->
                CrystalDrawerItem(
                    getCurrentNavItemName(i),
                    icon = getCurrentNavItemIcon(i),
                    selected = i == selectedIndex.intValue
                ) {
                    selectedIndex.intValue = i
                    scope.launch { drawerState.close() }
                }
            }
        }
    }

    @Composable
    private fun Topbar(drawerState: DrawerState) {
        val scope = rememberCoroutineScope()
        Row(
            Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 15.dp),
            Arrangement.SpaceBetween
        ) {
            BadgedBox({
                if (Settings.updateState == 1) {
                    Badge { Text("Update available")
                    } }
            }) {
                FilledTonalIconButton(
                    { scope.launch { drawerState.open() } }
                ) { Icon(menu, null) }
            }
            Row {
                AnimatedVisibility(
                    Settings.dismissMiniPlayer,
                    enter = fadeIn() + scaleIn(),
                    exit = fadeOut() + slideOutVertically { -50 }
                ) {
                    FilledTonalIconButton(
                        { Settings.dismissMiniPlayer = false }
                    ) {
                        Icon(
                            arrow_cool_down, null,
                            Modifier.rotate(180f)
                        )
                    }
                }

                FilledTonalIconButton(
                    { selectedIndex.intValue = 9 }
                ) { Icon(search, null) }
            }
        }
    }

    @Composable
    private fun NavBar(selectedIndex: MutableIntState) {
        val state = remember { MutableTransitionState(false).apply { targetState = true } }

        ShortNavigationBar(
            containerColor = MaterialTheme.colorScheme.surfaceContainer.copy(0.95f)
        ) {
            AnimatedVisibility(
                state,
                enter = scaleIn() + fadeIn()
            ) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    Arrangement.SpaceBetween
                ) {
                    for (i in 0..4)
                        NavTab(
                            i,
                            selectedIndex.intValue == Settings.navTabs[i]
                        ) { selectedIndex.intValue = it }
                }
            }
        }
    }
}
