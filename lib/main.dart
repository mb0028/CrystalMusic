import 'package:crystal_music/settings.dart';
import 'package:crystal_music/Pages/main_page.dart';
import 'package:dynamic_color/dynamic_color.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

void main() async {
  WidgetsFlutterBinding.ensureInitialized();
  await Settings.load();

  SystemChrome.setSystemUIOverlayStyle(SystemUiOverlayStyle(
    systemNavigationBarContrastEnforced: false,
    systemNavigationBarColor: Colors.transparent
  ));
  
  runApp(const MainApp());
}

class MainApp extends StatelessWidget {
  const MainApp({super.key});

  @override
  Widget build(BuildContext context) {
    return DynamicColorBuilder(
      builder: (l, d) => MaterialApp(
        debugShowCheckedModeBanner: false,
        home: MainPage(),
        theme: ThemeData(
          colorScheme: MediaQuery.platformBrightnessOf(context) == .light ? l : d,
          tooltipTheme: TooltipThemeData(
            preferBelow: false,
            decoration: BoxDecoration(
              borderRadius: .circular(50),
              color: Theme.of(context).colorScheme.surfaceBright,
            ),
            textStyle: TextStyle(
              color: Theme.of(context).colorScheme.onSurface
            ),
          ),
          bottomSheetTheme: BottomSheetThemeData(
            backgroundColor: Colors.transparent
          )
        ),
      ),
    );
  }
}
