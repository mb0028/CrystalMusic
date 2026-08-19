import 'package:crystal_music/Pages/settings_page.dart';
import 'package:crystal_music/Pages/tracks_page.dart';
import 'package:crystal_music/Widgets/mini_player.dart';
import 'package:crystal_music/Utils/audio_indexer.dart';
import 'package:flutter/material.dart';

class MainPage extends StatefulWidget {
  const MainPage({super.key});

  @override
  State<MainPage> createState() => _MainPageState();
}

class _MainPageState extends State<MainPage> {
  int selectedPage = 0;

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      extendBody: true,
      extendBodyBehindAppBar: true,
      
      appBar: AppBar(
        systemOverlayStyle: .dark,
        centerTitle: true,
        surfaceTintColor: Colors.transparent,
        backgroundColor: Colors.transparent,
        actionsPadding: .only(right: 20),
        actions: [
          IconButton(
            style: ButtonStyle(
              backgroundColor: .all(Theme.of(context).colorScheme.surfaceContainer)
            ),
            icon: Icon(Icons.settings_rounded),
            tooltip: "Settings",
            onPressed: () => Navigator.of(context).push(MaterialPageRoute(builder: (context) => SettingsPage())),
          ),
        ],
        title: Text(switch (selectedPage) {
          0 => "Tracks (${AudioIndexer.tracks.length})",
          2 => "Videos",
          3 => "Playlists",
          1 => "Folders",
          _ => throw Exception()
        }),
      ),

      body: Stack(
        alignment: .center,
        children: [
          switch (selectedPage) {
            0 => TracksPage(onChanged: () => setState(() {})),
            1 => Placeholder(),
            2 => Placeholder(),
            3 => Placeholder(),
            _ => throw Exception()
          },
          Align(
            alignment: .bottomCenter,
            child: MiniPlayer()
          ),
        ],
      ),

      bottomNavigationBar: NavigationBar(
        height: 60,
        selectedIndex: selectedPage,
        onDestinationSelected: (value) => setState(() => selectedPage = value),
        backgroundColor: Theme.of(context).colorScheme.surfaceContainer.withAlpha(240),
        destinations: [
          NavigationDestination(
            icon: Icon(Icons.nightlife_outlined),
            selectedIcon: Icon(Icons.nightlife_rounded),
            label: "Tracks"
          ),
          NavigationDestination(
            icon: Icon(Icons.video_library_outlined),
            selectedIcon: Icon(Icons.video_library_rounded),
            label: "Videos"
          ),
          NavigationDestination(
            icon: Icon(Icons.library_music_outlined),
            selectedIcon: Icon(Icons.library_music_rounded),
            label: "Playlists"
          ),
          NavigationDestination(
            icon: Icon(Icons.folder_outlined),
            selectedIcon: Icon(Icons.folder_rounded),
            label: "Folders"
          ),
        ]
      ),
    );
  }
}
