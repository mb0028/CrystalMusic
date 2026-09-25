package mb28.crysongs

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.LinearWavyProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEachIndexed
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import mb28.crysongs.core.Settings
import mb28.crysongs.core.Track
import mb28.crysongs.ui.theme.CrySongsTheme


class CacheCoversActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        window.isNavigationBarContrastEnforced = false
        super.onCreate(savedInstanceState)

        val count = tracks.count().toFloat()
        var last by mutableStateOf("")
        var progress by mutableIntStateOf(1)

        lifecycleScope.launch(Dispatchers.IO) {
            tracks.fastForEachIndexed { i, track ->
                Track.createOrGetThumbnail(track.path)
                last = track.path
                progress = i
            }
            Settings.tips_cacheThumbs = false
            Settings.save()
            this@CacheCoversActivity.finish()
        }

        setContent {
            CrySongsTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) {
                    Column(
                        Modifier.fillMaxSize(),
                        Arrangement.Center,
                        Alignment.CenterHorizontally
                    ) {
                        Text(last)
                        Spacer(Modifier.height(10.dp))
                        LinearWavyProgressIndicator(
                            { progress.toFloat() / count }
                        )
                    }
                }
            }
        }
    }
}
