package mb28.crysongs.ui.popups

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
import androidx.compose.ui.unit.dp

@Composable
fun RenamePlaylistPopup(ogName: String, onDismissRequired: (String, Boolean) -> Unit) {
    var name by remember { mutableStateOf(ogName) }
    AlertDialog(
        { onDismissRequired(name, false) },
        {
            Button(
                {
                    onDismissRequired(name, true)
                },
            ) {
                Text("Rename")
            }
        },
        dismissButton = {
            OutlinedButton({ onDismissRequired(name, false) }) {
                Text("Cancel")
            }
        },
        title = {
            Text("Rename playlist")
        },
        text = {
            LazyColumn {
                item {
                    OutlinedTextField(
                        name,
                        {
                            name = it
                        },
                        label = {
                            Text("Name")
                        },
                        isError = false,
                        shape = RoundedCornerShape(15.dp)
                    )
                    Spacer(Modifier.height(5.dp))
                }
            }
        }
    )
}