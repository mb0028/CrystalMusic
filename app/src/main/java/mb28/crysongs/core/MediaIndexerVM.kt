package mb28.crysongs.core

import android.content.ContentUris
import android.content.Context
import android.graphics.Bitmap
import android.media.ThumbnailUtils
import android.net.Uri
import android.provider.MediaStore
import android.util.Size
import androidx.lifecycle.ViewModel
import java.io.File
import java.io.FileOutputStream

class MediaIndexerVM : ViewModel() {
    private var _tracksList = mutableListOf<Track>()
    val tracks = _tracksList

    fun refreshList(context: Context, onRefresh: () -> Unit) {
        _tracksList.clear()

        val projection = arrayOf(
            MediaStore.MediaColumns.DATA,
            MediaStore.Video.Media._ID,
            MediaStore.Video.Media.TITLE,
            MediaStore.Video.Media.ARTIST,
            MediaStore.Video.Media.ALBUM,
            MediaStore.Video.Media.GENRE,
            MediaStore.Video.Media.COMPOSER,
            MediaStore.Video.Media.DURATION,
            MediaStore.Video.Media.BITRATE
        )

        context.contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            when(Settings.sortBy) {
                0 -> MediaStore.Audio.Media.DATE_MODIFIED
                else -> MediaStore.Audio.Media.TITLE
            } + " ${if (Settings.sortOrderDesc) "DESC" else "ASC"}",

            )?.use { cursor ->
            val idc = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
            val pc = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)
            val titleC = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
            val artistC = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
            val albumC = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM)
            val genreC = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.GENRE)
            val composerC = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.COMPOSER)
            val durationC = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)
            val bitrateC = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.BITRATE)

            while (cursor.moveToNext()) {
                val id = cursor.getLong(idc)
                val path = cursor.getString(pc)
                val contentUri: Uri = ContentUris.withAppendedId(
                    MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                    id
                )

                val track = Track(
                    contentUri,
                    path,
                    createOrGetThumbnail(path),
                    cursor.getString(titleC) ?: "???",
                    cursor.getString(artistC) ?: "???",
                    cursor.getString(albumC) ?: "???",
                    cursor.getString(genreC) ?: "???",
                    cursor.getString(composerC) ?: "???",
                    cursor.getLong(durationC),
                    cursor.getInt(bitrateC),
                )
                _tracksList += track
            }
        }
        onRefresh()
    }

    companion object {
        fun createOrGetThumbnail(path: String): String {
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
                    return "NULLL"
                }
            }
            return thumbnailFile.path
        }
    }
}