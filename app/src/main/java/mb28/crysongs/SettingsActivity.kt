package mb28.crysongs

import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.plus
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.DropdownMenuPopup
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedListItem
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import mb28.crysongs.core.Settings
import mb28.crysongs.core.Settings.experimental
import mb28.crysongs.core.openLink
import mb28.crysongs.icons.arrow_back
import mb28.crysongs.icons.flag
import mb28.crysongs.ui.other.EasySegmentedListItem
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
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                    topBar = { TopBar(this) }
                ) { innerPadding ->
                    LazyColumn(
                        contentPadding = innerPadding.plus(PaddingValues(bottom = 300.dp))
                    ) {
                        item {
                            SectionHeader("UI Customization")
                            UISettings()
                        }
                        item {
                            SectionHeader("Fullscreen Player")
                            FsSettings()
                        }
                        item {
                            SectionHeader("Indexing")
                            IndexingSettings()
                        }
                        item {
                            SectionHeader("About")
                            AboutSection()
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun UISettings() {
    val count = 2
    Column(Modifier.padding(10.dp)) {
        SettingSwitch(
            Settings.useCoverColor,
            "Full adaptive coloring", 0, count,
            desc = "Use track cover accent color for UI if available"
        ) { Settings.useCoverColor = it; Settings.save() }

        SegmentedListItem(
            ListItemDefaults.segmentedShapes(1, count),
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
                    Text("Spacer")
                }
            )
        }
    }
}

@Composable
private fun FsSettings() {
    val count = 1
    var fsShapeExpanded by remember { mutableStateOf(false) }
    Column(Modifier.padding(10.dp)) {
        SettingSwitch(
            Settings.whiteText,
            "Force white texts", 0, count
        ) { Settings.whiteText = it; Settings.save() }
//        SegmentedListItem(
//            ListItemDefaults.segmentedShapes(1, count),
//            Modifier.padding(bottom = 3.dp),
//            colors = ListItemDefaults.segmentedColors(
//                containerColor = MaterialTheme.colorScheme.surface
//            ),
//        ) {
//            Column {
//                Text("Cover shape:")
//                DropdownMenuPopup(fsShapeExpanded, {fsShapeExpanded = false}) {
//                    DropdownMenuItem({ Text("Square") },
//                        { Settings.coverShapeMode = -1; Settings.save() })
//                    DropdownMenuItem({ Text("Rounded Square") },
//                        { Settings.coverShapeMode = 0; Settings.save() })
//                    DropdownMenuItem({ Text("Rounded Polygon 1") },
//                        { Settings.coverShapeMode = 1; Settings.save() })
//                    DropdownMenuItem({ Text("Rounded Polygon 2") },
//                        { Settings.coverShapeMode = 2; Settings.save() })
//                    DropdownMenuItem({ Text("Circle") },
//                        { Settings.coverShapeMode = 3; Settings.save() })
//                }
//                Text("Note: you can switch between shapes by clicking the cover in fullscreen player")
//            }
//        }
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
private fun AboutSection() {
    val count = 5
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
        EasySegmentedListItem(null, "Source code", 0, count) {
            openLink(context, "https://github.com/mb0028/CrystalMusic")
        }
        EasySegmentedListItem(null, "Developer (mb28)", 1, count) {
            openLink(context, "https://github.com/mb0028")
        }
        EasySegmentedListItem(null, "Bug report / feature request", 2, count) {
            openLink(context, "https://github.com/mb0028/CrystalMusic/issues/new")
        }
        EasySegmentedListItem(null, "Clear covers cache (${size} mb)", 3, count) {
            val f = cache.listFiles()
            f?.forEach { it.delete() }
            Toast.makeText(context, "Cleared ${f?.count() ?: 0} files.",
                Toast.LENGTH_SHORT).show()
            size = getSize()
        }
        SettingSwitch(
            experimental,
            "Enable experimental features", 4, count
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
private fun SectionHeader(text: String) {
    Text(
        text, fontSize = 26.sp,
        modifier = Modifier.padding(start = 20.dp, top = 10.dp)
    )
}

@Composable
private fun TopBar(activity: SettingsActivity) {
    TopAppBar(
        {
            Text("Settings")
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow.copy(
                alpha = 0.9f
            )
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
