
// ignore_for_file: constant_identifier_names

import 'dart:io';

class LrcParser {
  List<LyricLine> lyricLines = [];
  int get count => lyricLines.length;
  double get duration => lyricLines.last.timestomp;
  bool isGettingLineInRealtimePossible = false;

  bool get has0Timestomps {
    int noTimedLines = 0;
    for (var i = 0; i < count; i++)
      if (lyricLines[i].timestomp == NO_TIMESTOMP)
        noTimedLines++;
    return noTimedLines == count;
  }

  /// New LRCParser. use lineByAudioPosition or lyricLines to get lyrics with their timestomps </br>
  LrcParser(String path) {
    if (path.isNotEmpty && File(path).existsSync()) {
      for (var line in File(path).readAsLinesSync()) {
        if (line.isNotEmpty) {
          if (isTimedSection(line))
            lyricLines.add(LyricLine.fromString(line));
          else if (isTagsSection(line)) { }
          else lyricLines.add(LyricLine(NO_TIMESTOMP, line));
        }
      }
    }
    isGettingLineInRealtimePossible = !has0Timestomps;
  }

  void changeLine(int line0, {double newTimestomp = -2, String? newText}) {
    if (line0 > count)
      throw Exception("Lyric dosen't have $line0 lines!");

      final old = lyricLines[line0];
      lyricLines[line0] = LyricLine(newTimestomp == -2 ? old.timestomp : newTimestomp, newText ?? old.lyric);
      isGettingLineInRealtimePossible = !has0Timestomps;
  }

  void addBefore(int line, double timestomp, String text)
  {
    if (line < count)
      lyricLines[line] = LyricLine(timestomp, text);
    else
      lyricLines.add(LyricLine(timestomp, text));
    isGettingLineInRealtimePossible = !has0Timestomps;
  }

  void addAfter(int line, double timestomp, String text) => addBefore(line + 1, timestomp, text);

  String lineByAudioPosition(Duration pos) {
    if (isGettingLineInRealtimePossible) {
      final double posInS = pos.inMilliseconds / 1000;
      if (posInS <= lyricLines.first.timestomp)
        return "";
      else if (posInS >= lyricLines.last.timestomp)
        return lyricLines.last.lyric;
      return lyricLines[lyricLines.indexWhere((i) => posInS <= i.timestomp) - 1].lyric;
    }
    return "🎵 No Lyrics...";
  }

  void save(String path) => File(path).writeAsStringSync(toString());
  Future<void> saveAsync(String path) async => await File(path).writeAsString(toString());

  @override
  String toString() {
    throw Exception("Coming Soon");
    // string r = string.Empty;
    //   int i = 0;
    //   if (!has0Timestomps) {
    //     for (var item in lyricLines) {
    //       Duration time = item.timestomp == NO_TIMESTOMP ? .zero : .new(milliseconds: (item.timestomp * 1000).round());
    //       if (time < .zero)
    //         time = .zero;

    //       final ms = "{(int)Math.Round(time.Milliseconds / 10d):D2}";
    //       r += "[${time.Minutes:D2}:{time.Seconds:D2}.{ms}]{item.Lyric}";

    //       if (i < Count - 1)
    //           r += "\n";
    //       i++;
    //     }
    //   }
    //   else
    //   {
    //       for(var item in lyricLines)
    //       {
    //           r += item.Lyric;
    //           if (i < Count - 1)
    //               r += "\n";
    //           i++;
    //       }
    //   }
    //   return r;
  }

  bool isTimedSection(String text) => text.startsWith('[') && text.contains(':') && text.contains('.');

  bool isTagsSection(String text) => text.startsWith("[re:") || text.contains("[ti:") || text.contains("[ar:")
    || text.startsWith("[al:") || text.contains("[au:") || text.contains("[offset:");

  static const double NO_TIMESTOMP = -1;
}

class LyricLine {
  final double timestomp;
  final String lyric;

  LyricLine(this.timestomp, this.lyric);
  
  factory LyricLine.fromString(String line) {
    if (line.endsWith("]")) {
      final time = (double.parse(line.substring(1, 3)) * 60) + double.parse(line.substring(4, 8));
      return LyricLine(time, " ");
    }
    else if (line.startsWith("[")) {
      final time = (double.parse(line.substring(1, 3)) * 60) + double.parse(line.substring(4, 8));
      return LyricLine(time, line.substring(line.indexOf("]") + 1).trimLeft());
    }
    return LyricLine(LrcParser.NO_TIMESTOMP, "ERROR PARSING :( Line: $line");
  }
}