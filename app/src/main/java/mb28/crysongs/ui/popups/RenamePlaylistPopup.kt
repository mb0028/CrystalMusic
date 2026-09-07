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
fun RenamePlaylistPopup(onDismissRequired: () -> Unit) {
    AlertDialog(
        { onDismissRequired() },
        {
            Button(
                {

                },
            ) {
                Text("Rename")
            }
        },
        dismissButton = {
            OutlinedButton({ onDismissRequired() }) {
                Text("Cancel")
            }
        },
        title = {
            Text("Rename playlist")
        },
        text = {
            LazyColumn {
                item {
//                    OutlinedTextField(
//                        aaa,
//                        {
//                        },
//                        label = {
//                            Text("Name")
//                        },
//                        isError = false,
//                        shape = RoundedCornerShape(15.dp)
//                    )
//                    Spacer(Modifier.height(5.dp))
                }
            }
        }
    )
}