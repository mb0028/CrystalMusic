package mb28.crysongs.ui.other

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun CrystalDrawerItem(
    text: String,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    selected: Boolean = true,
    containerColor: Color = MaterialTheme.colorScheme.surfaceContainer,
    selectedContainerColor: Color = MaterialTheme.colorScheme.secondaryContainer,
    shape: Shape = RoundedCornerShape(15.dp),
    onClick: () -> Unit = {}
) {
    Row(
        modifier.fillMaxWidth().padding(8.dp, 3.dp)
            .background(
                if (selected) selectedContainerColor else containerColor,
                shape
            )
            .clip(shape)
            .clickable { onClick() },
        Arrangement.Start,
        Alignment.CenterVertically
    ) {
        if (icon != null) {
            Box(Modifier.padding(start = 15.dp)) {
                Icon(icon, text)
            }
        }
        Text(text, Modifier.padding(15.dp))
    }
}