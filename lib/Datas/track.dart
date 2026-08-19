// ignore_for_file: prefer_interpolation_to_compose_strings

import 'dart:io';
import 'package:crystal_music/Utils/media_player.dart';
import 'package:crystal_music/settings.dart';
import 'package:flutter_taglib/flutter_taglib.dart';

/// Memory-eater class...
class Track {
  final String path;
  
  late DateTime modified;
  String title = "";
  String artist = "";
  String album = "";
  String genre = "";
  String format = "";
  int bitrate = -1;
  int year = -1;
  Duration duration = .zero;
  String? coverPath;
  bool hasLrc = false;

  Track(this.path);
  
  @override
  String toString() =>
    "$path|||${modified.toIso8601String()}|||$title|||$artist|||$album|||$genre|||$format|||$bitrate|||$year|||${duration.inMilliseconds}|||$coverPath|||$hasLrc";

  factory Track.fromString(String track) {
    final lines = track.split('|||');
    return Track(lines[0])
      ..modified = DateTime.parse(lines[1])
      ..title = lines[2]
      ..artist = lines[3]
      ..album = lines[4]
      ..genre = lines[5]
      ..format = lines[6]
      ..bitrate = int.parse(lines[7])
      ..year = int.parse(lines[8])
      ..duration = .new(milliseconds: int.parse(lines[9]))
      ..coverPath = lines[10] == "null" ? null : lines[10]
      ..hasLrc = bool.parse(lines[11])
      ;
  }

  static Future<Track> fromPath(String path) async {
    final track = Track(path);
    final tags = await TagLibFile.openAsync(path);
    final pathHash = path.hashCode;

    if (tags != null) {
      // #1 Cache Cover
      if (tags.coverData != null) {
        final coverFile = File("${Settings.appFolder}/Artworks/$pathHash.png");
        if (!await coverFile.exists()) {
          await coverFile.create(recursive: true);
          await coverFile.writeAsBytes(tags.coverData!);
        }
        track.coverPath = coverFile.path;
      }
      track.title = tags.title;
      track.artist = tags.artist;
      track.album = tags.album;
      track.genre = tags.genre;
      track.bitrate = tags.bitrate;
      track.year = tags.year;
      track.duration = tags.duration;
    }
    
    track.hasLrc = await File("${path.substring(0, path.lastIndexOf('.') + 1)}lrc").exists();
    track.modified = await File(path).lastModified();
    track.format = path.substring(path.lastIndexOf(".") + 1);

    tags?.close();
    return track;
  }

  bool get isPlaying => true;

  Future<void> setAndPlay() async {
    await player.setDataAndPlay(path);
    player.startFG();
  }

  static String? getLrcPath(String path) {
    final lPath ="${path.substring(0, path.lastIndexOf('.') + 1)}lrc";
    return File(lPath).existsSync() ? lPath : null;
  }

}