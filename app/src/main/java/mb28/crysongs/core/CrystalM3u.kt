package mb28.crysongs.core

import java.io.File

private const val ITEM_MUSIC = "Music -> "
private const val ITEM_PATH = "Name -> "
private const val ITEM_NAME = "Path -> "

data class CrystalM3u( //TODO: Use this for playlists
    var path: String,
    var name: String,
    val songs: MutableList<String>,
) {

    override fun toString(): String {
        var data = "$ITEM_NAME$name" // $ITEM_PATH$path

        songs.forEach {
            data += "\n$ITEM_MUSIC$it" // Remove path from list and then add it later
        }
        return data
    }

    fun save() {
        with(File(path)) {
            createNewFile()
            writeText(this@CrystalM3u.toString())
        }
    }

    companion object fun CrystalM3u.fromString(s: String) : CrystalM3u {
        return CrystalM3u("", "", mutableListOf()).apply {
            s.split('\n').forEach { line ->
                when {
                    line.startsWith(ITEM_MUSIC) -> songs.add(line.removePrefix(ITEM_MUSIC))
                    line.startsWith(ITEM_NAME) -> songs.add(line.removePrefix(ITEM_MUSIC))
                    line.startsWith(ITEM_PATH) -> songs.add(line.removePrefix(ITEM_MUSIC))
                }
            }
        }
    }

//    fun CrystalM3u.fromStringAsTrack() {
//    }
}