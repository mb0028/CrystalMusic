import 'package:flutter/material.dart';

class PillTile extends StatelessWidget {
  final Widget? leading;
  final Widget? title;
  final Widget? subtitle;
  final Widget? trailing;
  final Color? backgroundColor;
  final bool autoScale;
  final EdgeInsetsGeometry? margin;
  final Function? onPressed;
  const PillTile({super.key, this.leading, this.title, this.subtitle, this.backgroundColor, this.margin, this.autoScale = false, this.onPressed, this.trailing});

  @override
  Widget build(BuildContext context) {
    return GestureDetector(
      onTap: () => onPressed?.call(),
      child: Container(
        height: autoScale ? null : 80,
        margin: .only(bottom: 5),
        padding: .symmetric(horizontal: 15, vertical: autoScale ? 15 : 0),
        decoration: BoxDecoration(
          color: backgroundColor ?? Theme.of(context).colorScheme.surfaceContainer,
          borderRadius: .circular(75)
        ),
        child: Row(
          mainAxisAlignment: .spaceBetween,
          children: [
            Row(
              spacing: 10,
              children: [
                leading ?? SizedBox(),
                Column(
                  mainAxisAlignment: .center,
                  crossAxisAlignment: .start,
                  children: [
                    title ?? SizedBox(),
                    subtitle ?? SizedBox()
                  ],
                ),
              ],
            ),
            
            trailing ?? SizedBox()
          ],
        ),
      ),
    );
  }
}