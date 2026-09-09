package mb28.crysongs.ui.other

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.ListItemShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedListItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import mb28.crysongs.core.Settings.tagsSpacer
import mb28.crysongs.core.Track
import mb28.crysongs.core.formatDurationMs
import mb28.crysongs.noCoverBitmap
import mb28.crysongs.nowPlaying
import mb28.crysongs.setAndPlay
import mb28.crysongs.ui.popups.TrackMoreOptionsPopup
import kotlin.math.roundToInt
import kotlin.time.Duration.Companion.milliseconds

private val topShape = RoundedCornerShape(topStart = 25.dp, topEnd = 25.dp, bottomStart = 12.dp, bottomEnd = 12.dp)
private val defaultShape = RoundedCornerShape(12.dp)
private val defaultPressedShape = RoundedCornerShape(25.dp)
private val endShape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp, bottomStart = 25.dp, bottomEnd = 25.dp)

@Composable
fun TrackTile(
    t: Track,
    index: Int,
    count: Int,
    resetQueryOnClick: Boolean = true,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier,
    onBeforeClick: () -> Unit = {}
) {
    var showMoreOptions by remember { mutableStateOf(false) }
    val state = remember {
        MutableTransitionState(false).apply {
            targetState = true
        }
    }
    val shape = when {
        count == 1 -> defaultShape
        index == 0 -> topShape
        index == count - 1 -> endShape
        else -> defaultShape
    }
    AnimatedVisibility(
        visibleState = state,
        enter = scaleIn(initialScale = 0.65f) + fadeIn(initialAlpha = 0.1f),
    ) {
        SegmentedListItem(
            shapes = ListItemShapes(
                shape,
                defaultShape,
                defaultPressedShape,
                defaultPressedShape,
                defaultPressedShape,
                defaultShape
            ),
            colors = ListItemDefaults.segmentedColors(
                containerColor = if (t == nowPlaying) MaterialTheme.colorScheme.tertiaryContainer
                else MaterialTheme.colorScheme.surfaceContainerLowest
            ),
            modifier = modifier
                .padding(bottom = 5.dp)
                .padding(horizontal = 10.dp)
                .height(82.dp),
//            .background(
//                Brush.horizontalGradient(
//                    listOf(
//                        if (t == nowPlaying) MaterialTheme.colorScheme.tertiaryContainer
//                        else MaterialTheme.colorScheme.surfaceContainerLowest,
//                        MaterialTheme.colorScheme.surfaceBright,
//                    )
//                )
//            ),
            contentPadding = PaddingValues(5.dp),
            leadingContent = {
                val coverPath = Track.createOrGetThumbnail(t.path)
                val cover = if (coverPath == null) noCoverBitmap!!
                else BitmapFactory.decodeFile(coverPath).asImageBitmap()
                Image(
                    cover,
                    "Track cover",
                    contentScale = ContentScale.FillHeight,
                    modifier = Modifier
                        .size(73.dp, 73.dp)
                        .clickable { showMoreOptions = true }
                        .clip(defaultPressedShape)
                )
            },
            onClick = {
                onBeforeClick()
                setAndPlay(t, resetQueryOnClick)
            }
        ) {
            Column(
                Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    t.title, maxLines = 1, overflow = TextOverflow.Ellipsis, fontSize = 18.sp
                )

                Row(
                    Modifier.fillMaxWidth(),
                ) {
                    Text(
                        t.artist,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontSize = 13.sp,
                        modifier = Modifier.fillMaxWidth(0.4f)
                    )
                    Text(
                        tagsSpacer, fontSize = 18.sp,
                        modifier = Modifier.fillMaxWidth(0.2f)
                    )
                    Text(
                        t.album,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontSize = 13.sp,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                val dura = formatDurationMs(t.duration.milliseconds)
                val bitrate = (t.bitrate / 1000f).roundToInt()
                Text(
                    "$dura${tagsSpacer}${bitrate} kbps${tagsSpacer}${t.year}${tagsSpacer}${t.genre}" +
                            if (t.hasLRC) "${tagsSpacer}LRC" else "",
                    maxLines = 1, overflow = TextOverflow.Ellipsis, fontSize = 13.sp,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }

    if (showMoreOptions) {
        TrackMoreOptionsPopup(t) {
            showMoreOptions = false
        }
    }
}