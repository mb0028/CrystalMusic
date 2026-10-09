package mb28.crysongs

import android.app.Activity
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.plus
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.PrimaryScrollableTabRow
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedListItem
import androidx.compose.material3.Slider
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberSliderState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import mb28.crysongs.core.Settings
import mb28.crysongs.core.Settings.experimental
import mb28.crysongs.core.openLink
import mb28.crysongs.icons.arrow_back
import mb28.crysongs.icons.arrow_cool_down
import mb28.crysongs.icons.flag
import mb28.crysongs.icons.library_add
import mb28.crysongs.ui.fullscreen_player.fullscreenCoverShape
import mb28.crysongs.ui.other.EasySegmentedListItem
import mb28.crysongs.ui.other.EditNavigationItemPopup
import mb28.crysongs.ui.other.NavTab
import mb28.crysongs.ui.other.SettingSwitch
import mb28.crysongs.ui.popups.AddPathPopup
import mb28.crysongs.ui.popups.FlagsPopup
import mb28.crysongs.ui.theme.CrySongsTheme
import mb28.crysongs.ui.theme.FSPlayerUIPreview
import mb28.crysongs.ui.theme.UIPreview
import mb28.monoP.icons.add_2
import mb28.monoP.icons.delete_forever
import java.io.File
import kotlin.math.roundToInt

class SettingsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        window.isNavigationBarContrastEnforced = false

        if (!Environment.isExternalStorageManager()) { finish() }
        Settings.load()

        super.onCreate(savedInstanceState)
        setContent {
            CrySongsTheme {
                var selectedTab by remember { mutableIntStateOf(0) }
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                    topBar = {
                        Column {
                            TopBar(this@SettingsActivity)
                            PrimaryScrollableTabRow(
                                selectedTab,
                                containerColor = MaterialTheme.colorScheme.surfaceContainerLow.copy(0.9f)
                            ) {
                                for (i in 0..3) {
                                    Tab(
                                        selectedTab == i,
                                        { selectedTab = i },
                                        text = { Text(when(i) {
                                            0 -> "Customization"
                                            1 -> "Fullscreen Player"
                                            2 -> "Library"
                                            else -> "Advanced"
                                        }) },
                                    )
                                }
                            }
                        }
                    }
                ) { innerPadding ->
                    LazyColumn(
                        contentPadding = innerPadding.plus(PaddingValues(bottom = 300.dp))
                    ) {
                        item {
                            when(selectedTab) {
                                0 -> UISettings()
                                1 -> FsSettings()
                                2 -> IndexingSettings()
                                3 -> AboutSection(this@SettingsActivity)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun UISettings() {
    var lastClickedNavTab by remember { mutableIntStateOf(-1) }
    val count = 8
    Column(Modifier.padding(10.dp)) {
        UIPreview(Modifier.width(220.dp).align(Alignment.CenterHorizontally))
        Spacer(Modifier.height(5.dp))
        SettingSwitch(
            Settings.useCoverColor,
            "Use artwork color for ui", 0, count,
        ) { Settings.useCoverColor = it; Settings.save() }
        SettingSwitch(
            Settings.showWallpaper,
            "Draw wallpaper", 1, count,
            desc = "Shows system wallpaper behind app",
            enable = !Settings.paintMode
        ) { Settings.showWallpaper = it; Settings.save() }
        SettingSwitch(
            Settings.paintMode,
            "Contrast background", 2, count,
            desc = "Makes app background fully black on dark mode or fully white on light mode",
            enable = !Settings.showWallpaper
        ) { Settings.paintMode = it; Settings.save() }
        SettingSwitch(
            Settings.twoRowTrackCard,
            "Compact track tiles", 2, count,
        ) { Settings.twoRowTrackCard = it; Settings.save() }
        SettingSwitch(
            Settings.gradientColoring,
            "Gradient coloring", 2, count,
        ) { Settings.gradientColoring = it; Settings.save() }
        SettingSwitch(
            Settings.floatingNavBar,
            "Floating navigation bar", 2, count,
            desc = "Also changes mini player style"
        ) { Settings.floatingNavBar = it; Settings.save() }
        SegmentedListItem(
            ListItemDefaults.segmentedShapes(1, count),
            Modifier.padding(bottom = 3.dp),
            colors = ListItemDefaults.segmentedColors(
                MaterialTheme.colorScheme.surface
            ),
            supportingContent = {
                Column {
                    Spacer(Modifier.height(5.dp))
                    Row {
                        for (i in 0..4)
                            NavTab(i, Settings.initTab == i) { lastClickedNavTab = i }
                    }
                    Spacer(Modifier.height(5.dp))
                    Text("Click tabs to edit them")
                }
            }
        ) {
            Text("Navigation bar items")
        }
        SegmentedListItem(
            ListItemDefaults.segmentedShapes(7, count),
            Modifier.padding(bottom = 3.dp),
            colors = ListItemDefaults.segmentedColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
        ) {
            OutlinedTextField(
                Settings.tagsSpacer,
                {
                    Settings.tagsSpacer = it
                    Settings.save()
                },
                maxLines = 1,
                shape = OutlinedTextFieldDefaults.roundedShape,
                textStyle = TextStyle(
                    fontSize = 20.sp
                ),
                label = {
                    Text("Separator")
                }
            )
        }
        Text("To change app font, copy and rename any .ttf file into 0/Documents/.Crystal/UI Font.ttf")
    }

    if (lastClickedNavTab != -1) {
        EditNavigationItemPopup(lastClickedNavTab) {
            lastClickedNavTab = -1
        }
    }

}

@Composable
private fun FsSettings() {
    val count = 1
    val blurState = rememberSliderState(
        Settings.backgroundBlurRadius.toFloat(),
        trackRange = 0f..85f
    )
    Column(Modifier.padding(10.dp)) {
        FSPlayerUIPreview(Modifier.width(220.dp).align(Alignment.CenterHorizontally))
//        SettingSwitch(
//            Settings.whiteText,
//            "Force white texts", 0, count
//        ) { Settings.whiteText = it; Settings.save() }


        Spacer(Modifier.height(10.dp))
        Text("Style", fontSize = 28.sp)
        Spacer(Modifier.height(5.dp))
        Row {
            for (i in 0..1) {
                Column(
                    Modifier.weight(0.5f).padding(5.dp),
                    Arrangement.Center,
                    Alignment.CenterHorizontally
                ) {
                    Text(when(i) {
                        1 -> "Material"
                        else -> "Crystal"
                    })
                    RadioButton(
                        Settings.fsStyle == i,
                        { Settings.fsStyle = i; Settings.save() }
                    )
                }
            }
        }

        Spacer(Modifier.height(5.dp))
        SegmentedListItem(
            ListItemDefaults.segmentedShapes(0, count),
            Modifier.padding(bottom = 3.dp),
            colors = ListItemDefaults.segmentedColors(
                MaterialTheme.colorScheme.surface
            ),
            supportingContent = {
                Slider(
                    blurState,
                    onValueChange = {
                        blurState.value = it
                        Settings.backgroundBlurRadius = it.roundToInt()
                    },
                    onValueChangeFinished = {
                        Settings.save()
                    },
                    enabled = Settings.fsStyle == 0
                )
            },
        ) {
            Text("Background blur: ${Settings.backgroundBlurRadius}")
        }

        Spacer(Modifier.height(10.dp))
        Text("Artwork shape", fontSize = 28.sp)
        Spacer(Modifier.height(5.dp))
        Row {
            for (i in 0..4) {
                Column(
                    Modifier.weight(0.15f).padding(5.dp),
                    Arrangement.Center,
                    Alignment.CenterHorizontally
                ) {
                    Image(
                        painterResource(R.drawable.null_track_cover),
                        null,
                        Modifier.aspectRatio(1f)
                            .clip(
                                if (i == 1) RoundedCornerShape(18.dp)
                                else fullscreenCoverShape(i)
                            )
                            .clickable {
                                Settings.coverShapeMode = i; Settings.save()
                            },
                    )
                    RadioButton(
                        Settings.coverShapeMode == i,
                        { Settings.coverShapeMode = i; Settings.save() }
                    )
                }
            }
        }
    }
}

@Composable
private fun IndexingSettings() {
    val count = 2
    var showPathPicker by remember { mutableStateOf(false) }
    val minduState = rememberSliderState(
        Settings.minDuration.toFloat(),
        trackRange = 0f..120f
    )
    Column(Modifier.padding(10.dp)) {
        SettingSwitch(
            Settings.sortOrderDesc,
            "Descending sort order", 0, count
        ) { Settings.sortOrderDesc = it; Settings.save() }
        SegmentedListItem(
            ListItemDefaults.segmentedShapes(1, count),
            Modifier.padding(bottom = 3.dp),
            colors = ListItemDefaults.segmentedColors(
                MaterialTheme.colorScheme.surface
            ),
            supportingContent = {
                Slider(
                    minduState,
                    onValueChange = {
                        minduState.value = it
                        Settings.minDuration = it.roundToInt()
                    },
                    onValueChangeFinished = {
                        Settings.save()
                    },
                )
            },
        ) {
            Text("Min track duration: ${Settings.minDuration}")
        }
        Spacer(Modifier.height(10.dp))
        ListItem(
            trailingContent = {
                FilledIconButton({
                    showPathPicker = true
                }) { Icon(add_2, null) }
            },
            supportingContent = {
                Text("Includes subfolders too")
            },
            modifier = Modifier.clip(RoundedCornerShape(15.dp))
                .padding(bottom = 3.dp)
        ) { Text("Library exclude:") }

        Settings.libraryExclude.forEach {
            ListItem(
                trailingContent = {
                    FilledIconButton(
                        {
                            Settings.libraryExclude.remove(it)
                            Settings.save()
                        },
                        colors = IconButtonDefaults.filledIconButtonColors(
                            MaterialTheme.colorScheme.error,
                            MaterialTheme.colorScheme.onError
                        )
                    ) { Icon(delete_forever, null) }
                },
                modifier = Modifier.clip(RoundedCornerShape(25.dp))
                    .padding(bottom = 3.dp)
            ) { Text(it, fontSize = 12.sp) }
        }
    }

    if (showPathPicker) {
        AddPathPopup {
            showPathPicker = false
            if (it != null) {
                Settings.libraryExclude.add(it)
                Settings.save()
            }
        }
    }
}

@Composable
private fun AboutSection(activity: Activity) {
    val count = 6
    val context = LocalContext.current
    val cache = File(Settings.appCacheThumbsFolder)
    var size by remember { mutableFloatStateOf(0f) }
    var showFlags by remember { mutableStateOf(false) }

    fun getSize() : Float {
        var t = 0f
        cache.listFiles()?.forEach {
            t += it.length()
        }
        return  ((t / 1024f / 1024f) * 100f).roundToInt() / 100f
    }
    size = getSize()

    Column(Modifier.padding(10.dp)) {
        SettingSwitch(
            Settings.waveformDataCapture,
            "Enable waveform capture", 0, count,
            desc = "Required for effects like Edge lighting, Parallax & Wind. Requires microphone permission\n * Effects are currently under experimental features",
            enable = experimental
        ) {
            Settings.waveformDataCapture = it
            if (Settings.waveformDataCapture) {
                activity.setupVisu()
            }
            Settings.save()
        }
        EasySegmentedListItem(null, "Source code", 1, count) {
            openLink(context, "https://github.com/mb0028/CrystalMusic")
        }
        EasySegmentedListItem(null, "Developer (mb28)", 1, count) {
            openLink(context, "https://github.com/mb0028")
        }
        EasySegmentedListItem(null, "Bug report / feature request", 2, count) {
            openLink(context, "https://github.com/mb0028/CrystalMusic/issues/new")
        }
        EasySegmentedListItem(null, "Clear app cache (${size} mb)", 3, count) {
            val f = cache.listFiles()
            f?.forEach { it.delete() }
            Toast.makeText(context, "Cleared ${f?.count() ?: 0} files.",
                Toast.LENGTH_SHORT).show()
            size = getSize()
        }
        SettingSwitch(
            experimental,
            "Enable experimental features", count - 1, count
        ) { experimental = it; Settings.save() }
        if (experimental) {
            EasySegmentedListItem(flag, "Flags", 0, 1) {
                showFlags = true
            }
        }
//        EasySegmentedListItem(null, "Open source licenses", 4, count) {
//
//        }
    }

    if (showFlags) {
        FlagsPopup { showFlags = false }
    }
}

@Composable
private fun TopBar(activity: SettingsActivity) {
    TopAppBar(
        {
            Text("Settings")
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow.copy(0.9f)
        ),
        navigationIcon = {
            IconButton(
                { activity.finish() },
                colors = IconButtonDefaults.iconButtonColors(
                    MaterialTheme.colorScheme.surfaceContainerHigh
                ),
                modifier = Modifier.padding(horizontal = 15.dp)
            ) { Icon(arrow_back, null) }
        },
    )
}
