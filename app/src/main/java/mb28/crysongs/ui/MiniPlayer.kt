package mb28.crysongs.ui

import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularWavyProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilledTonalToggleButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
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
import mb28.crysongs.core.formatDurationMs
import mb28.crysongs.duration
import mb28.crysongs.icons.swap_horiz
import mb28.crysongs.isPlaying
import mb28.crysongs.nowPlaying
import mb28.crysongs.nowPlayingCover
import mb28.crysongs.nowPlayingTags
import mb28.crysongs.playNextOrPrevious
import mb28.crysongs.player
import mb28.crysongs.position
import mb28.crysongs.ui.popups.TrackMoreOptionsPopup
import kotlin.time.Duration.Companion.milliseconds

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun MiniPlayer(secondSet: MutableState<Boolean>, context: Activity) {
    var showMoreOptions by remember { mutableStateOf(false) }
    val size = 0.52f
    val shape = RoundedPolygon.star(
        12,
        radius = size,
        innerRadius = 0.42f,
        centerX = size * 0.5f,
        centerY = size * 0.5f,
        rounding = CornerRounding(80f)
    ).toShape()

    Row (
        Modifier.padding(horizontal = 15.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        FilledTonalToggleButton(
            secondSet.value,
            { secondSet.value = !secondSet.value }
        ) {
            Icon(swap_horiz, null)
        }

        Row(
            Modifier
                .fillMaxWidth()
                .height(80.dp)
                .padding(start = 5.dp)
                .clip(CircleShape)
                .background(
                    MaterialTheme.colorScheme.inversePrimary.copy(0.9f),
                    shape = CircleShape
                )
                .clickable {
                    val intent = Intent(context, FullscreenPlayerActivity::class.java)
                    context.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(context).toBundle())
                },
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (nowPlaying != null) {
                Spacer(Modifier.width(5.dp))
                Image(
                    nowPlayingCover,
                    "Track cover",
                    contentScale = ContentScale.FillHeight,
                    modifier = Modifier.width(70.dp).height(70.dp).clip(shape)
                        .clickable { showMoreOptions = true }
                )

                Column(
                    Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(0.75f)
                        .padding(0.dp, 5.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        nowPlayingTags?.title ?: "Loading...",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Start,
                        modifier = Modifier.fillMaxWidth()
                    )
                    PlayPauseNextPrevious()
                }

                Box(
                    Modifier.padding(end = 5.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularWavyProgressIndicator(
                        progress = { position.toFloat() / duration },
                        amplitude = { if (isPlaying) 1f else 0.4f }
                    )
                    Text(
                        formatDurationMs(position.milliseconds),
                        fontSize = 13.sp
                    )
                }

            } else {
                Text("Play something", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
            }
        }
    }

    if (showMoreOptions) {
        TrackMoreOptionsPopup(nowPlaying!!) {
            showMoreOptions = false
        }
    }
}

@Composable
private fun PlayPauseNextPrevious() {
    Row {
        IconButton(
            {
                playNextOrPrevious(false)
            }
        ) {
            Icon(painterResource(R.drawable.skip_previous_24px), null)
        }
        IconButton(
            {
                if (player.isPlaying) {
                    player.pause()
                } else {
                    player.play()
                }
                isPlaying = player.isPlaying
            },
            modifier = Modifier.scale(1.5f)
        ) {
            Icon(if (isPlaying) painterResource(R.drawable.pause_24px)
                else painterResource(R.drawable.play_arrow_24px), null)
        }
        IconButton(
            {
                playNextOrPrevious()
            }
        ) {
            Icon(painterResource(R.drawable.skip_next_24px), null)
        }
    }
}
