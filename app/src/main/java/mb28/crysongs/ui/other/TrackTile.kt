package mb28.crysongs.ui.other

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mb28.crysongs.core.Settings
import mb28.crysongs.core.Settings.tagsSpacer
import mb28.crysongs.core.Track
import mb28.crysongs.core.formatDurationMs
import mb28.crysongs.icons.favorite
import mb28.crysongs.icons.heart_plus
import mb28.crysongs.noCoverBitmap
import mb28.crysongs.nowPlaying
import mb28.crysongs.setAndPlay
import mb28.crysongs.ui.popups.TrackMoreOptionsPopup
import kotlin.math.roundToInt
import kotlin.time.Duration.Companion.milliseconds

private val roundnessMin = 7.dp
private val roundnessMax = 18.dp

private val topShape = RoundedCornerShape(roundnessMax, roundnessMax, roundnessMin, roundnessMin)
private val defaultShape = RoundedCornerShape(roundnessMin)
private val defaultPressedShape = RoundedCornerShape(roundnessMax)
private val endShape = RoundedCornerShape(roundnessMin, roundnessMin, roundnessMax, roundnessMax)

@Composable
@SuppressLint("ModifierParameter")
fun TrackTile(
    path: String,
    index: Int,
    count: Int,
    resetQueryOnClick: Boolean = true,
    modifier: Modifier = Modifier,
    onBeforeClick: () -> Unit = {}
) {
    var showMoreOptions by remember { mutableStateOf(false) }
    val state = remember { MutableTransitionState(false).apply { targetState = true }}
    val context = LocalContext.current
    var t: Track? by remember { mutableStateOf(null) }
    var cover: ImageBitmap? by remember { mutableStateOf(null) }
    val shape = when {
        count == 1 -> defaultShape
        index == 0 -> topShape
        index == count - 1 -> endShape
        else -> defaultShape
    }

    LaunchedEffect(Unit) {
        t = Track.getTags(path, context)
        withContext(Dispatchers.IO) {
            val coverPath = Track.createOrGetThumbnail(path)
            if (coverPath != null)
                cover = BitmapFactory.decodeFile(coverPath).asImageBitmap()
        }
    }

    AnimatedVisibility(
        visibleState = state,
        enter = scaleIn(initialScale = 0.75f) + fadeIn(initialAlpha = 0.1f),
    ) {
        SegmentedListItem(
            verticalAlignment = Alignment.CenterVertically,
            shapes = ListItemDefaults.shapes(
                shape = shape,
                hoveredShape = defaultPressedShape,
                pressedShape = defaultPressedShape
            ),
            colors = ListItemDefaults.segmentedColors(
                if (path == nowPlaying) MaterialTheme.colorScheme.tertiaryContainer
                    else MaterialTheme.colorScheme.surfaceContainerLowest,
                if (path == nowPlaying) MaterialTheme.colorScheme.onTertiaryContainer
                    else MaterialTheme.colorScheme.onSurface,
            ),
            modifier = modifier
                .padding(bottom = 3.dp, start = 5.dp, end = 5.dp)
                .height(if (Settings.twoRowTrackCard) 60.dp else 80.dp),
            onClick = {
                onBeforeClick()
                setAndPlay(path, resetQueryOnClick)
            },
            contentPadding = PaddingValues(5.dp),
            leadingContent = {
                Box {
                    if (Settings.gradientColoring) {
                        Image(
                            cover ?: noCoverBitmap!!,
                            "Track cover",
                            modifier = Modifier
                                .size(50.dp)
                                .scale(4f)
                                .blur(35.dp, BlurredEdgeTreatment.Unbounded)
                                .alpha(0.6f)
                        )
                    }
                    Box(Modifier.clip(shape)) {
                        Image(
                            cover ?: noCoverBitmap!!,
                            "Track cover",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .aspectRatio(1f)
                                .clickable { showMoreOptions = true }
                                .clip(defaultPressedShape)
                        )
                    }
                }
            },
            trailingContent = {
                IconButton (
                    { Settings.addOrRemoveFavorite(path) }
                ) {
                    Icon(
                        if (Settings.favorites.contains(path)) favorite else heart_plus,
                        null,
                        tint = MaterialTheme.colorScheme.secondary
                    )
                }
            }
        ) {
            Column(
                Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    t?.title ?: "Loading..." ,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 16.sp
                )

                Row(
                    Modifier.fillMaxWidth(),
                ) {
                    Text(
                        t?.artist ?: "🕒",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontSize = 13.sp,
                        modifier = Modifier.weight(0.5f)
                    )
                    Text(
                        tagsSpacer,
                        fontSize = 17.sp,
                        modifier = Modifier.weight(0.1f)
                    )
                    Text(
                        t?.album ?: "",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontSize = 13.sp,
                        modifier = Modifier.weight(0.5f)
                    )
                }

                if (!Settings.twoRowTrackCard) {
                    val dura = formatDurationMs(t?.duration?.milliseconds ?: 1.milliseconds)
                    val bitrate = ((t?.bitrate ?: 1) / 1000f).roundToInt()
                    Text(
                        "$dura${tagsSpacer}${bitrate} kbps${tagsSpacer}${t?.year}${tagsSpacer}${t?.genre}" +
                                if (Track.hasLRC(path)) "${tagsSpacer}LRC" else "",
                        maxLines = 1, overflow = TextOverflow.Ellipsis, fontSize = 13.sp,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }

    if (showMoreOptions) {
        TrackMoreOptionsPopup(path, t?.title) {
            showMoreOptions = false
        }
    }
}