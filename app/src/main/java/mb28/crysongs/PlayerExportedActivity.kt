package mb28.crysongs

import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import java.io.File

var lastUriPath: String? = null

class PlayerExportedActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        var uriPath = intent.data?.path
        Log.i("Crystal Songs",
            "App started with external app. Intent data: $uriPath")

        if (uriPath == null) {
            finish()
        }

        if (lastUriPath == uriPath) {
            startActivity( Intent(this, FullscreenPlayerActivity::class.java))
            finish()
            return
        }

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
        lastUriPath = uriPath
        finish()

        super.onCreate(savedInstanceState)
    }
}
