package mb28.crysongs

import android.app.Activity
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.plus
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.PrimaryScrollableTabRow
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import mb28.crysongs.core.Settings
import mb28.crysongs.core.Settings.experimental
import mb28.crysongs.core.openLink
import mb28.crysongs.icons.arrow_back
import mb28.crysongs.icons.flag
import mb28.crysongs.ui.other.EasySegmentedListItem
import mb28.crysongs.ui.other.EditNavigationItemPopup
import mb28.crysongs.ui.other.NavTab
import mb28.crysongs.ui.other.SettingSwitch
import mb28.crysongs.ui.popups.FlagsPopup
import mb28.crysongs.ui.theme.CrySongsTheme
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
    val count = 7
    Column(Modifier.padding(10.dp)) {
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
        SegmentedListItem(
            ListItemDefaults.segmentedShapes(1, count),
            Modifier.padding(bottom = 3.dp),
            colors = ListItemDefaults.segmentedColors(
                MaterialTheme.colorScheme.surface
            ),
            supportingContent = {
                Column {
                    Row {
                        for (i in 0..4)
                            NavTab(i, Settings.initTab == i) { lastClickedNavTab = i }
                    }
                    Row {
                        for (i in 5..9)
                            NavTab(i, Settings.initTab == i) { lastClickedNavTab = i }
                    }
                    Text("Click to edit items")
                }
            }
        ) {
            Text("Navigation bar items")
        }
        SegmentedListItem(
            ListItemDefaults.segmentedShapes(6, count),
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
    val count = 2
    val blurState = rememberSliderState(
        Settings.backgroundBlurRadius.toFloat(),
        trackRange = 0f..85f
    )
    Column(Modifier.padding(10.dp)) {
        SettingSwitch(
            Settings.whiteText,
            "Force white texts", 0, count
        ) { Settings.whiteText = it; Settings.save() }
        SegmentedListItem(
            ListItemDefaults.segmentedShapes(1, count),
            Modifier.padding(bottom = 3.dp),
            colors = ListItemDefaults.segmentedColors(
                MaterialTheme.colorScheme.surface
            ),
            overlineContent = {
                Text(
                    "Background blur: ${Settings.backgroundBlurRadius}",
                    fontSize = 16.sp
                )
            },
            trailingContent = {
                Image(
                    painterResource(R.drawable.widget_turntable_preview),
                    null,
                    Modifier
                        .size(80.dp)
                        .blur(Settings.backgroundBlurRadius.dp)

                )
            }
        ) {
            Slider(
                blurState,
                onValueChange = {
                    blurState.value = it
                    Settings.backgroundBlurRadius = it.roundToInt()
                },
                onValueChangeFinished = {
                    Settings.save()
                }
            )
        }
        Text("Tip: you can switch between cover shapes by clicking the cover in fullscreen player")
    }
}

@Composable
private fun IndexingSettings() {
    val count = 1
    Column(Modifier.padding(10.dp)) {
        SettingSwitch(
            Settings.sortOrderDesc,
            "Descending sort order", 0, count
        ) { Settings.sortOrderDesc = it; Settings.save() }
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
                colors = IconButtonDefaults.iconButtonColors().copy(
                    MaterialTheme.colorScheme.surfaceContainerHigh
                ),
                modifier = Modifier.padding(horizontal = 15.dp)
            ) {
                Icon(
                    arrow_back,
                    contentDescription = null
                )
            }
        },
    )
}
