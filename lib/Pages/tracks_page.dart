import 'dart:async';
import 'package:crystal_music/Widgets/track_tile.dart';
import 'package:crystal_music/Utils/audio_indexer.dart';
import 'package:flutter/material.dart';
import 'package:silky_scroll/silky_scroll.dart';

class TracksPage extends StatefulWidget {
  final Function onChanged;
  const TracksPage({super.key, required this.onChanged});

  @override
  State<TracksPage> createState() => _TracksPageState();
}

class _TracksPageState extends State<TracksPage> {

  FutureOr<void> refresh() async {
    await AudioIndexer.scanFast();
    setState(() {});
    widget.onChanged();
    await AudioIndexer.scanAll(() => setState(() {}));
    setState(() {});
    widget.onChanged();
  }

  @override
  void initState() {
    Future.delayed(.new(seconds: 2), () => refresh());
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return Column(
      children: [
        AudioIndexer.scanInfoText != null ? Container(
          margin: .only(top: MediaQuery.paddingOf(context).top, left: 5, right: 5), 
          child: Text(
            AudioIndexer.scanInfoText!,
            maxLines: 1,
          ),
        ) : SizedBox(),
        Expanded(
          child: RefreshIndicator(
            onRefresh: () async => await refresh(),
            child: SilkyListView.builder(
              padding: .only(top: MediaQuery.paddingOf(context).top + 5, bottom: 300, left: 5, right: 5),
              itemCount: AudioIndexer.tracks.length,
              itemBuilder: (context, i) => TrackTile(
                track: AudioIndexer.tracks.values.elementAt(i),
                cornersType: i == 0 ? 0 : i != AudioIndexer.tracks.length - 1 ? 1 : 2,
                onChanged: () => widget.onChanged(),
              ),
            ),
          ),
        ),
      ],
    );
  }
}
