package mb28.crysongs.core

import android.graphics.Bitmap
import android.media.ThumbnailUtils
import android.net.Uri
import android.util.Size
import java.io.File
import java.io.FileOutputStream

data class Track(
    val uri: Uri,
    val path: String,
    val title: String,
    val artist: String,
    val album: String,
    val genre: String,
    val composer: String,
    val duration: Long,
    val bitrate: Int,
    val year: String,
    val albumArtist: String,
) {
    val lrcPath = path.substring(0, path.lastIndexOf('.')) + ".lrc"
    val hasLRC = File(lrcPath).exists()
    
    companion object {
        fun createOrGetThumbnail(path: String): String? {
            val pathHash = path.hashCode()
            val thumbnailFile = File("${Settings.appCacheThumbsFolder}/$pathHash.jpeg")
            if (!thumbnailFile.exists()) {
                try {
                    val t = ThumbnailUtils.createAudioThumbnail(File(path),
                        Size(600, 600), null)
                    thumbnailFile.createNewFile()
                    val outputStream = FileOutputStream(thumbnailFile)
                    t.compress(Bitmap.CompressFormat.JPEG, 90, outputStream)
                    outputStream.flush()
                    outputStream.close()
                } catch (_: Exception) {
                    return null
                }
            }
            return thumbnailFile.path
        }
    }
}
