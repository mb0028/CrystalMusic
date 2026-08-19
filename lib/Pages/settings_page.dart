import 'dart:io';
import 'package:crystal_music/settings.dart';
import 'package:flutter/material.dart';
import 'package:m3ex_ui_package/m3ex_card_button.dart';
import 'package:m3ex_ui_package/m3ex_icon_button.dart';
import 'package:m3ex_ui_package/popups.dart';
import 'package:silky_scroll/silky_scroll.dart';

class SettingsPage extends StatefulWidget {
  const SettingsPage({super.key});

  @override
  State<SettingsPage> createState() => _SettingsPageState();
}

class _SettingsPageState extends State<SettingsPage> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      extendBodyBehindAppBar: true,
      appBar: AppBar(
        title: Text("Settings"),
        surfaceTintColor: Colors.transparent,
        backgroundColor: Colors.transparent,
        elevation: 0,
        leadingWidth: 80,
        titleSpacing: 0,
        systemOverlayStyle: .dark,
        leading: IconButton(
          style: ButtonStyle(
            backgroundColor: .all(Theme.of(context).colorScheme.surfaceContainer)
          ),
          icon: Icon(Icons.arrow_back_rounded), 
          onPressed: () => Navigator.of(context).pop(hashCode),
        ),
      ),
      body: Container(
        padding: .symmetric(horizontal: 15),
        child: SilkyListView(
          scrollSpeed: 1.5,
          physics: BouncingScrollPhysics(),
          children: [
            SizedBox(height: 8),

            M3EXCardButton.top(
              text: "AAAAAAAAAA"
            ),
            SizedBox(height: 15),
            
            libraryIncludeSettings(),
            SizedBox(height: 15),
            
            Text("App's folder path: ${Settings.appFolder}"),
            SizedBox(height: MediaQuery.paddingOf(context).bottom + 5),
          ],
        ),
      ),
    );
  }

  Widget libraryIncludeSettings() {
    return Column(
      spacing: 5,
      children: [
        Row(
          mainAxisAlignment: .spaceBetween,
          children: [
            Text("Library Folders:"),
            TextButton(
              onPressed: () async {
                final pathToAdd = await showM3exTextInput(
                  context,
                  "Add folder path",
                  "/sdcard/download",
                  3,
                  initText: "/sdcard/"
                );
                if (pathToAdd != null && await Directory(pathToAdd).exists()) {
                  Settings.libraryInclude.add(pathToAdd);
                  setState(() {});
                  Settings.save();
                }
              },
              child: Text("Add")
            ),
          ],
        ),
        
        Container(
          height: 220,
          padding: .all(5),
          clipBehavior: .antiAlias,
          decoration: BoxDecoration(
            color: Theme.of(context).colorScheme.secondaryContainer,
            borderRadius: .circular(30)
          ),
          child: SilkyListView.builder(
            itemCount: Settings.libraryInclude.length,
            itemBuilder: (context, i) => M3EXCardButton(
              text: Settings.libraryInclude[i],
              cornersType: i == 0 ? 0 : i == Settings.libraryInclude.length - 1 ? 2 : 1,
              iconPadding: .all(5),
              maxLines: 2,
              icon: Settings.libraryInclude.length > 1 ? M3EXIconButton(
                tooltip: "Remove",
                icon: Icons.remove_circle_outline,
                backgroundColor: Theme.of(context).colorScheme.error,
                iconColor: Theme.of(context).colorScheme.errorContainer,
                onClick: () {
                  setState(() => Settings.libraryInclude.removeAt(i));
                  Settings.save();
                },
              ) : null,
            ),
          ),
        ),
      ],
    );
  }

}
