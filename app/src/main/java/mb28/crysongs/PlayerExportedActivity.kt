package mb28.crysongs

import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import java.io.File

class PlayerExportedActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        var uriPath = intent.data?.path
        Log.i("Crystal Songs", "App started with external app. Intent data: $uriPath")

        val extraPath = intent.getStringExtra(EXTRA_PATH)
        if (extraPath != null) {
            val intent = Intent(this, FullscreenPlayerActivity::class.java)
                .putExtra(EXTRA_LOAD_FROM_OTHER_APPS, true)
                .putExtra(EXTRA_PATH, extraPath)
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle())
            finish()
            super.onCreate(savedInstanceState)
            return
        }

        if (uriPath == null) {
            finish()
        }

        try {
            if (!uriPath!!.contains("storage/emulated")) {
                contentResolver.query(
                    intent.data!!,
                    arrayOf(MediaStore.Audio.Media.DATA),
                    null, null, null
                )?.use { cursor ->
                    val pathC = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)
                    if (cursor.moveToFirst()) {
                        uriPath = cursor.getString(pathC)
                    }
                }
            }
        } catch (_: Exception) {
            try {
                val path = cacheDir.path + "/LastOpenedSong.track"
                val iStream = contentResolver.openInputStream(intent.data!!)
                val oStream = File(path).outputStream()
                oStream.write(iStream!!.readBytes())
                oStream.flush()
                oStream.close()
                iStream.close()

                val intent = Intent(this, FullscreenPlayerActivity::class.java)
                    .putExtra(EXTRA_LOAD_FROM_OTHER_APPS, true)
                    .putExtra(EXTRA_PATH, path)

                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle())
                finish()
                super.onCreate(savedInstanceState)
                return
            } catch (e: Exception) {
                Log.e(packageName, e.message ?: "ERROR")
            }
        }

        if (uriPath == null) {
            finish()
        }

        val path = uriPath!!.substring(uriPath.indexOf("/storage/"))
        if (!File(path).exists()) {
            finish()
        }

        val intent = Intent(this, FullscreenPlayerActivity::class.java)
            .putExtra(EXTRA_LOAD_FROM_OTHER_APPS, true)
            .putExtra(EXTRA_PATH, path)
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle())
        finish()

        super.onCreate(savedInstanceState)
    }
}
