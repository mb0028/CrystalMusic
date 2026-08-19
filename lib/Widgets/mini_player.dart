// ignore_for_file: non_constant_identifier_names

import 'dart:io';
import 'package:crystal_music/Datas/track.dart';
import 'package:crystal_music/Utils/lrc_parser_dart.dart';
import 'package:crystal_music/Utils/media_player.dart';
import 'package:crystal_music/settings.dart';
import 'package:crystal_music/Popups/track_info_popup.dart';
import 'package:crystal_music/Utils/utils.dart';
import 'package:crystal_music/Utils/audio_indexer.dart';
import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:m3ex_ui_package/m3ex_icon_button.dart';

class MiniPlayer extends StatefulWidget {
  const MiniPlayer({super.key});

  @override
  State<MiniPlayer> createState() => _MiniPlayerState();
}

class _MiniPlayerState extends State<MiniPlayer> {
  Duration pos = Duration.zero;
  bool loop = true;
  bool isPlaying = false;
  Duration lastAudioDuration = .new(seconds: -1);
  LrcParser? lrcParser;
  String lastLrcLine = "";
  String lastNotificationLrcLine = "";

  void playerLoop() async {
    while (loop) {
      if (AudioIndexer.nowPlaying != null) {
        pos = Duration(milliseconds: await player.position());
        isPlaying = await player.isPlaying();
        lastLrcLine = lrcParser?.lineByAudioPosition(pos) ?? "🎵 No Lyrics...";

        // On audio changes
        if (lastAudioDuration != AudioIndexer.nowPlaying!.duration) {
          final lrcPath = Track.getLrcPath(AudioIndexer.nowPlaying!.path);
          if (lrcPath != null)
            lrcParser = LrcParser(lrcPath);
          else lrcParser = null;

          lastAudioDuration = AudioIndexer.nowPlaying!.duration;
        }

        // On lyric active line changed
        if (lastNotificationLrcLine != lastLrcLine) {
          player.updateNotification(
            AudioIndexer.nowPlaying!.title,
            lastLrcLine,
            lastLrcLine
          );
          lastNotificationLrcLine = lastLrcLine;
        }

        setState(() {});
      }
      await Future.delayed(.new(milliseconds: 200));
    }
  }

  @override
  void initState() {
    playerLoop();
    super.initState();
  }

  @override
  void dispose() {
    loop = false;
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return AnimatedContainer(
      duration: Duration(milliseconds: 600),
      curve: kDebugMode ? Curves.easeOutCirc : Curves.elasticOut,
      margin: .symmetric(horizontal: 10).add(.only(bottom: MediaQuery.paddingOf(context).bottom + 10)),
      padding: .symmetric(horizontal: 6).add(.only(bottom: 6)),
      height: AudioIndexer.nowPlaying == null ? 50 : switch (Settings.mp_lyricMode) {
        0 => 135,
        1 => 190,
        2 => MediaQuery.heightOf(context) - 300,
        _ => 190
      },
      width: 600,
      decoration: BoxDecoration(
        borderRadius: .circular(25),
        gradient: LinearGradient(
          colors: [
            Theme.of(context).colorScheme.secondaryContainer.withAlpha(230),
            Theme.of(context).colorScheme.primaryContainer,
          ],
          begin: .topCenter,
          end: .bottomCenter
        )
      ),
      child: AudioIndexer.nowPlaying != null ? Column(
        mainAxisAlignment: .start,
        children: [
          _Seekbar(),
          Expanded(
            child: Settings.mp_lyricMode >= 1 ? Text(
              lastLrcLine,
              textAlign: .center,
              maxLines: Settings.mp_lyricMode == 2 ? 15 : 2,
              overflow: .ellipsis,
              style: TextStyle(
                color: Theme.of(context).colorScheme.primary,
                fontSize: 19
              ),
            ) : SizedBox(),
          ),
          Settings.mp_lyricMode == 2 ? _VolumeSlider() : SizedBox(),
          Row(
            spacing: 5,
            children: [
              GestureDetector(
                onTap: () => showTrackInfoPopup(context, AudioIndexer.nowPlaying!.path),
                child: Container(
                  width: 80,
                  height: 80,
                  clipBehavior: .antiAlias,
                  decoration: BoxDecoration(
                    color: Theme.of(context).colorScheme.surfaceContainerLow,
                    borderRadius: .circular(21)
                  ),
                  child: AudioIndexer.nowPlaying!.coverPath != null ? Image.file(
                    File(AudioIndexer.nowPlaying!.coverPath!),
                    cacheWidth: 350,
                    cacheHeight: 350,
                  ) : Image.asset(
                    "Assets/Unknown.png",
                  ),
                ),
              ),
              Expanded(
                child: Column(
                  crossAxisAlignment: .start,
                  spacing: 5,
                  children: [
                    Padding(
                      padding: .only(left: 5),
                      child: Text(
                        AudioIndexer.nowPlaying!.title,
                        maxLines: 1,
                        overflow: .ellipsis,
                        style: TextStyle(
                          color: Theme.of(context).colorScheme.tertiary,
                          fontSize: 16
                        ),
                      ),
                    ),
                    _PlayerButtonsRow(),
                  ],
                ),
              ),
            ],
          ),
          
        ],
      ) : Center(child: Text("Play something")),
    );
  }

  Row _PlayerButtonsRow() {
    return Row(
      mainAxisAlignment: .spaceEvenly,
      spacing: 5,
      children: [
        M3EXIconButton(
          icon: switch (Settings.mp_lyricMode) {
            0 => Icons.lyrics_outlined,
            1 => Icons.lyrics_rounded,
            2 => Icons.fullscreen_exit_rounded,
            _ => Icons.lyrics_outlined
          },
          iconSize: 24,
          roundness: 20,
          backgroundColor: Theme.of(context).colorScheme.primary,
          iconColor: Theme.of(context).colorScheme.onPrimary,
          tooltip: "Toggle Lyrics",
          onClick: () {
            setState(() => Settings.mp_lyricMode = switch (Settings.mp_lyricMode) {
              0 => 1,
              1 => 2,
              2 => 0,
              _ => 0
            });
            Settings.save();
          },
        ),
        M3EXIconButton(
          icon: Icons.skip_previous_rounded,
          iconSize: 24,
          roundness: 20,
          backgroundColor: Theme.of(context).colorScheme.secondary,
          iconColor: Theme.of(context).colorScheme.onSecondary,
          tooltip: "Previous",
          onClick: () {
            
          },
        ),
        M3EXIconButton(
          icon: isPlaying ? Icons.pause_rounded : Icons.play_arrow_rounded,
          iconSize: 36,
          roundness: 18,
          backgroundColor: Theme.of(context).colorScheme.tertiary,
          iconColor: Theme.of(context).colorScheme.onTertiary,
          tooltip: "Play / Pause",
          onClick: () async {
            isPlaying = await player.isPlaying();
            if (isPlaying) {
              player.pause();
              player.stopFG();
            }
            else {
              player.play();
              player.startFG();
            } 
            setState(() {});
          },
        ),
        M3EXIconButton(
          icon: Icons.skip_next_rounded,
          iconSize: 24,
          roundness: 20,
          backgroundColor: Theme.of(context).colorScheme.secondary,
          iconColor: Theme.of(context).colorScheme.onSecondary,
          tooltip: "Next",
          onClick: () {
            
          },
        ),
        M3EXIconButton(
          icon: Settings.loopTrack ? Icons.repeat_on_rounded : Icons.repeat_rounded,
          iconSize: 24,
          roundness: 20,
          backgroundColor: Theme.of(context).colorScheme.primary,
          iconColor: Theme.of(context).colorScheme.onPrimary,
          tooltip: "Loop Mode",
          onClick: () async {
            player.setIsLooping(!(await player.isLooping()));
            Settings.loopTrack = !Settings.loopTrack;
            setState(() {});
          },
        ),
      ],
    );
  }

  Widget _Seekbar() {
    return Row(
      mainAxisAlignment: .spaceBetween,
      children: [
        TextButton(
          style: ButtonStyle(padding: .all(.all(4))),
          child: Text(formattedDuration(pos)),
          onPressed: () async {
            await player.seekTo((pos.inMilliseconds - 5000).clamp(0, AudioIndexer.nowPlaying?.duration.inMilliseconds ?? 1));
            setState(() {});
          },
        ),
        Expanded(
          child: SliderTheme(
            data: SliderThemeData(
              trackHeight: 10,
              trackGap: 8,
              thumbSize: WidgetStatePropertyAll(Size(8, 30)),
              trackShape: GappedSliderTrackShape(),
              thumbShape: HandleThumbShape(),
              inactiveTrackColor: Theme.of(context).colorScheme.secondary.withAlpha(100)
            ),
            child: Slider(
              padding: .symmetric(horizontal: 15, vertical: 0),
              value: pos.inSeconds.toDouble(),
              max: AudioIndexer.nowPlaying!.duration.inSeconds.toDouble(), 
              onChanged: (value) async {
                await player.seekTo((value * 1000).round().clamp(0, AudioIndexer.nowPlaying?.duration.inMilliseconds ?? 1));
                setState(() {});
              },
            ),
          ),
        ),
        
        TextButton(
          style: ButtonStyle(padding: .all(.all(4))),
          child: Text(formattedDuration(AudioIndexer.nowPlaying!.duration)),
          onPressed: () async {
            await player.seekTo((pos.inMilliseconds + 5000).clamp(0, AudioIndexer.nowPlaying?.duration.inMilliseconds ?? 1));
            setState(() {});
          },
        ),
      ],
    );
  }

  Widget _VolumeSlider() {
    return ListTile(
      title: SliderTheme(
        data: SliderThemeData(
          trackHeight: 10,
          trackGap: 8,
          thumbSize: WidgetStatePropertyAll(Size(8, 30)),
          trackShape: GappedSliderTrackShape(),
          thumbShape: HandleThumbShape(),
          activeTrackColor: Theme.of(context).colorScheme.tertiary,
          thumbColor: Theme.of(context).colorScheme.tertiary,
          inactiveTrackColor: Theme.of(context).colorScheme.tertiary.withAlpha(100)
        ),
        child: Slider(
          padding: .all(0),
          value: Settings.volume,
          min: 0,
          max: 1,
          onChanged: (value) async {
            await player.setVolume(value);
            Settings.volume = value;
            setState(() {});
          },
        ),
      ),
      leading: Icon(
        switch (Settings.volume) {
          > 0 && <= 0.33 => Icons.volume_mute_rounded,
          > 0.33 && <= 0.66 => Icons.volume_down_rounded,
          0 => Icons.volume_off_rounded,
          _ => Icons.volume_up_rounded,
        },
        color: Theme.of(context).colorScheme.onSecondaryContainer,
      ),
      trailing: Text(
        (Settings.volume * 100).round().toString(),
        style: TextStyle(
          fontSize: 16
        ),
      ),
    );
  }

}
