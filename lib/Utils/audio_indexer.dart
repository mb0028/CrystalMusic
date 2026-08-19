import 'dart:io';
import 'package:crystal_music/settings.dart';
import 'package:crystal_music/Datas/track.dart';

class AudioIndexer {
  static Map<String, Track> tracks = {};
  static Track? nowPlaying;
  static String? scanInfoText;

  static Future<void> scanFast() async {
    final file = File("${Settings.appFolder}/Tracks.txt");
    scanInfoText = "Loading from last cache...";

    if (await file.exists()) {
      tracks = {};
      for (var line in await file.readAsLines()) {
        final trackI = Track.fromString(line);
        tracks[trackI.path] = trackI;
      }

      // tracks.sort((a, b) => b.modified.compareTo(a.modified));
    }
  }

  static Future<void> scanAll(Function onRefresh) async {
    for (var dir in Settings.libraryInclude)
      await for (var file in Directory(dir).list(recursive: true)) {
        final path = file.path;
        if (file is File && isAudio(path)) {
          final td = tracks[path];
          if (td == null || await file.lastModified() != td.modified) {
            scanInfoText = "Indexing $path";
            onRefresh();
            tracks[path] = await Track.fromPath(path);
            await Future.delayed(const Duration(milliseconds: 15));
          }
        }
      }

    String indexed = "";
    for (var f in tracks.values) {
      if (!await File(f.path).exists())
        tracks.remove(f.path);
      else indexed += "${f.toString()}\n";
    }

    // tracks.sort((a, b) => b.modified.compareTo(a.modified));
    scanInfoText = null;
    onRefresh();

    await File("${Settings.appFolder}/Tracks.txt").writeAsString(indexed);
  }

  static bool isAudio(String path) => path.endsWith(".mp3") || path.endsWith(".m4a") || path.endsWith(".flac") || path.endsWith(".wav") || path.endsWith(".ogg");
  static String fileNameWithoutExtention(String path) => path.substring(path.lastIndexOf(Platform.pathSeparator) + 1, path.lastIndexOf("."));
  
}