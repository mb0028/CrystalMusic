package mb28.crysongs.ui.fullscreen_player

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import mb28.crysongs.EXTRA_PATH
import mb28.crysongs.TagEditorActivity
import mb28.crysongs.core.getSpecial
import mb28.crysongs.icons.edit
import mb28.crysongs.icons.playlist_add
import mb28.crysongs.nowPlaying
import mb28.crysongs.ui.popups.AddToPlaylistPopup
import org.jaudiotagger.audio.AudioFileIO
import org.jaudiotagger.tag.FieldKey
import java.io.File
import kotlin.math.roundToInt
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun FSTagsTab(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    var showAddToPlaylist by remember { mutableStateOf(false) }
    if (nowPlaying != null) {
        val tag = remember { AudioFileIO.read(File(nowPlaying!!.path)).tag }
        val tags = remember {
//            val txxx = tag.getFields("TXXX")
            listOf(
                "Title: ${tag.getFirst(FieldKey.TITLE)}",
                "Artist: ${tag.getFirst(FieldKey.ARTIST)}",
                "Album: ${tag.getFirst(FieldKey.ALBUM)}",
                "Composer: ${tag.getFirst(FieldKey.COMPOSER)}",
                "Genre: ${tag.getFirst(FieldKey.GENRE)}",
                " ",
                "Duration: ${nowPlaying!!.duration.milliseconds}",
                "Bitrate: ${(nowPlaying!!.bitrate / 1000f).roundToInt()} kbps",
                "Year: ${tag.getFirst(FieldKey.YEAR)}",
                " ",
                "Mood: ${tag.getFirst(FieldKey.MOOD)}",
                "Album artist: ${tag.getFirst(FieldKey.ALBUM_ARTIST)}",
                "Remixer: ${tag.getFirst(FieldKey.REMIXER)}",
                "Lyricist: ${tag.getFirst(FieldKey.LYRICIST)}",
                "Record label: ${tag.getFirst(FieldKey.RECORD_LABEL)}",
                "Language: ${tag.getFirst(FieldKey.LANGUAGE)}",
                "Tags: ${tag.getFirst(FieldKey.TAGS)}",
                "Rating: ${tag.getFirst(FieldKey.RATING)}",
                "Comment: ${tag.getFirst(FieldKey.COMMENT)}",
                " ",
                "Path:\n${nowPlaying!!.path.removePrefix("/storage/emulated/")}",
                "LRC path: ${if (nowPlaying!!.hasLRC) "\n${nowPlaying!!.lrcPath.removePrefix("/storage/emulated/")}" else "No lrc file found"}",
//                "URL: ${txxx.getSpecial("purl")}",
//                " ",
//                "Comment:\n${txxx.getSpecial("comment")}",
//                "Description:\n${txxx.getSpecial("description")}",
//                "Synopsis:\n${txxx.getSpecial("synopsis")}",
//                "Embedded lyrics:\n${txxx.getSpecial("lyrics-   ")}",
            )
        }
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
                    FilledTonalIconButton(
                        {
                            val intent = Intent(context, TagEditorActivity::class.java)
                                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                .putExtra(EXTRA_PATH, nowPlaying!!.path)
                            context.startActivity(intent)
                        }
                    ) {
                        Icon(edit, null)
                    }
                }
            }
            val count = tags.count()
            items(count) { i ->
                val t = tags[i]
                Text(
                    t,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                if (t.isNotBlank() && i != count - 1) {
                    HorizontalDivider(
                        Modifier.padding(vertical = 1.dp).clip(CircleShape),
                        1.dp
                    )
                }

            }
            item {
                val allTags = remember {
                    var att = ""
                    tag.fields.iterator().forEach {
                        att += "${it.id}: $it\n"
                    }
                    att
                }
                HorizontalDivider(
                    Modifier.padding(vertical = 10.dp).clip(CircleShape),
                    8.dp
                )
                Text(
                    "All extracted tags:",
                    fontSize = 22.sp,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                Text(allTags)
            }
        }

        if (showAddToPlaylist) {
            AddToPlaylistPopup(nowPlaying!!) { showAddToPlaylist = false }
        }
    }
}