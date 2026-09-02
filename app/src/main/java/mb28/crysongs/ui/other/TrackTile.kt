package mb28.crysongs.ui.other

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.ListItemShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import mb28.crysongs.core.Settings
import mb28.crysongs.core.Track
import mb28.crysongs.core.formatDurationMs
import mb28.crysongs.nowPlaying
import mb28.crysongs.setAndPlay
import kotlin.math.roundToInt
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun TrackTile(t: Track, index: Int, count: Int, resetQueryOnClick: Boolean = true) {
    val coverPath = Track.createOrGetThumbnail(t.path)
    val defaultShape = RoundedCornerShape(15.dp)
    val shape = when {
        count == 1 -> defaultShape
        index == 0 -> RoundedCornerShape(topStart = 25.dp, topEnd = 25.dp, bottomStart = 15.dp, bottomEnd = 15.dp)
        index == count - 1 -> RoundedCornerShape(topStart = 15.dp, topEnd = 15.dp, bottomStart = 25.dp, bottomEnd = 25.dp)
        else -> defaultShape
    }
    SegmentedListItem(
        shapes = ListItemShapes(
            shape = shape,
            defaultShape,
            defaultShape,
            defaultShape,
            defaultShape,
            defaultShape
        ),
        modifier = Modifier
            .padding(bottom = 5.dp)
            .padding(horizontal = 10.dp)
            .background(
                Brush.horizontalGradient(
                    listOf(
                        if (t == nowPlaying) MaterialTheme.colorScheme.tertiaryContainer
                        else MaterialTheme.colorScheme.secondaryContainer,
                        MaterialTheme.colorScheme.surfaceContainer,
                    )
                ),
                shape = shape
            ),
        colors = ListItemDefaults.segmentedColors(
            containerColor = Color.Transparent
        ),
        contentPadding = PaddingValues(8.dp),
        leadingContent = {
            if (coverPath != null) {
                Image(
                    BitmapFactory.decodeFile(coverPath).asImageBitmap(),
                    "Track cover",
                    contentScale = ContentScale.FillHeight,
                    modifier = Modifier
                        .height(80.dp)
                        .width(80.dp)
                        .clip(RoundedCornerShape(25.dp))
                )
            } else {
                NoCoverImage()
            }
        },
        onClick = {
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
                    Settings.tagsSpacer, fontSize = 26.sp,
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

            val dura by remember { mutableStateOf(formatDurationMs(t.duration.milliseconds)) }
            val bitrate by remember { mutableIntStateOf((t.bitrate / 1000f).roundToInt()) }
            Text(
                "$dura${Settings.tagsSpacer}${bitrate} kbps${Settings.tagsSpacer}${t.genre}" +
                        if (t.hasLRC) "${Settings.tagsSpacer}LRC" else "",
                maxLines = 1, overflow = TextOverflow.Ellipsis, fontSize = 13.sp,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}