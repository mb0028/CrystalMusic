package mb28.crysongs.ui

import android.app.NotificationManager
import android.graphics.BitmapFactory
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ListItemDefaults
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.NotificationManagerCompat
import mb28.crysongs.R
import mb28.crysongs.core.Settings
import mb28.crysongs.core.formatDurationMs
import mb28.crysongs.player
import mb28.crysongs.setAndPlay
import mb28.crysongs.tracksVM
import kotlin.math.roundToInt
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun TracksList(modifier: Modifier = Modifier) {
    LazyColumn(
        contentPadding = PaddingValues(vertical = 200.dp),
    ) {
        val count = tracksVM.tracks.count()
        items(count) { i ->
            val t = tracksVM.tracks[i]
            SegmentedListItem(
                shapes = ListItemDefaults.segmentedShapes(i, count),
                modifier = Modifier
                    .padding(bottom = 5.dp)
                    .padding(horizontal = 10.dp)
                    .background(
                        Brush.horizontalGradient(
                            listOf(
                                MaterialTheme.colorScheme.secondaryContainer,
                                MaterialTheme.colorScheme.surfaceContainer,
                            )
                        ),
                        shape = ListItemDefaults.segmentedShapes(i, count).shape,
                    ),
                colors = ListItemDefaults.segmentedColors(
                    containerColor = Color.Transparent
                ),
                contentPadding = PaddingValues(8.dp),
                leadingContent = {
                    if (t.coverPath != "NULLL") {
                        Image(
                            BitmapFactory.decodeFile(t.coverPath).asImageBitmap(),
                            "Track cover",
                            contentScale = ContentScale.FillHeight,
                            modifier = Modifier
                                .height(80.dp)
                                .width(80.dp)
                                .clip(RoundedCornerShape(25.dp))
                        )
                    } else {
                        Box(
                            Modifier
                                .height(80.dp)
                                .width(80.dp)
                                .background(
                                    MaterialTheme.colorScheme.outlineVariant,
                                    RoundedCornerShape(25.dp)
                                )
                        ) {
                            Image(
                                painterResource(R.drawable.null_track_cover),
                                "Track cover",
                                contentScale = ContentScale.FillHeight,
                                modifier = Modifier.fillMaxSize().padding(8.dp)
                            )
                        }
                    }
                },
                onClick = {
                    setAndPlay(t)
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
    }
}