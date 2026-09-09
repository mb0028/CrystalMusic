package mb28.crysongs.ui.fullscreen_player

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import mb28.crysongs.core.Settings
import mb28.crysongs.icons.align_justify_flex_end
import mb28.crysongs.icons.arrow_cool_down
import mb28.crysongs.icons.list_2
import mb28.crysongs.icons.sound_detection_loud_sound
import mb28.crysongs.player
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FSChangePageRow(modifier: Modifier = Modifier, selectedTab: PagerState, activity: Activity) {
    var showVolSheet by rememberSaveable { mutableStateOf(false) }
    val tabs = listOf("Details", "Player", "Lyrics")
    Row(
        modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp),
        Arrangement.SpaceBetween,
        Alignment.CenterVertically
    ) {
        IconButton(
            { activity.finish() }
        ) {
            Icon(arrow_cool_down, null)
        }

        Row(
            Modifier
                .size(270.dp, 40.dp)
                .background(
                    MaterialTheme.colorScheme.surfaceContainerHigh.copy(0.5f),
                    RoundedCornerShape(35.dp)
                ),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            tabs.forEachIndexed { i, tab ->
                Surface(
                    onClick = {selectedTab.requestScrollToPage(i)},
                    modifier = Modifier.size(80.dp, 30.dp),
                    color = if (selectedTab.currentPage == i) MaterialTheme.colorScheme.secondaryContainer
                    else MaterialTheme.colorScheme.surface.copy(0.5f),
                    shape = RoundedCornerShape(35.dp)
                ) {
                    Text(
                        tab,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 2.dp)
                    )
                }
            }
        }

        if (selectedTab.currentPage == 2) {
            IconButton(
                {
                    Settings.verticalLyrics = !Settings.verticalLyrics
                    Settings.save()
                }
            ) {
                Icon(
                    if (Settings.verticalLyrics) align_justify_flex_end else list_2,
                    null
                )
            }
        } else {
            IconButton(
                { showVolSheet = true }
            ) {
                Icon(sound_detection_loud_sound, null)
            }
        }

    }

    if (showVolSheet) {
        ModalBottomSheet(
            {
                showVolSheet = false
            }
        ) {
            Text("Volume: ${(Settings.appVolume * 100f).roundToInt() / 100f}", modifier = Modifier.padding(horizontal = 25.dp))
            Slider(
                Settings.appVolume,
                {
                    Settings.appVolume = it
                    player.setVolume(Settings.appVolume, Settings.appVolume)
                    Settings.save()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 15.dp)
            )
        }
    }
}