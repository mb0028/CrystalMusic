package mb28.crysongs

import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import java.io.File

class PlayerExportedActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val uriPath = intent.data?.path

        if (uriPath == null) {
            finish()
        }
        val path = uriPath!!.substring(uriPath.indexOf("/storage/"))
        println(path)
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
