package mb28.crysongs.ui

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import mb28.crysongs.MainActivity
import mb28.crysongs.MainContent
import mb28.crysongs.core.Settings
import mb28.crysongs.nowPlayingTags
import mb28.crysongs.privateNowPlayingCover
import mb28.crysongs.ui.fullscreen_player.FSCover
import mb28.crysongs.ui.fullscreen_player.FSLyricsTab
import mb28.crysongs.ui.fullscreen_player.FSPlayerButtonsRow
import mb28.crysongs.ui.fullscreen_player.FSProgressBarRow

@Composable
fun LandscapePage(paddingValues: PaddingValues, activity: MainActivity) {
    Box(Modifier.fillMaxSize()) {
        if (privateNowPlayingCover != null) {
            Image(
                (privateNowPlayingCover as Bitmap).asImageBitmap(), null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .blur(Settings.backgroundBlurRadius.dp)
                    .alpha(0.5f),
            )
        }

        Row(
            Modifier.padding(
                start = paddingValues.calculateStartPadding(LayoutDirection.Ltr),
                end = paddingValues.calculateEndPadding(LayoutDirection.Ltr)
            ),
            Arrangement.Center,
            Alignment.CenterVertically
        ) {
            Spacer(Modifier.width(5.dp))
            Box(Modifier.weight(0.48f)) {
                activity.MainContent()
            }

            Column(
                Modifier.weight(0.52f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                FSLyricsTab(Modifier.fillMaxSize(), false)
            }
        }
    }
}
