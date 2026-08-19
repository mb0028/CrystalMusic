import 'package:flutter/material.dart';

class M3EXCardButton extends StatelessWidget {
  final String text;
  final int cornersType;
  final Function? onClick;
  final Widget? icon;
  final double? fontSize;
  final Color? backgroundColor;
  final Color? textColor;
  final EdgeInsetsGeometry? margin;
  final EdgeInsetsGeometry? iconPadding;
  final bool autoScale;
  final int maxLines;
  const M3EXCardButton({super.key, required this.text, this.onClick, this.icon, this.fontSize,
    this.cornersType = 1, this.backgroundColor, this.textColor, this.margin, this.iconPadding, this.autoScale = false, this.maxLines = 2});

  @override
  Widget build(BuildContext context) {
    return GestureDetector(
      onTap: () => onClick?.call(),
      child: Container(
        margin: margin ?? .symmetric(vertical: 1.5),
        padding: .symmetric(horizontal: 15, vertical: autoScale ? 15 : 0),
        height: autoScale ? null : 80,
        decoration: BoxDecoration(
          color: backgroundColor ?? Theme.of(context).colorScheme.surfaceContainerLowest.withAlpha(150),
          borderRadius: switch (cornersType) {
            0 => .only(topLeft: .circular(20), topRight: .circular(20)).add(.circular(10)),
            1 => .circular(10),
            2 => .only(bottomLeft: .circular(20), bottomRight: .circular(20)).add(.circular(10)),
            _ => .circular(0),
          }
        ),
        child: Row(
          mainAxisAlignment: .spaceBetween,
          children: [
            Flexible(
              child: Text(
                text,
                maxLines: maxLines,
                overflow: .ellipsis,
                style: TextStyle(
                  fontSize: fontSize ?? 17,
                  color: textColor
                ),
              ),
            ),
            icon != null ? Container(
              padding: iconPadding ?? .all(15),
              decoration: BoxDecoration(
                color: Theme.of(context).colorScheme.secondaryContainer.withAlpha(200),
                borderRadius: .circular(15)
              ),
              child: icon
            ) : SizedBox(width: 5, height: 5)
          ],
        ),
      ),
    );
  }

  factory M3EXCardButton.top({Function? onClick, required String text, Widget? icon, double? fontSize})
    => M3EXCardButton(onClick: onClick, text: text, icon: icon, fontSize: fontSize, cornersType: 0);
  factory M3EXCardButton.end({Function? onClick, required String text, Widget? icon, double? fontSize})
    => M3EXCardButton(onClick: onClick, text: text, icon: icon, fontSize: fontSize, cornersType: 2);
}
