// ignore_for_file: prefer_interpolation_to_compose_strings, prefer_adjacent_string_concatenation

import 'dart:io';
import 'package:crystal_music/Datas/track.dart';
import 'package:crystal_music/Popups/track_info_popup.dart';
import 'package:crystal_music/Utils/audio_indexer.dart';
import 'package:crystal_music/Utils/utils.dart';
import 'package:flutter/material.dart';

class TrackTile extends StatelessWidget {
  static const subTextScale = 12.0; 
  final Track track;
  final int cornersType;
  final Function onChanged;
  const TrackTile({super.key, required this.track, required this.cornersType, required this.onChanged});

  @override
  Widget build(BuildContext context) {
    return GestureDetector(
      onTap: () async {
        AudioIndexer.nowPlaying = track;
        onChanged();
        await track.setAndPlay();
      },
      child: Container(
        margin: .symmetric(vertical: 2),
        padding: .only(top: 2.5, bottom: 2.5, left: 2.5, right: 15),
        decoration: BoxDecoration(
          borderRadius: switch (cornersType) {
            0 => .only(topLeft: .circular(20), topRight: .circular(20)).add(.circular(10)),
            1 => .circular(10),
            2 => .only(bottomLeft: .circular(20), bottomRight: .circular(20)).add(.circular(10)),
            _ => .circular(0),
          },
          gradient: LinearGradient(
            colors: [
              Theme.of(context).colorScheme.secondaryContainer,
              Theme.of(context).colorScheme.surfaceContainer
            ],
            begin: .topLeft,
            end: .bottomRight
          )
        ),
        child: Row(
          spacing: 5,
          children: [
            GestureDetector(
              onTap: () => showTrackInfoPopup(context, track.path),
              child: Container(
                margin: .all(2),
                width: 75,
                height: 75,
                clipBehavior: .antiAlias,
                decoration: BoxDecoration(
                  color: Theme.of(context).colorScheme.surfaceContainerLow,
                  borderRadius: .circular(25)
                ),
                child: track.coverPath != null ? Image.file(
                  File(track.coverPath!),
                  cacheWidth: 300,
                  cacheHeight: 300,
                ) : Image.asset(
                  "Assets/Unknown.png",
                ),
              ),
            ),
            Expanded(
              child: Column(
                spacing: 3,
                crossAxisAlignment: .stretch,
                children: [
                  Text(
                    track.title,
                    maxLines: 1,
                    overflow: .ellipsis,
                    style: TextStyle(
                      fontSize: 16,
                    ),
                  ),
                  _ArtistAlbumRow(track: track),
                  _InfoRow(track: track),
                ],
              ),
            ),
          ],
        ),
      ),
    );
  }
}

class _ArtistAlbumRow extends StatelessWidget {
  const _ArtistAlbumRow({required this.track});
  final Track track;

  @override
  Widget build(BuildContext context) {
    return Row(
      children: [
        Expanded(
          child: Text(
            track.artist,
            maxLines: 1,
            overflow: .ellipsis,
            style: TextStyle(
              fontSize: TrackTile.subTextScale
            ),
          ),
        ),
        Text(" • "),
        Expanded(
          child: Text(
            track.album,
            maxLines: 1,
            overflow: .ellipsis,
            style: TextStyle(
              fontSize: TrackTile.subTextScale
            ),
          ),
        ),
      ],
    );
  }
}

class _InfoRow extends StatelessWidget {
  const _InfoRow({required this.track});
  final Track track;

  @override
  Widget build(BuildContext context) {
    return Text(
      "${formattedDuration(track.duration)}${track.genre.isNotEmpty ? " • ${track.genre}" : ''}"
        + (track.year > 0 ? " • ${track.year.toString().padLeft(4, "0")}" : '')
        + " • ${track.format} • ${track.bitrate}kbps${track.hasLrc ? " • Lrc" : ''}",
      maxLines: 1,
      overflow: .ellipsis,
      style: TextStyle(
        fontSize: TrackTile.subTextScale
      ),
    );
  }
}
