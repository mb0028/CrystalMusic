// ignore_for_file: prefer_adjacent_string_concatenation

import 'dart:io';

import 'package:crystal_music/Datas/track.dart';
import 'package:crystal_music/Utils/utils.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_taglib/flutter_taglib.dart';
import 'package:m3ex_ui_package/m3ex_card_button.dart';
import 'package:silky_scroll/silky_scroll.dart';

void showTrackInfoPopup(BuildContext context, String path) async {
  final tags = await TagLibFile.openAsync(path);

  await showDialog(context: context, builder: (context) => Dialog(
    insetPadding: .all(10),
    backgroundColor: Theme.of(context).colorScheme.secondaryContainer,
    child: _Dia(tags: tags, path: path)
  ));

  tags?.close();
}

class _Dia extends StatelessWidget {
  final TagLibFile? tags;
  final String path;
  const _Dia({this.tags, required this.path});

  @override
  Widget build(BuildContext context) {
    final isLosslessNull = tags?.audioInfo.isLossless;
    final isLossless = isLosslessNull != null && isLosslessNull;
    final comment = tags?.comment ?? "";
    final p = tags?.properties;
    final description = p?["DESCRIPTION"]?.join(" - ") ?? "";
    final lyrics = p?["LYRICS"]?.join(" - ") ?? "";
    final lyricist = p?["LYRICIST"]?.join(" - ") ?? "";
    final remixer = p?["REMIXER"]?.join(" - ") ?? "";
    final composer = p?["COMPOSER"]?.join(" - ") ?? "";
    final genre = p?["GENRE"]?.join(" - ") ?? "";
    final mood = p?["MOOD"]?.join(" - ") ?? "";
    final lrcPath = Track.getLrcPath(path);
    String? lrc; 
    if (lrcPath != null)
      lrc = File(lrcPath).readAsStringSync();
    return Container(
      margin: .all(10),
      height: 750,
      clipBehavior: .antiAlias,
      decoration: BoxDecoration(
        borderRadius: .circular(30)
      ),
      child: SilkyListView(
        children: [
          M3EXCardButton.top(text: "Title: ${tags?.title}"),
          M3EXCardButton(text: "Artist: ${tags?.artist}"),
          M3EXCardButton(text: "Album: ${tags?.album}"),
          composer.isNotEmpty ? M3EXCardButton(text: "Composer: $composer") : SizedBox(),
          genre.isNotEmpty ? M3EXCardButton(text: "Genre: $genre") : SizedBox(),
          lyricist.isNotEmpty ? M3EXCardButton(text: "Lyricist: $lyricist") : SizedBox(),
          remixer.isNotEmpty ? M3EXCardButton(text: "Remixer: $remixer") : SizedBox(),
          mood.isNotEmpty ? M3EXCardButton(text: "Mood: $mood") : SizedBox(),
          M3EXCardButton.end(text: "Year: ${tags?.year} • Duration: ${formattedDuration(tags?.duration ?? .zero)}"
            + "\nTrack number: ${p?["TRACKNUMBER"]?.join(" - ")} • Disc: ${p?["DISCNUMBER"]?.join(" - ")}"),          
          Divider(),
          M3EXCardButton.top(text: "Format: ${tags?.format?.toLowerCase()} • ${tags?.audioInfo.bitrate}kbps (${tags?.audioInfo.bitrateMode})"
          + "\n${tags?.audioInfo.channels == 2 ? "Stereo" : "Mono"} • ${tags?.audioInfo.sampleRate}Hz${isLossless ? " • Lossless" : ""}"),
          comment.isNotEmpty ? M3EXCardButton(
            text: "Comment:\n\n$comment",
            fontSize: 14,
            maxLines: 50,
            autoScale: true,
            onClick: () => Clipboard.setData(ClipboardData(text: comment)),
          ) : SizedBox(),
          description.isNotEmpty ? M3EXCardButton(
            text: "Description:\n\n$description",
            fontSize: 14,
            maxLines: 50,
            autoScale: true,
            onClick: () => Clipboard.setData(ClipboardData(text: description)),
          ) : SizedBox(),
          lyrics.isNotEmpty ? M3EXCardButton(
            text: "Lyrics: (Embedded)\n\n$lyrics",
            fontSize: 14,
            maxLines: 350,
            autoScale: true,
            onClick: () => Clipboard.setData(ClipboardData(text: lyrics)),
          ) : SizedBox(),
          lrcPath != null ? M3EXCardButton(
            text: "Lyrics: (LRC File)\n\n$lrc",
            fontSize: 14,
            maxLines: 350,
            autoScale: true,
            onClick: () => Clipboard.setData(ClipboardData(text: lrc!)),
          ) : SizedBox(),
          M3EXCardButton(
            text: "File path:\n$path",
            fontSize: 14,
            maxLines: 5,
            autoScale: true,
            cornersType: 2,
          ),
        ],
      ),
    );
  }
}