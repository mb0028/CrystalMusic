import 'package:flutter/material.dart';

class BlurredContainer extends StatelessWidget {
  const BlurredContainer({super.key, required this.child, this.color, this.padding, this.margin, this.roundneess, this.blur, this.border, this.width, this.height});
  /// null = 15
  final double? roundneess;
  /// null = 5
  final double? blur;
  final EdgeInsetsGeometry? padding;
  final EdgeInsetsGeometry? margin;
  final Color? color;
  final BoxBorder? border;
  final double? width;
  final double? height;
  final Widget child;

  @override
  Widget build(BuildContext context) {
    return Container(
      width: width,
      height: height,
      margin: margin,
      clipBehavior: .antiAlias,
      decoration: BoxDecoration(
        borderRadius: .circular(roundneess ?? 15)
      ),
      foregroundDecoration: BoxDecoration(
        borderRadius: .circular(roundneess ?? 15),
        border: border
      ),
      child: BackdropFilter(
        filter: .blur(sigmaX: blur ?? 5, sigmaY: blur ?? 5),
        child: Container(
          color: color,
          padding: padding,
          child: child,
        ),
      ),
    );
  }
}