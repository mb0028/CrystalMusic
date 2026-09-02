package mb28.crysongs.ui

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import mb28.crysongs.core.Track
import mb28.crysongs.icons.pause_circle
import mb28.crysongs.icons.play_circle
import mb28.crysongs.icons.search
import mb28.crysongs.icons.skip_next
import mb28.crysongs.icons.skip_previous
import mb28.crysongs.nowPlaying
import mb28.crysongs.player
import mb28.crysongs.playerQuery
import mb28.crysongs.setAndPlay
import mb28.crysongs.ui.other.NoCoverImage

@Composable
fun MiniPlayer() {
    Row (
        Modifier.padding(horizontal = 15.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        FloatingActionButton(
            {},
            elevation = FloatingActionButtonDefaults.elevation(
                0.dp, 0.dp)
        ) {
            Icon(search, null)
        }

        Row(
            Modifier
                .fillMaxWidth()
                .height(80.dp)
                .padding(start = 5.dp)
                .background(
                    MaterialTheme.colorScheme.inversePrimary.copy(alpha = 0.92f),
                    shape = RoundedCornerShape(35.dp)
                ),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (nowPlaying != null) {
                Spacer(Modifier.width(5.dp))

                val coverPath = Track.createOrGetThumbnail(nowPlaying!!.path)
                if (coverPath != null) {
                    Image(
                        BitmapFactory.decodeFile(coverPath).asImageBitmap(),
                        "Track cover",
                        contentScale = ContentScale.FillHeight,
                        modifier = Modifier.width(70.dp).height(70.dp)
                            .clip(RoundedCornerShape(30.dp))
                    )
                } else {
                    NoCoverImage()
                }

                Column(
                    Modifier.fillMaxSize().padding(5.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        nowPlaying!!.title,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Start
                    )
                    PlayPauseNextPrevious()
                }

            } else {
                Text("Play something", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
            }
        }
    }
}

@Composable
fun PlayPauseNextPrevious() {
    var isPlaying by remember { mutableStateOf(player.isPlaying) }
    Row {
        IconButton(
            {
                setAndPlay(
                    playerQuery[(playerQuery.indexOf(nowPlaying) - 1).coerceIn(0, playerQuery.count() - 1)],
                    false
                )
            }
        ) {
            Icon(skip_previous, null)
        }
        IconButton(
            {
                if (player.isPlaying) {
                    player.pause()
                } else {
                    player.start()
                }
                isPlaying = player.isPlaying
            },
            modifier = Modifier.scale(1.5f)
        ) {
            Icon(if (isPlaying) pause_circle else play_circle, null)
        }
        IconButton(
            {
                setAndPlay(
                    playerQuery[(playerQuery.indexOf(nowPlaying) + 1).coerceIn(0, playerQuery.count() - 1)],
                    false
                )
            }
        ) {
            Icon(skip_next, null)
        }
    }
}
