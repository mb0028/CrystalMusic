package mb28.music

import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

object LRCLIB {
    const val LRCLIB_NET = "https://lrclib.net"
    const val API_SEARCH = "/api/search"

    // https://lrclib.net/docs#lyrics-record-fields
    data class LyricsRecord(
        val id: Int,
//        val name: String,
        val trackName: String,
        val artistName: String,
        val albumName: String,
        val duration: Int,
        val instrumental: Boolean,
        val hasWordSync: Boolean,
        val plainLyrics: String,
        val syncedLyrics: String,
        val lyricsfile: String
    ) {
        override fun toString(): String {
            return  "ID: ${id}\n" +
                    "Title: ${trackName}\n" +
                    "Artist: ${artistName}\n" +
                    "Album: ${albumName}\n\n" +
//                    "Duration: ${duration}\n" +
//                    "Instrumental: ${instrumental}\n" +
//                    "Has word sync: ${hasWordSync}\n" +
                    "Plain: ${plainLyrics}\n-------------------\n" +
                    "Synced: ${syncedLyrics}\n-------------------\n"
//                    "File: $lyricsfile"
        }
    }

    fun search(text: String) : List<LyricsRecord>? {
        val formattedText = text.replace(' ', '+')
        val url = URL("$LRCLIB_NET$API_SEARCH?q=$formattedText")

        println("Starting the search for $text")

        val http = url.openConnection() as HttpURLConnection
        try {
            with(http) {
                requestMethod = "GET"
                connectTimeout = 15000
                readTimeout = 15000
            }

            val reader = BufferedReader(InputStreamReader(http.inputStream))
            val body = reader.readText()
            reader.close()
            val lr = parseApiResult(body)

            println("lrclib: $body")
            println("---------------------------------------\n"+
                "Found ${lr.count()} Lyrics.")
            lr.forEach {
                println("---------------------------------------")
                println(it.toString())
            }

            return lr
        } catch (e: Exception) {
            println(e.toString())
            return null
        } finally {
            http.disconnect()
        }
    }

    fun parseApiResult(json: String) : List<LyricsRecord> {
        val result = mutableListOf<LyricsRecord>()
        val results = json.split("},{")

        results.forEach { text ->
            println(text)
            try {
                val id = text.substringAfter("\"id\":")
                val idInt = id.substring(0, id.indexOf(',').coerceAtLeast(0)).toIntOrNull() ?: -1

                var name = text.substringAfter("\"trackName\":\"")
                name = name.substring(0, name.indexOf("\",").coerceAtLeast(0))

                var artistName = text.substringAfter("\"artistName\":\"")
                artistName = artistName.substring(0, artistName.indexOf("\",").coerceAtLeast(0))

                var albumName = text.substringAfter("\"albumName\":\"")
                albumName = albumName.substring(0, albumName.indexOf("\",").coerceAtLeast(0))

                val duration = text.substringAfter("\"duration\":")
                val durationInt = duration.substring(0, duration.indexOf(',').coerceAtLeast(0)).toIntOrNull() ?: -1

                val instrumental = text.substringAfter("\"instrumental\":")
                val instrumentalBool = instrumental.substring(0, instrumental.indexOf(',').coerceAtLeast(0)).toBooleanStrictOrNull() ?: false
                val hasWordSync = text.substringAfter("\"hasWordSync\":")
                val hasWordSyncBool = hasWordSync.substring(0, hasWordSync.indexOf(',').coerceAtLeast(0)).toBooleanStrictOrNull() ?: false

                var plainLyrics = text.substringAfter("\"plainLyrics\":\"")
                plainLyrics = plainLyrics.substring(0, plainLyrics.indexOf("\",").coerceAtLeast(0))

                var syncedLyrics = text.substringAfter("\"syncedLyrics\":\"")
                syncedLyrics = syncedLyrics.substring(0, syncedLyrics.indexOf("\",").coerceAtLeast(0))

                var lyricsfile = text.substringAfter("\"lyricsfile\":\"")
                lyricsfile = lyricsfile.substring(0, lyricsfile.indexOf("\",").coerceAtLeast(0))

                result.add(LyricsRecord(idInt, name, artistName, albumName, durationInt,
                    instrumentalBool, hasWordSyncBool, plainLyrics, syncedLyrics, lyricsfile))
            } catch (_: Exception) {}
        }

        return result
    }
}
