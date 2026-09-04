package mb28.crysongs

import android.app.NotificationManager
import android.os.Bundle
import android.view.Window
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ContainedLoadingIndicator
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.getSystemService
import androidx.core.view.WindowCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import mb28.crysongs.core.Settings
import mb28.crysongs.core.Settings.requestAllFilesAccessOrFinish
import mb28.crysongs.core.setupPermissions
import mb28.crysongs.icons.album
import mb28.crysongs.icons.artist
import mb28.crysongs.icons.folder
import mb28.crysongs.icons.library_music
import mb28.crysongs.icons.list_2
import mb28.crysongs.icons.music_note_2
import mb28.crysongs.icons.queue_music
import mb28.crysongs.icons.search
import mb28.crysongs.icons.stylus_brush
import mb28.crysongs.icons.theater_comedy
import mb28.crysongs.ui.MiniPlayer
import mb28.crysongs.ui.QueryPage
import mb28.crysongs.ui.TracksList
import mb28.crysongs.ui.theme.CrySongsTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3ExpressiveApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        window.isNavigationBarContrastEnforced = false
        window.requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS)

        requestAllFilesAccessOrFinish()
        setupPermissions()
        Settings.load()
        super.onCreate(savedInstanceState)

        val nm = getSystemService<NotificationManager>()!!

        player.setOnCompletionListener {
            try {
                nm.cancel(0)
                setAndPlay(
                    playerQuery[(playerQuery.indexOf(nowPlaying) + 1).coerceIn(0, playerQuery.count() - 1)],
                    false
                )
            } catch (_: Exception) {

            }
        }

        playerLoop(nm, this, Color(0xffcdb5de))

        lifecycleScope.launch {
            refreshTracksList(this@MainActivity)
        }

        setContent {
            CrySongsTheme {
                val selectedIndex = rememberSaveable { mutableIntStateOf(0) }
                val selectedSet = remember { mutableStateOf(false) }
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = MaterialTheme.colorScheme.surface,
                    bottomBar = {
                        Column {
                            MiniPlayer(selectedSet)
                            Spacer(Modifier.height(3.dp))
                            NavBar(selectedIndex, selectedSet)
                        }
                    }
                ) { innerPadding ->
                    when (selectedIndex.intValue) {
                        0 -> TracksList(Modifier.padding(innerPadding))
                        1 -> QueryPage()
                        4 -> SearchPage()
                        else -> {
                            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                Text("Coming soon!")
                            }
                        }
                    }

                }
            }
        }

    }

    override fun onDestroy() {
        NotificationManagerCompat.from(this).cancel(0)
        super.onDestroy()
    }
}

@Composable
fun NavBar(selectedIndex: MutableIntState, secondSet: MutableState<Boolean>) {
    val firstTabs = rememberSaveable { listOf("Tracks", "Query", "Playlists", "Folders", "Search") }
    val secondTabs = rememberSaveable { listOf("Artists", "Albums", "Genres", "Composers", "Other") }
    HorizontalFloatingToolbar(
        expanded = true,
        contentPadding = PaddingValues(horizontal = 10.dp),
        modifier = Modifier.padding(
            bottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding() + 2.dp
        ).padding(horizontal = 15.dp)
            .height(65.dp),
        colors = FloatingToolbarDefaults.standardFloatingToolbarColors().copy(
            toolbarContainerColor = MaterialTheme.colorScheme.surfaceContainerLow.copy(alpha = 0.95f)
        ),
    ) {
        if (!secondSet.value) {
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
                    label = { Text(label) },
                )
            }
        }
        else {
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











