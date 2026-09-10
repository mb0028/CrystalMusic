package mb28.crysongs.ui.popups

import android.os.Environment
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ContainedLoadingIndicator
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import mb28.crysongs.core.Settings
import mb28.crysongs.ui.other.EasySegmentedListItem
import java.io.File
import java.io.FileFilter
import java.io.FilenameFilter
import java.nio.file.Path
import kotlin.io.path.listDirectoryEntries

@Composable
fun CreatePlaylistPopup(onDismissRequired: () -> Unit) {
    val context = LocalContext.current
    val external = Environment.getExternalStorageDirectory().parent
    var plPath by remember { mutableStateOf("0/Download") }
    var plName by remember { mutableStateOf("New playlist") }
    var pathExists by remember { mutableStateOf(true) }
    AlertDialog(
        { onDismissRequired() },
        {
            Button(
                {
                    val file = File("$external/$plPath/$plName.crym3u")
                    if (!file.exists()) {
                        file.createNewFile()
                        file.writeText("Name -> $plName")
                        Settings.playlists.add(file.path)
                        Settings.save()
                        onDismissRequired()
                    } else {
                        Toast.makeText(context, "$plName already exists :(", Toast.LENGTH_SHORT).show()
                    }
                },
                enabled = pathExists
            ) {
                Text("Create")
            }
        },
        dismissButton = {
            OutlinedButton({ onDismissRequired() }) {
                Text("Cancel")
            }
        },
        title = {
            Text("Create playlist")
        },
        text = {
            LazyColumn {
                item {
                    OutlinedTextField(
                        plPath,
                        {
                            plPath = it
                            pathExists = File("$external/$plPath").exists()
                        },
                        label = {
                            Text("Path")
                        },
                        isError = !pathExists,
                        shape = RoundedCornerShape(15.dp)
                    )
                    Spacer(Modifier.height(5.dp))
                    OutlinedTextField(
                        plName,
                        { plName = it },
                        label = {
                            Text("Playlist name")
                        },
                        shape = RoundedCornerShape(15.dp)
                    )
                    Spacer(Modifier.height(5.dp))
                    Text(
                        if (pathExists) "Full path: $external/$plPath/$plName.crym3u"
                        else "🔴 Folder doesn't exist :("
                    )
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ImportPlaylistPopup(onDismissRequired: () -> Unit) {
    val context = LocalContext.current
    var isSearching by remember { mutableStateOf(true) }
    val files = remember { mutableStateListOf<String>() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(scope) {
        scope.launch(Dispatchers.IO) {
            File(Environment.getExternalStorageDirectory().path).walkTopDown().forEach {
                if (it.path.endsWith(".crym3u")) {
                    files.add(it.path)
                }
            }
            isSearching = false
        }
    }

    AlertDialog(
        { onDismissRequired() },
        { },
        dismissButton = {
            OutlinedButton({ onDismissRequired() }) {
                Text("Cancel")
            }
        },
        title = {
            Text("Import playlist")
        },
        text = {
            LazyColumn(Modifier.heightIn(50.dp, 400.dp)) {
                val c = files.count()
                items(c) {
                    EasySegmentedListItem(
                        icon = null,
                        text = files[it].removePrefix("/storage/emulated/"),
                        index = it, count = c
                    ) {
                        if (Settings.playlists.contains(files[it])) {
                            Toast.makeText(context, "Playlist already added!", Toast.LENGTH_SHORT).show()
                        } else {
                            Settings.playlists.add(files[it])
                            Settings.save()
                            Toast.makeText(context, "Added!", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                if (isSearching) {
                    item {
                        Box(
                            Modifier.fillMaxWidth().padding(vertical = 10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            ContainedLoadingIndicator()
                        }
                    }
                } else if (files.isEmpty()) {
                    item {
                        Box(
                            Modifier.fillMaxWidth().padding(vertical = 10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("No .crym3u file found :(")
                        }
                    }
                }
            }
        }
    )
}
