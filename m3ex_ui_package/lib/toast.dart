import 'package:flutter/material.dart';
import 'package:flutter_styled_toast/flutter_styled_toast.dart';

class Toast {
  static void show(String text, BuildContext context, {int duration = 3}) {
    showToastWidget(
      Container(
        margin: .only(top: MediaQuery.paddingOf(context).top + 5),
        child: Container(
          padding: .symmetric(vertical: 8, horizontal: 15),
          decoration: BoxDecoration(
            color: Theme.of(context).colorScheme.surfaceContainer,
            borderRadius: .circular(25),
            border: .all(
              width: 2,
              color: Theme.of(context).colorScheme.surfaceBright
            ),
          ),
          child: Text(
            text,
            style: TextStyle(
              color: Theme.of(context).colorScheme.onSurfaceVariant,
            ),
          ),
        ),
      ),
      context: context,
      duration: .new(seconds: duration),
      animation: .fadeScale,
      reverseAnimation: .fadeScale,
      curve: ElasticOutCurve(1.5),
      reverseCurve: ElasticOutCurve(1.5),
      position: .top,
    );
  }
}
