package mb28.crysongs

import android.app.NotificationManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
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
import mb28.crysongs.icons.folder
import mb28.crysongs.icons.library_music
import mb28.crysongs.icons.list_2
import mb28.crysongs.icons.music_note_2
import mb28.crysongs.icons.queue_music
import mb28.crysongs.ui.MiniPlayer
import mb28.crysongs.ui.QueryPage
import mb28.crysongs.ui.TracksList
import mb28.crysongs.ui.theme.CrySongsTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3ExpressiveApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        window.isNavigationBarContrastEnforced = false

        requestAllFilesAccessOrFinish()
        setupPermissions()
        Settings.load()
        super.onCreate(savedInstanceState)

        val nm = getSystemService<NotificationManager>()!!

        player.setOnCompletionListener {
            nm.cancel(0)
        }

        playerLoop(nm, this, Color(0xffcdb5de))

        lifecycleScope.launch {
            refreshTracksList(this@MainActivity)
        }

        setContent {
            CrySongsTheme {
                val selectedIndex = rememberSaveable { mutableIntStateOf(0) }
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = MaterialTheme.colorScheme.surface,
                    bottomBar = {
                        Column {
                            MiniPlayer()
                            Spacer(Modifier.height(3.dp))
                            NavBar(selectedIndex)
                        }
                    }
                ) { innerPadding ->
                    when (selectedIndex.intValue) {
                        0 -> TracksList(Modifier.padding(innerPadding))
                        1 -> QueryPage()
                        else -> {}
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
fun NavBar(selectedIndex: MutableIntState) {
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
        NavigationBarItem(
            selected = selectedIndex.intValue == 0,
            onClick = { selectedIndex.intValue = 0 },
            icon = { Icon(music_note_2, null) },
            label = { Text("Tracks") },
        )
        NavigationBarItem(
            selected = selectedIndex.intValue == 1,
            onClick = { selectedIndex.intValue = 1 },
            icon = { Icon(queue_music, null) },
            label = { Text("Query") },
        )
        NavigationBarItem(
            selected = selectedIndex.intValue == 2,
            onClick = { selectedIndex.intValue = 2 },
            icon = { Icon(library_music, null) },
            label = { Text("Playlists") },
        )
        NavigationBarItem(
            selected = selectedIndex.intValue == 3,
            onClick = { selectedIndex.intValue = 3 },
            icon = { Icon(folder, null) },
            label = { Text("Folders") },
        )
        NavigationBarItem(
            selected = selectedIndex.intValue == 4,
            onClick = { selectedIndex.intValue = 4 },
            icon = { Icon(list_2, null) },
            label = { Text("Other") },
        )
    }
}











