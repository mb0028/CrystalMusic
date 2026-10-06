package mb28.crysongs

import android.media.MediaPlayer
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FlexibleBottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.fastCoerceAtLeast
import androidx.compose.ui.util.fastRoundToInt
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import mb28.crysongs.core.Track
import mb28.crysongs.core.formatDurationMs
import mb28.crysongs.icons.arrow_back
import mb28.crysongs.ui.theme.CrySongsTheme
import mb28.music.LrcParser
import kotlin.time.Duration.Companion.milliseconds

class LrcMakerActivity : ComponentActivity() {
    private val editorPlayer = MediaPlayer()
    private var path: String? = null
    private var track: Track? by mutableStateOf(null)
    private val scope = CoroutineScope(Dispatchers.Main)
    private var isPlaying by mutableStateOf(false)
    private var pos by mutableIntStateOf(0)
    private var duration by mutableIntStateOf(0)

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        window.isNavigationBarContrastEnforced = false
        super.onCreate(savedInstanceState)

        if (Settings.System.getInt(contentResolver, Settings.System.ACCELEROMETER_ROTATION) == 1) {
            Toast.makeText(this, "Note: Rotating phone will resets LRC Maker, so turn auto rotation off",
                Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(this, "Note: Rotating phone will resets LRC Maker",
                Toast.LENGTH_LONG).show()
        }

        path = intent.getStringExtra(EXTRA_PATH)
        if (path == null) {
            finish()
        }
        player.stop()
        editorPlayer.apply {
            setDataSource(path)
            prepare()
        }
        lifecycleScope.launch {
            track = Track.getTags(path, this@LrcMakerActivity)
        }

        scope.launch {
            while (true) {
                pos = editorPlayer.currentPosition
                duration = editorPlayer.duration
                delay(500.milliseconds)
            }
        }

        setContent {
            CrySongsTheme {
                BackHandler(true) {
                    Toast.makeText(this, "To exit, click back button at the top left",
                        Toast.LENGTH_SHORT).show()
                }
                Scaffold(
                    Modifier.fillMaxSize(),
                    topBar = {
                        TopAppBar(
                            { Text("LRC Maker") },
                            navigationIcon = {
                                IconButton(
                                    { finish() },
                                    colors = IconButtonDefaults.iconButtonColors().copy(
                                        MaterialTheme.colorScheme.surfaceContainerHigh
                                    ),
                                    modifier = Modifier.padding(horizontal = 15.dp)
                                ) { Icon(arrow_back, null) }
                            },
                            actions = {
                                if (isReady) {
                                    IconButton(
                                        {
                                        },
                                    ) { Icon(arrow_back, "Save") }
                                    IconButton(
                                        {
                                        },
                                    ) { Icon(arrow_back, "Save") }
                                } else {
                                    OutlinedButton(
                                        {
                                            lrcParser = LrcParser(lyric, true)
                                            isReady = true
                                        }
                                    ) {
                                        Text("Start")
                                    }
                                }
                            }
                        )
                    },
                    bottomBar = {
                        Column(Modifier.fillMaxWidth()) {
                            LinearProgressIndicator(
                                { pos.toFloat() / duration },
                                Modifier.fillMaxWidth()
                            )
                            BottomBar()
                        }
                    }
                ) { paddingValues ->
                    LrcMaker(paddingValues)
                }
            }
        }
    }

    @Composable
    fun BottomBar() {
        FlexibleBottomAppBar(
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    formatDurationMs(pos.milliseconds) + " / " + formatDurationMs(duration.milliseconds),
                    fontSize = 18.sp
                )
                Text(track?.title ?: "Loading...")
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                FilledTonalButton(
                    { editorPlayer.seekTo((editorPlayer.currentPosition - 5000)
                        .coerceAtLeast(0)) },
                ) {
                    Text("-5s")
                }
                Spacer(Modifier.width(5.dp))
                FilledTonalButton(
                    { editorPlayer.seekTo((editorPlayer.currentPosition + 5000)
                        .coerceAtMost(editorPlayer.duration)) },
                ) {
                    Text("+5s")
                }
                FilledIconButton(
                    {
                        if (editorPlayer.isPlaying) editorPlayer.pause()
                        else editorPlayer.start()
                        isPlaying = editorPlayer.isPlaying
                    },
                    shape = RoundedCornerShape(15.dp),
                    modifier = Modifier
                        .aspectRatio(1f)
                        .padding(5.dp),
                ) {
                    Icon(
                        painterResource(if (isPlaying) R.drawable.pause_24px
                        else R.drawable.play_arrow_24px),
                        null
                    )
                }
            }
        }
    }

    private var isReady by mutableStateOf(false)
    private var lyric by mutableStateOf("")
    private var lrcParser by mutableStateOf<LrcParser?>(null)

    @Composable
    private fun LrcMaker(paddingValues: PaddingValues) {
        if (!isReady) {
            OutlinedTextField(
                lyric,
                { lyric = it },
                Modifier.fillMaxSize()
                    .padding(paddingValues).padding(10.dp),
                shape = RoundedCornerShape(25.dp),
                minLines = 15,
                label = {
                    Text("Write or paste lyrics text, then click start")
                }
            )
        } else {
            lrcParser?.let {
                LazyColumn(
                    Modifier.fillMaxSize().padding(horizontal = 10.dp),
                    contentPadding = paddingValues
                ) {
                    items(it.LyricLines.count()) { i ->
                        OutlinedCard(
                            {

                            },
                            modifier = Modifier.fillMaxWidth().height(100.dp)
                                .padding(bottom = 10.dp)
                        ) {
                            Box(
                                Modifier.fillMaxSize()
                                    .padding(8.dp)
                            ) {
                                val line = it.LyricLines[i]
                                Text(
                                    line.Lyric,
                                    Modifier.align(Alignment.TopStart)
                                )
                                Text(
                                    (line.TimeStomp.fastRoundToInt() / 60).fastCoerceAtLeast(0)
                                        .toString().padStart(2, '0') +
                                    ":" + line.TimeStomp.fastRoundToInt().rem(60)
                                        .toString().padStart(2, '0'),
                                    Modifier.align(Alignment.BottomStart)
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        scope.cancel()
        editorPlayer.stop()
        editorPlayer.release()
        super.onDestroy()
    }
}
