@file:Suppress("PropertyName", "FunctionName", "ControlFlowWithEmptyBody")

package mb28.music

import java.io.File

class LrcParser {
    val LyricLines = mutableListOf<LyricLine>()
    val Count: Int get() = LyricLines.count()
    val Duration: Float get() = LyricLines.last().TimeStomp
    var IsGettingLineInRealtimePossible = false

    val Has0Timestomps: Boolean get() {
        var noTimedLines = 0
        LyricLines.forEach {
            if (it.TimeStomp == -1f) {
                noTimedLines++
            }
        }
        return noTimedLines == Count
    }


    constructor(path: String) {
        val lines = File(path).readLines()
        lines.forEach { line ->
            if (line.isNotBlank()) {
                if (line.IsTimedSection()) {
                    LyricLines.add(LyricLine.FromString(line))
                } else if (line.IsTagsSection()) {

                } else {
                    LyricLines.add(LyricLine(-1f, line))
                }
            }
        }
        IsGettingLineInRealtimePossible = !Has0Timestomps
    }

    fun String.IsTimedSection() : Boolean {
        return this.startsWith('[') && this.contains(':') && this.contains('.')
    }

    fun String.IsTagsSection(): Boolean {
        return this.startsWith("[re:") || this.startsWith("[ti:") || this.startsWith("[ar:") ||
            this.startsWith("[offset:") || this.startsWith("[au:") || this.startsWith("[al:")
    }

    fun LineByAudioPosition(audioPosInMillisecond: Int): String {
        if (IsGettingLineInRealtimePossible) {
            val audioPosInSeconds = audioPosInMillisecond / 1000f
            if (audioPosInSeconds <= LyricLines.first().TimeStomp)
                return ""
            if (audioPosInSeconds >= LyricLines.last().TimeStomp)
                return LyricLines.last().Lyric
            return LyricLines[LyricLines.indexOf(LyricLines.find { audioPosInSeconds <= it.TimeStomp }) - 1].Lyric
        }
        return "No lyrics..."
    }

    fun LineIndex(audioPosInMillisecond: Int): Int {
        if (IsGettingLineInRealtimePossible) {
            val audioPosInSeconds = audioPosInMillisecond / 1000f
            if (audioPosInSeconds <= LyricLines.first().TimeStomp)
                return 0
            if (audioPosInSeconds >= LyricLines.last().TimeStomp)
                return LyricLines.count() - 1
            return LyricLines.indexOf(LyricLines.find { audioPosInSeconds <= it.TimeStomp }) - 1
        }
        return -1
    }
}

data class LyricLine(val TimeStomp: Float, val Lyric: String) {
    companion object {
        fun FromString(text: String) : LyricLine {
            val time = text.substring(0, text.lastIndexOf(']')).removePrefix("[") // xx:xx.xx?
            val sec = (time.substring(0, 2).toFloat() * 60f) + time.substring(3).toFloat()

            return if (text.lastIndexOf('[') + 1 != text.count()) {
                LyricLine(
                    sec,
                    text.substring(text.lastIndexOf(']') + 1).trimStart()
                )
            } else {
                LyricLine(sec, "")
            }
        }
    }
}