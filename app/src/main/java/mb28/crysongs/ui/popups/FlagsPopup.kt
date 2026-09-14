package mb28.crysongs.ui.popups

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import mb28.crysongs.core.Settings
import mb28.crysongs.ui.other.SettingSwitch

@Composable
fun FlagsPopup(onDismissRequired: () -> Unit) {
    AlertDialog(
        { onDismissRequired() },
        { },
        title = {
            Text("Flags")
        },
        text = {
            LazyColumn {
                item {
                    SettingSwitch(
                        Settings.gradientColoring,
                        "Gradient coloring", 0, 1
                    ) { Settings.gradientColoring = it; Settings.save() }
                }
            }
        }
    )
}