package mb28.crysongs

import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.os.PowerManager
import android.view.Window
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
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
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import mb28.crysongs.icons.album
import mb28.crysongs.icons.artist
import mb28.crysongs.icons.folder
import mb28.crysongs.icons.library_music
import mb28.crysongs.icons.list_2
import mb28.crysongs.icons.music_note_2
import mb28.crysongs.icons.queue_music
import mb28.crysongs.icons.search
import mb28.crysongs.icons.settings
import mb28.crysongs.icons.stylus_brush
import mb28.crysongs.icons.theater_comedy
import mb28.crysongs.ui.MiniPlayer
import mb28.crysongs.ui.PermissionsPage
import mb28.crysongs.ui.PlaylistsPage
import mb28.crysongs.ui.QueryPage
import mb28.crysongs.ui.SearchPage
import mb28.crysongs.ui.TracksList
import mb28.crysongs.ui.more_pages.CustomTagsPage
import mb28.crysongs.ui.more_pages.FoldersPage
import mb28.crysongs.ui.other.EdgeLightingEffect
import mb28.crysongs.ui.theme.CrySongsTheme

var noCoverBitmap: ImageBitmap? = null
var notificationColor: Int? = null

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3ExpressiveApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        window.isNavigationBarContrastEnforced = false
        window.requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS)

        if (!Environment.isExternalStorageManager() ||
            !getSystemService<PowerManager>()!!.isIgnoringBatteryOptimizations(packageName)) {
            super.onCreate(savedInstanceState)
            setContent { CrySongsTheme { PermissionsPage(Modifier.fillMaxSize(), this) } }
            return
        }

        Settings.load()
        setupPermissions()
        setupPlayer()
        scheduleNotifications(this)
        super.onCreate(savedInstanceState)

        if (noCoverBitmap == null) {
            noCoverBitmap = resources.getDrawable(R.drawable.null_track_cover).toBitmap().asImageBitmap()
        }

        lifecycleScope.launch {
            refreshTracksList(this@MainActivity)
        }

        setContent {
            CrySongsTheme {
                if (notificationColor == null) { notificationColor = MaterialTheme.colorScheme.primary.toArgb() }
                val selectedIndex = rememberSaveable { mutableIntStateOf(0) }
                val selectedSet = remember { mutableStateOf(false) }
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
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
                ) { innerPadding ->
                    Box {
                        when (selectedIndex.intValue) {
                            0 -> TracksList(Modifier.padding(innerPadding))
                            1 -> QueryPage()
                            2 -> PlaylistsPage()
                            3 -> FoldersPage()
                            4 -> SearchPage()
                            5 -> CustomTagsPage(artists, "artists")
                            6 -> CustomTagsPage(albums, "albums")
                            7 -> CustomTagsPage(genres, "genres")
                            8 -> CustomTagsPage(composers, "composers")
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

    override fun onResume() {
        refreshTracksList(this@MainActivity)
        super.onResume()
    }
}

@Composable
fun NavBar(selectedIndex: MutableIntState, secondSet: MutableState<Boolean>) {
    val firstTabs = rememberSaveable { listOf("Tracks", "Query", "Playlists", "Folders", "Search") }
    val secondTabs = rememberSaveable { listOf("Artists", "Albums", "Genres", "Composers", "Other") }
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
            val state = remember {
                MutableTransitionState(false).apply {
                    targetState = true
                }
            }
            AnimatedVisibility(
                state,
                enter = slideInHorizontally { -100 }
            ) {
                Row {
                    firstTabs.forEachIndexed { index, label ->
                        NavigationBarItem(
                            selected = selectedIndex.intValue == index,
                            onClick = { selectedIndex.intValue = index },
                            icon = {
                                Icon(
                                    when(index) {
                                        0 -> music_note_2
                                        1 -> queue_music
                                        2 -> library_music
                                        3 -> folder
                                        4 -> search
                                        else -> throw Exception()
                                    },
                                    null
                                )
                            },
                            label = { Text(label) }
                        )
                    }
                }
            }
        }
        else {
            val state = remember {
                MutableTransitionState(false).apply {
                    targetState = true
                }
            }
            AnimatedVisibility(
                state,
                enter = slideInHorizontally { 100 }
            ) {
                Row {
                    secondTabs.forEachIndexed { index, label ->
                        NavigationBarItem(
                            selected = selectedIndex.intValue == index + 5,
                            onClick = { selectedIndex.intValue = index + 5 },
                            icon = {
                                Icon(
                                    when(index + 5) {
                                        5 -> artist
                                        6 -> album
                                        7 -> theater_comedy
                                        8 -> stylus_brush
                                        9 -> list_2
                                        else -> throw Exception()
                                    },
                                    null
                                )
                            },
                            label = { Text(label) },
                        )
                    }
                }

            }
        }
    }
}
