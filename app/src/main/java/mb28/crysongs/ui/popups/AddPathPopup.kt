package mb28.crysongs.ui.popups

import android.os.Environment
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
import androidx.compose.ui.unit.dp

@Composable
fun AddPathPopup(onDismissRequired: (path: String?) -> Unit) {
    val external = Environment.getExternalStorageDirectory().parent
    var path by remember { mutableStateOf("0/Download") }
    AlertDialog(
        { onDismissRequired(null) },
        {
            Button(
                {
                   onDismissRequired("$external/$path")
                },
            ) {
                Text("Ok")
            }
        },
        dismissButton = {
            OutlinedButton({ onDismissRequired(null) }) {
                Text("Cancel")
            }
        },
        title = {
            Text("Enter Path")
        },
        text = {
            LazyColumn {
                item {
                    OutlinedTextField(
                        path,
                        {
                            path = it
                        },
                        label = {
                            Text("Path")
                        },
                        supportingText = {
                            Text("No need to include '$external'")
                        },
                        shape = RoundedCornerShape(15.dp)
                    )
                }
            }
        }
    )
}
