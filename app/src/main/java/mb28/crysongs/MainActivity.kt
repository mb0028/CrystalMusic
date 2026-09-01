package mb28.crysongs

import android.app.NotificationManager
import android.content.Context
import android.media.MediaPlayer
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ContainedLoadingIndicator
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.getSystemService
import androidx.core.view.WindowCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import mb28.crysongs.core.MediaIndexerVM
import mb28.crysongs.core.Settings
import mb28.crysongs.core.Settings.requestAllFilesAccessOrFinish
import mb28.crysongs.core.setupPermissions
import mb28.crysongs.ui.TracksList
import mb28.crysongs.ui.theme.CrySongsTheme

lateinit var tracksVM: MediaIndexerVM

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3ExpressiveApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        val controller = WindowCompat.getInsetsController(window, window.decorView)
        controller.isAppearanceLightStatusBars = true
        controller.isAppearanceLightNavigationBars = true
        window.isNavigationBarContrastEnforced = false

        requestAllFilesAccessOrFinish()
        setupPermissions()
        Settings.load()
        super.onCreate(savedInstanceState)

        tracksVM = ViewModelProvider(this).get<MediaIndexerVM>()
        player = MediaPlayer()
        val nm = getSystemService<NotificationManager>()!!
        playerLoop(nm, this, Color(0xffcdb5de))

        setContent {
            CrySongsTheme {
                Box(
                    Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    ContainedLoadingIndicator()
                }
            }
        }

        lifecycleScope.launch {
            tracksVM.refreshList(this@MainActivity) {
                setContent {
                    CrySongsTheme {
                        Scaffold(
                            modifier = Modifier.fillMaxSize(),
                            containerColor = MaterialTheme.colorScheme.surface
                        ) { innerPadding ->
                            TracksList(
                                modifier = Modifier.padding(innerPadding),
                            )
                        }
                    }
                }
            }
        }

    }
}













