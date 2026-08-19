import 'dart:io';

import 'package:crystal_music/Utils/media_player.dart';

class Settings {
  static final appFolder = Platform.isAndroid
    ? "/sdcard/Documents/.Crystal/Crystal Songs"
    : "C:\\Users\\Public\\Music\\.CrystalSongs";
  static final appDir = Directory(appFolder);


  /// 0 hide - 1 show - 2 expanded
  static int mp_lyricMode = 1;
  static double volume = 1.0;
  static bool loopTrack = false;
  static List<String> libraryInclude = [];
  
  static Future<void> load() async {
    if (!await appDir.exists())
      await appDir.create(recursive: true);

    final settingsFile = File("$appFolder/Settings.txt");
    if (await settingsFile.exists()) {
      for (var line in await settingsFile.readAsLines()) {
        if (line.startsWith("[Lib Include]"))
          libraryInclude.add(line.replaceFirst("[Lib Include]", ''));
          
        else if (line.startsWith("[MP Lyrics Mode]"))
          mp_lyricMode = int.parse(line.replaceFirst("[MP Lyrics Mode]", ''));
        else if (line.startsWith("[Loop Track]"))
          loopTrack = bool.parse(line.replaceFirst("[Loop Track]", ''));
        else if (line.startsWith("[Volume]"))
          volume = double.parse(line.replaceFirst("[Volume]", ''));
      }
    }
    else {
      libraryInclude.addAll(Platform.isAndroid ? [
        "/sdcard/Music",
        "/sdcard/Movies",
        "/sdcard/Download"
      ] : [
        // TODO: Add windows default path
      ]);
      save();
    }
    await player.setIsLooping(loopTrack);
    await player.setVolume(volume);
  }

  static void save() async {
    var data = "[MP Lyrics Mode]$mp_lyricMode\n";
    data += "[Loop Track]$loopTrack\n";
    data += "[Volume]$volume\n";

    for (var libI in libraryInclude)
      data += "[Lib Include]$libI\n";
  
    await File("$appFolder/Settings.txt").writeAsString(data);
  }
  
}