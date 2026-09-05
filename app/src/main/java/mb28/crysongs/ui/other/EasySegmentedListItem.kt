package mb28.crysongs.ui.other

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun EasySegmentedListItem(
    icon: ImageVector?,
    text: String,
    index: Int,
    count: Int,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    SegmentedListItem(
        onClick = onClick,
        modifier = modifier.padding(bottom = if(index == count - 1) 0.dp else 2.dp),
        verticalAlignment = Alignment.CenterVertically,
        colors = ListItemDefaults.segmentedColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow.copy(alpha = 0.8f)
        ),
        trailingContent = {
            if (icon != null) {
                Card(
                    colors = CardDefaults.cardColors().copy(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                    ),
                    modifier = Modifier.height(55.dp).width(55.dp)
                ) {
                    Icon(
                        icon,
                        null,
                        Modifier.fillMaxSize().padding(12.dp)
                    )
                }
            }
        },
        shapes = ListItemDefaults.segmentedShapes(index, count)
    ) {
        Text(
            text,
            modifier = Modifier.padding(start = 6.dp)
        )
    }
}
