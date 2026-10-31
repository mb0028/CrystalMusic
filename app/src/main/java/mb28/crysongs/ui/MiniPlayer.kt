package mb28.crysongs.ui

import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenuPopup
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.fastRoundToInt
import mb28.crysongs.FullscreenPlayerActivity
import mb28.crysongs.R
import mb28.crysongs.canChangeTrack
import mb28.crysongs.core.Settings
import mb28.crysongs.duration
import mb28.crysongs.isPlaying
import mb28.crysongs.nowPlaying
import mb28.crysongs.nowPlayingCover
import mb28.crysongs.nowPlayingTags
import mb28.crysongs.playNextOrPrevious
import mb28.crysongs.player
import mb28.crysongs.playerQuery
import mb28.crysongs.position
import mb28.crysongs.requestBlur
import mb28.crysongs.ui.popups.TrackMoreOptionsPopup

const val REQUEST_CODE_FS_PLAYER = 42
const val FS_RESULT_OPEN_ARTIST = 43
const val FS_RESULT_OPEN_ALBUM = 44
const val EXTRA_ARTIST_ALBUM = "EXTRA_ARTIST_ALBUM"

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun MiniPlayer(context: Activity) {
    var showMoreOptions by remember { mutableStateOf(false) }
    val iconsShape = RoundedCornerShape(15.dp)
    val bgShape = if (Settings.hideNavBar) iconsShape else
        RoundedCornerShape(15.dp, 15.dp ,0.dp, 0.dp)
    val color = MaterialTheme.colorScheme.surfaceContainer.copy(0.95f)

    var itemsOffset by remember { mutableStateOf(Offset.Zero) }
    var openFsAnimPlaying by remember { mutableStateOf(false) }
    val itemsHPositionAnim by animateIntAsState(itemsOffset.x.fastRoundToInt())
    val itemsVPositionAnim by animateFloatAsState(if (openFsAnimPlaying) 0.3f else 1f)
    val miniPlayerHeightAnim by animateDpAsState(if (Settings.dismissMiniPlayer) 0.dp else 75.dp)

    Column(
        Modifier
            .fillMaxWidth()
            .height(miniPlayerHeightAnim)
            .clip(bgShape)
            .padding(horizontal = if (Settings.hideNavBar) 10.dp else 0.dp)
            .background(
                Brush.horizontalGradient(
                    0f to if (Settings.gradientColoring)
                        MaterialTheme.colorScheme.secondaryContainer else color,
                    1f to color
                ),
                shape = bgShape
            )
            .clickable {
                val intent = Intent(context, FullscreenPlayerActivity::class.java)
                context.startActivityForResult(intent, REQUEST_CODE_FS_PLAYER,
                    ActivityOptions.makeSceneTransitionAnimation(context).toBundle())
            }
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragCancel = {
                        itemsOffset = Offset.Zero
                        openFsAnimPlaying = false
                    },
                    onDragEnd = {
                        if (itemsOffset.x > 150) {
                            if (playerQuery.isNotEmpty() && canChangeTrack) {
                                playNextOrPrevious(false)
                            }
                        } else if (itemsOffset.x < -150) {
                            if (playerQuery.isNotEmpty() && canChangeTrack) {
                                playNextOrPrevious()
                            }
                        } else if (itemsOffset.y < -100) {
                            val intent = Intent(context, FullscreenPlayerActivity::class.java)
                            context.startActivityForResult(intent, REQUEST_CODE_FS_PLAYER,
                                ActivityOptions.makeSceneTransitionAnimation(context).toBundle())
                        } else if (itemsOffset.y > 100) {
                            Settings.dismissMiniPlayer = true
                        }
                        itemsOffset = Offset.Zero
                        openFsAnimPlaying = false
                    }
                ) { _, dragAmount ->
                    itemsOffset += dragAmount
                    openFsAnimPlaying = itemsOffset.x > -150 && itemsOffset.x < 150 && itemsOffset.y < -100
                }
            },
        Arrangement.SpaceBetween,
        Alignment.CenterHorizontally
    ) {
        Row(
            Modifier.fillMaxWidth().height(70.dp)
                .blur(((1 - itemsVPositionAnim) * 6).dp,
                    BlurredEdgeTreatment.Unbounded)
                .alpha(itemsVPositionAnim)
                .offset {
                    IntOffset(itemsHPositionAnim, -((1 - itemsVPositionAnim) * 60).fastRoundToInt())
                },
            Arrangement.SpaceBetween,
            Alignment.CenterVertically
        ) {
            Box {
                Image(
                    nowPlayingCover,
                    null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.aspectRatio(1f).padding(8.dp).clip(iconsShape)
                        .clickable {
                            if (nowPlaying != null) {
                                showMoreOptions = true; requestBlur = true
                            }
                        }
                )
                DropdownMenuPopup(
                    nowPlaying != null && showMoreOptions,
                    { showMoreOptions = false; requestBlur = false }
                ) {
                    TrackMoreOptionsPopup(nowPlaying!!, nowPlayingTags?.title)
                }
            }

            Text(
                nowPlayingTags?.title ?: "Crystal Songs",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(0.7f),
                fontSize = 16.sp
            )

            FilledIconButton(
                { if (player.isPlaying) player.pause() else player.play() },
                shape = iconsShape,
                modifier = Modifier.aspectRatio(1f).padding(8.dp),
                enabled = nowPlaying != null
            ) {
                Icon(
                    painterResource(if (isPlaying) R.drawable.pause_24px else R.drawable.play_arrow_24px),
                    null
                )
            }
        }

        LinearProgressIndicator(
            { position.toFloat() / duration },
            Modifier.fillMaxWidth()
        )
    }
}
