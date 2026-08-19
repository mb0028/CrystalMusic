import 'dart:async';
import 'package:flutter/services.dart';

final player = MediaPlayer();

class MediaPlayer {
  static const channel = MethodChannel("mb28.CrystalSongs/MediaPlayer");

  Future<void> play() async => await channel.invokeMethod("play");
  Future<void> pause() async => await channel.invokeMethod("pause");
  Future<void> stop() async => await channel.invokeMethod("stop");

  FutureOr<bool> isPlaying() async => await channel.invokeMethod<bool>("isPlaying") ?? false;
  FutureOr<bool> isLooping() async => await channel.invokeMethod<bool>("isLooping") ?? false;
  FutureOr<int> position() async => await channel.invokeMethod<int>("getPosition") ?? 0;

  Future<void> setData(String path) async => await channel.invokeMethod("prepare", {"path": path});
  Future<void> setDataAndPlay(String path) async => await channel.invokeMethod("prepareAndPlay", {"path": path});
  Future<void> setVolume(double vol) async => await channel.invokeMethod("setVolume", {"volume": vol});
  Future<void> seekTo(int msec) async => await channel.invokeMethod("seekTo", {"msec": msec});
  Future<void> setIsLooping(bool loop) async => await channel.invokeMethod("setIsLooping", {"loop": loop});

  Future<void> updateNotification(String title, String subtitle, String sst) async =>
    await channel.invokeMethod("updateNotif", {"title": title, "subtitle": subtitle, "sst": sst});
  
  Future<void> startFG() async => await channel.invokeMethod("startFG");
  Future<void> stopFG() async => await channel.invokeMethod("stopFG");

  /// Releases resources associated with this MediaPlayer object. </br>
  /// You must call this method once the player is no longer required.
  Future<void> dispose() async => await channel.invokeMethod("dispose");
}
