package mb28.crysongs.ui

import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.graphics.shapes.CornerRounding
import androidx.graphics.shapes.RoundedPolygon
import androidx.graphics.shapes.star
import mb28.crysongs.FullscreenPlayerActivity
import mb28.crysongs.R
import mb28.crysongs.canChangeTrack
import mb28.crysongs.core.Settings
import mb28.crysongs.core.formatDurationMs
import mb28.crysongs.duration
import mb28.crysongs.isPlaying
import mb28.crysongs.nowPlaying
import mb28.crysongs.nowPlayingCover
import mb28.crysongs.nowPlayingTags
import mb28.crysongs.playNextOrPrevious
import mb28.crysongs.player
import mb28.crysongs.playerQuery
import mb28.crysongs.position
import mb28.crysongs.ui.popups.TrackMoreOptionsPopup
import kotlin.time.Duration.Companion.milliseconds

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun MiniPlayer(context: Activity) {
    var showMoreOptions by remember { mutableStateOf(false) }
    val size = 0.52f
    val coverShape = RoundedPolygon.star(
        12,
        radius = size,
        innerRadius = 0.42f,
        centerX = size * 0.5f,
        centerY = size * 0.5f,
        rounding = CornerRounding(80f)
    ).toShape()
    val shape = if (Settings.floatingNavBar) CircleShape else RoundedCornerShape(15.dp)

    Row(
        Modifier
            .fillMaxWidth()
            .height(80.dp)
            .padding(horizontal = 10.dp)
            .clip(shape)
            .background(
                if (Settings.floatingNavBar) MaterialTheme.colorScheme.inversePrimary.copy(0.9f)
                    else MaterialTheme.colorScheme.surfaceContainerHigh,
                shape = shape
            )
            .clickable {
                val intent = Intent(context, FullscreenPlayerActivity::class.java)
                context.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(context).toBundle())
            },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            nowPlayingCover,
            "Track cover",
            contentScale = ContentScale.FillHeight,
            modifier = Modifier.aspectRatio(1f).padding(3.dp).clip(coverShape)
                .clickable { if (nowPlaying != null) showMoreOptions = true }
        )

        Column(
            Modifier
                .fillMaxHeight()
                .fillMaxWidth(0.70f)
                .padding(0.dp, 5.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                nowPlayingTags?.title ?: "Play something...",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
                fontSize = 16.sp
            )

            Row(Modifier.height(40.dp), verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    {
                        playNextOrPrevious(false)
                    },
                    enabled = playerQuery.isNotEmpty() && canChangeTrack,
                ) {
                    Icon(painterResource(R.drawable.skip_previous_24px), null)
                }
                Text(
                    formatDurationMs(position.milliseconds) + " / " + formatDurationMs(duration.milliseconds),
                    fontSize = 14.sp
                )
                IconButton(
                    {
                        playNextOrPrevious()
                    },
                    enabled = playerQuery.isNotEmpty() && canChangeTrack,
                ) {
                    Icon(painterResource(R.drawable.skip_next_24px), null)
                }
            }

            LinearProgressIndicator(
                { position.toFloat() / duration }
            )
        }

        FilledIconButton(
            { if (player.isPlaying) player.pause() else player.play() },
            shape = shape,
            modifier = Modifier.aspectRatio(1f).padding(10.dp),
            enabled = nowPlaying != null
        ) {
            Icon(
                painterResource(if (isPlaying) R.drawable.pause_24px else R.drawable.play_arrow_24px),
                null
            )
        }
    }

    if (showMoreOptions) {
        TrackMoreOptionsPopup(nowPlaying!!, nowPlayingTags?.title) {
            showMoreOptions = false
        }
    }
}
