package mb28.crysongs.ui.fullscreen_player

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import mb28.crysongs.icons.playlist_add
import mb28.crysongs.nowPlaying
import mb28.crysongs.ui.popups.AddToPlaylistPopup
import kotlin.math.roundToInt
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun FSTagsTab(modifier: Modifier = Modifier) {
    var showAddToPlaylist by rememberSaveable { mutableStateOf(false) }

    if (nowPlaying != null) {
        val tags = listOf(
            "Title: ${nowPlaying!!.title}",
            "Artist: ${nowPlaying!!.artist}",
            "Album: ${nowPlaying!!.album}",
            "Composer: ${nowPlaying!!.composer}",
            "Genre: ${nowPlaying!!.genre}",
            " ",
            "Duration: ${nowPlaying!!.duration.milliseconds}",
            "Bitrate: ${(nowPlaying!!.bitrate / 1000f).roundToInt()} kbps",
            "Year: ${nowPlaying!!.year}",
            " ",
            "Album artist: ${nowPlaying!!.albumArtist}",
            " ",
            "Path:\n${nowPlaying!!.path.removePrefix("/storage/emulated/")}",
            "LRC path: ${if (nowPlaying!!.hasLRC) "\n${nowPlaying!!.lrcPath.removePrefix("/storage/emulated/")}" else "No lrc file found"}",
        )
        LazyColumn(
            modifier,
            contentPadding = PaddingValues(vertical = 200.dp)
        ) {
            item {
                Row(
                    Modifier.fillMaxWidth().padding(bottom = 10.dp),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    FilledTonalIconButton(
                        { showAddToPlaylist = true }
                    ) {
                        Icon(playlist_add, null)
                    }
                }
            }
            items(tags.count()) { i ->
                Text(
                    tags[i],
                    fontSize = 20.sp,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
        }

        if (showAddToPlaylist) {
            AddToPlaylistPopup(nowPlaying!!) { showAddToPlaylist = false }
        }
    }
}