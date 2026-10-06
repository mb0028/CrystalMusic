package mb28.crysongs.ui.more_pages

import android.app.Activity
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import mb28.crysongs.EXTRA_PATH
import mb28.crysongs.LrcMakerActivity
import mb28.crysongs.SoundPickerActivity
import mb28.crysongs.core.pageAnimation
import mb28.crysongs.ui.other.CrystalDrawerItem

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ToolsPage(activity: Activity) {
    val state = remember { MutableTransitionState(false).apply { targetState = true } }
    val result = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {  activityResult ->
        if (activityResult.resultCode == Activity.RESULT_OK) {
            val intent = Intent(activity, LrcMakerActivity::class.java)
                .putExtra(EXTRA_PATH, activityResult.data!!.getStringExtra(EXTRA_PATH))
            activity.startActivity(intent)
        }
    }

    AnimatedVisibility(
        visibleState = state,
        enter = pageAnimation,
    ) {
        LazyColumn(
            contentPadding = PaddingValues(top = 100.dp, bottom = 260.dp),
        ) {
            item {
                Text(
                    "Tools", fontSize = 36.sp, textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(45.dp))
            }

            item {
                OutlinedCard(
                    {
                        val intent = Intent(activity, SoundPickerActivity::class.java)
                            .putExtra(Intent.EXTRA_TITLE, "Select track to make lrc for")
                        result.launch(intent)
                    },
                    Modifier.fillMaxWidth().height(180.dp).padding(10.dp)
                        .background(
                            Brush.linearGradient(
                                0f to MaterialTheme.colorScheme.surfaceContainerLowest,
                                1f to MaterialTheme.colorScheme.primaryContainer
                            ),
                            CardDefaults.outlinedShape
                        ),
                    colors = CardDefaults.outlinedCardColors(
                        Color.Transparent
                    )
                ) {
                    Box(Modifier.fillMaxSize().padding(10.dp)) {
                        Text(
                            "LRC Maker",
                            fontSize = 36.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            "Create synced lrc from plain text\nwith just button clicks",
                            Modifier.align(Alignment.BottomEnd),
                            textAlign = TextAlign.End
                        )
                    }
                }
            }
        }
    }
}
