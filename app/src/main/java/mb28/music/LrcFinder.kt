package mb28.music

import android.util.Log
import org.jsoup.Jsoup

object LrcFinder {
    fun search(text: String) : String {
        Log.d("Crystal Songs", "Starting the search for $text")
        try {
            val web = Jsoup.connect("https://www.utatime.com/global/lyrics/$text/").get()
            println(web.html())
            return ""
        } catch (e: Exception) {
            return e.toString()
        }
    }
}
