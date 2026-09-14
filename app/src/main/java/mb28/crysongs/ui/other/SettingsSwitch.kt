package mb28.crysongs.ui.other

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedListItem
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SettingSwitch(checked: Boolean, label: String, i: Int, count: Int,
                  enable: Boolean = true, desc: String = "", onChanged: (v: Boolean) -> Unit = {}) {
    SegmentedListItem(
        ListItemDefaults.segmentedShapes(i, count),
        Modifier.padding(bottom = 3.dp),
        colors = ListItemDefaults.segmentedColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        supportingContent = {
            if (desc.isNotEmpty()) {
                Text(desc, fontSize = 14.sp)
            }
        }
    ) {
        Row(
            Modifier.fillMaxWidth(),
            Arrangement.SpaceBetween,
            Alignment.CenterVertically
        ) {
            Text(label)
            Switch(checked, onChanged, enabled = enable)
        }

    }
}