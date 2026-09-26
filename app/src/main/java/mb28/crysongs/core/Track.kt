package mb28.crysongs.core

import android.content.ContentUris
import android.content.Context
import android.graphics.Bitmap
import android.media.ThumbnailUtils
import android.net.Uri
import android.provider.MediaStore
import android.util.Size
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.util.Collections

data class Track(
    val title: String,
    val artist: String,
    val album: String,
    val genre: String,
    val duration: Long,
    val bitrate: Int,
    val year: String,
) {
    companion object {
        fun lrcPath(path: String) = path.substring(0, path.lastIndexOf('.')) + ".lrc"
        fun hasLRC(path: String) = File(lrcPath(path)).exists()

        suspend fun createOrGetThumbnail(path: String): String? = withContext(Dispatchers.IO) {
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
                    return@withContext null
                }
            }
            return@withContext thumbnailFile.path
        }


        private fun getUri(path: String, context: Context): Uri? {
            context.contentResolver.query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                arrayOf(MediaStore.Audio.Media._ID),
                "${MediaStore.Audio.Media.DATA} = ?",
                arrayOf(path),
                null
            )?.use {
                if (it.moveToFirst()) {
                    val id = it.getLong(it.getColumnIndexOrThrow(MediaStore.Audio.Media._ID))
                    return ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id)
                }
            }
            return null
        }

        private val projection = arrayOf(
            MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.ALBUM, MediaStore.Audio.Media.GENRE,
            MediaStore.Audio.Media.DURATION, MediaStore.Audio.Media.BITRATE,
            MediaStore.Audio.Media.YEAR,
        )

        private val uris = Collections.synchronizedMap(
            object : LinkedHashMap<String, Uri>(200, 0.75f, true) {
                override fun removeEldestEntry(eldest: MutableMap.MutableEntry<String, Uri>): Boolean = size > 200
            }
        )

        suspend fun getTags(path: String?, context: Context) : Track = withContext(Dispatchers.IO) {
            if (path != null) {
                val uri = uris[path] ?: getUri(path, context)?.also { uris[path] = it }
                if (uri != null) {
                    context.contentResolver.query(
                        uri, projection,
                        null, null, null
                    )?.use { cursor ->
                        val titleC = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
                        val artistC = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
                        val albumC = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM)
                        val genreC = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.GENRE)
                        val durationC = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)
                        val bitrateC = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.BITRATE)
                        val yearC = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.YEAR)

                        if (cursor.moveToFirst()) {
                            return@withContext Track(
                                cursor.getString(titleC) ?: "???",
                                cursor.getString(artistC) ?: "???",
                                cursor.getString(albumC) ?: "???",
                                cursor.getString(genreC) ?: "???",
                                cursor.getLong(durationC),
                                cursor.getInt(bitrateC),
                                cursor.getString(yearC) ?: "???",
                            )
                        }
                    }
                }
            }
            return@withContext Track("?", "?", "?", "?", -1, -1, "?")
        }
    }
}
