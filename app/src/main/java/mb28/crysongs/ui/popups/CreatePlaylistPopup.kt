package mb28.crysongs.ui.popups

import android.os.Environment
import android.widget.Toast
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import mb28.crysongs.core.Settings
import java.io.File

@Composable
fun CreatePlaylistPopup(onDismissRequired: () -> Unit) {
    val context = LocalContext.current
    val external = Environment.getExternalStorageDirectory().parent
    var plPath by remember { mutableStateOf(
        "0/Download") }
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