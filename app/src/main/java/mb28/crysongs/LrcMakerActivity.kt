package mb28.crysongs

import android.content.ClipData
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
import androidx.compose.foundation.layout.plus
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
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
import androidx.compose.runtime.DefaultMonotonicFrameClock
import androidx.compose.runtime.MonotonicFrameClock
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ClipEntry
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.fastCoerceAtLeast
import androidx.compose.ui.util.fastCoerceAtMost
import androidx.compose.ui.util.fastRoundToInt
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import mb28.crysongs.core.Track
import mb28.crysongs.core.formatDurationMs
import mb28.crysongs.icons.arrow_back
import mb28.crysongs.icons.arrow_cool_down
import mb28.crysongs.icons.note_alt
import mb28.crysongs.icons.save
import mb28.crysongs.icons.timer_arrow_down
import mb28.crysongs.ui.theme.CrySongsTheme
import mb28.music.LrcParser
import java.io.File
import kotlin.math.roundToInt
import kotlin.time.Duration.Companion.milliseconds

class LrcMakerActivity : ComponentActivity() {
    private val editorPlayer = MediaPlayer()
    private var path: String? = null
    private var track: Track? by mutableStateOf(null)
    private val scope = CoroutineScope(Dispatchers.Main)
    private var isPlaying by mutableStateOf(false)
    private var pos by mutableIntStateOf(0)
    private var duration by mutableIntStateOf(0)
    private var noOverride = true

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        window.isNavigationBarContrastEnforced = false
        super.onCreate(savedInstanceState)

        if (Settings.System.getInt(contentResolver, Settings.System.ACCELEROMETER_ROTATION) == 1) {
            Toast.makeText(this, "Note: Rotating phone will resets progress, so turn off auto rotation",
                Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(this, "Note: Rotating phone will resets progress",
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
                                val c = LocalClipboard.current
                                if (isReady) {
                                    IconButton(
                                        {
                                            val lrcFile = File(path!!.substring(0, path!!
                                                .lastIndexOf('.') + 1) + "lrc")
                                            println(lrcFile.path)
                                            if (lrcFile.exists() && noOverride) {
                                                Toast.makeText(this@LrcMakerActivity, "File already exists!" +
                                                    " click again in next 5s to override", Toast.LENGTH_LONG).show()
                                                lifecycleScope.launch {
                                                    noOverride = false
                                                    delay(5000.milliseconds)
                                                    noOverride = true
                                                }
                                            } else {
                                                lrcFile.createNewFile()
                                                lrcFile.writeText(lrcParser!!.toString())
                                                Toast.makeText(this@LrcMakerActivity, "Saved!",
                                                    Toast.LENGTH_SHORT).show()
                                            }
                                        },
                                    ) { Icon(save, "Save") }
                                    IconButton(
                                        { lifecycleScope.launch {
                                            c.setClipEntry(ClipEntry(ClipData.newPlainText("Lyrics", lrcParser!!.toString())))
                                        } },
                                    ) { Icon(note_alt, "Copy") }
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
            Text(
                formatDurationMs(pos.milliseconds) + " / " + formatDurationMs(duration.milliseconds),
                fontSize = 18.sp
            )

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
    private var selectedLine by mutableIntStateOf(0)
    @Composable
    private fun LrcMaker(paddingValues: PaddingValues) {
        if (!isReady) {
            OutlinedTextField(
                lyric,
                { lyric = it },
                Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(10.dp),
                shape = RoundedCornerShape(25.dp),
                minLines = 15,
                label = {
                    Text("Write or paste lyrics text, then click start")
                }
            )
        } else {
            val state = rememberLazyListState()
            lrcParser?.let {
                LazyColumn(
                    Modifier
                        .fillMaxSize()
                        .padding(horizontal = 10.dp),
                    state,
                    contentPadding = paddingValues + PaddingValues(vertical = 150.dp)
                ) {
                    items(it.LyricLines.count()) { i ->
                        var line by remember { mutableStateOf(it.LyricLines[i]) }
                        OutlinedCard(
                            {

                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(140.dp)
                                .padding(bottom = 10.dp),
                            colors = CardDefaults.outlinedCardColors(
                                if (i == selectedLine) MaterialTheme.colorScheme.secondaryContainer
                                    else Color.Transparent
                            )
                        ) {
                            Box(
                                Modifier
                                    .fillMaxSize()
                                    .padding(8.dp)
                            ) {
                                Text(
                                    line.Lyric,
                                    Modifier
                                        .fillMaxWidth(0.8f)
                                        .align(Alignment.TopStart),
                                    maxLines = 3,
                                    overflow = TextOverflow.Ellipsis
                                )
                                val timeInt = line.TimeStomp.fastRoundToInt()
                                Text(
                                    (timeInt / 60).fastCoerceAtLeast(0).toString().padStart(2, '0') +
                                    ":" + timeInt.rem(60).toString().padStart(2, '0') +
                                    ":" + (line.TimeStomp * 1000).fastRoundToInt().rem(1000).toString().padStart(3, '0'),
                                    Modifier.align(Alignment.BottomStart)
                                )
                                FilledIconButton(
                                    {
                                        editorPlayer.seekTo((line.TimeStomp * 1000).roundToInt())
                                        if (!editorPlayer.isPlaying) {
                                            editorPlayer.start()
                                            isPlaying = true
                                        }
                                        selectedLine = i
                                    },
                                    shape = RoundedCornerShape(15.dp),
                                    modifier = Modifier
                                        .size(45.dp)
                                        .align(Alignment.TopEnd)
                                ) {
                                    Icon(
                                        painterResource(R.drawable.play_arrow_24px),
                                        null
                                    )
                                }
                                Row(
                                    Modifier.align(Alignment.BottomEnd),
                                    verticalAlignment = Alignment.Bottom
                                ) {
                                    OutlinedButton(
                                        {
                                            lrcParser?.LyricLines[i] = line.copy(TimeStomp = line.TimeStomp - 0.1f)
                                            line = lrcParser!!.LyricLines[i]
                                            editorPlayer.seekTo((line.TimeStomp * 1000).roundToInt())
                                            selectedLine = i
                                        }
                                    ) {
                                        Text("-0.1s")
                                    }
                                    Spacer(Modifier.width(5.dp))
                                    OutlinedButton(
                                        {
                                            lrcParser?.LyricLines[i] = line.copy(TimeStomp = line.TimeStomp + 0.1f)
                                            line = lrcParser!!.LyricLines[i]
                                            editorPlayer.seekTo((line.TimeStomp * 1000).roundToInt())
                                            selectedLine = i
                                        }
                                    ) {
                                        Text("+0.1s")
                                    }
                                    Spacer(Modifier.width(5.dp))
                                    FilledIconButton(
                                        {
                                            lrcParser?.LyricLines[i] = line.copy(TimeStomp = (editorPlayer.currentPosition / 1000f))
                                            selectedLine = (i + 1).fastCoerceAtMost(lrcParser!!.Count)
                                            line = lrcParser!!.LyricLines[i]
                                            lifecycleScope.launch { withContext(DefaultMonotonicFrameClock) {
                                                state.animateScrollToItem(selectedLine, -350)
                                            } }
                                        },
                                        shape = RoundedCornerShape(15.dp),
                                        modifier = Modifier.size(60.dp)
                                    ) {
                                        Icon(timer_arrow_down, null)
                                    }
                                }
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
