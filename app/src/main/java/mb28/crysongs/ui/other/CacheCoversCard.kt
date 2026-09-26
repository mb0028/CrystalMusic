package mb28.crysongs.ui.other

import android.content.Intent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Card
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import mb28.crysongs.CacheCoversActivity
import mb28.crysongs.core.Settings
import mb28.crysongs.icons.arrow_back

@Composable
fun CacheCoversCard(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    Card(modifier) {
        ListItem(
            colors = ListItemDefaults.colors(
                MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.secondary
            ),
            supportingContent = {
                Column {
                    Text("Caching all artworks makes app 10 times faster " +
                        "Might takes a few minutes to complete.")
                    Spacer(Modifier.height(5.dp))
                    FilledTonalButton({
                        context.startActivity(Intent(context, CacheCoversActivity::class.java)
                            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
                    }) {
                        Text("Start")
                    }
                }
            },
            trailingContent = {
                IconButton({
                    Settings.tips_cacheThumbs = false
                    Settings.save()
                }) {
                    Icon(arrow_back, null)
                }
            },
        ) {
           Text("Cache all artworks?")
        }
    }
}