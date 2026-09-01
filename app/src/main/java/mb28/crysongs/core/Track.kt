package mb28.crysongs.core

import android.net.Uri
import java.io.File

data class Track(
    val uri: Uri,
    val path: String,
    val coverPath: String,
    val title: String,
    val artist: String,
    val album: String,
    val genre: String,
    val composer: String,
    val duration: Long,
    val bitrate: Int,
) {
    val lrcPath = path.substring(0, path.lastIndexOf('.')) + ".lrc"
    val hasLRC = File(lrcPath).exists()
}
