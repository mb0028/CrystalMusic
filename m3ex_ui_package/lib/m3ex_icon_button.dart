import 'package:flutter/material.dart';

class M3EXIconButton extends StatefulWidget {
  final IconData icon;
  final Function onClick;
  final String? tooltip;
  final Color? backgroundColor;
  final Color? iconColor;
  final double iconSize;
  final EdgeInsetsGeometry? padding;
  final double roundness;

  @protected
  final int colorMode;
  
  const M3EXIconButton({super.key, required this.icon, required this.onClick,
    this.tooltip, this.backgroundColor, this.iconColor, this.iconSize = 30, this.colorMode = 0, this.padding, this.roundness = 35});

  @override
  State<M3EXIconButton> createState() => _M3EXIconButtonState();

  factory M3EXIconButton.transparent({required IconData icon, required Function onClick, double? roundness, EdgeInsetsGeometry? padding, String? tooltip, Color? iconColor, double iconSize = 30})
    => M3EXIconButton(
      icon: icon, onClick: onClick, tooltip: tooltip, iconSize: iconSize, padding: padding, roundness: roundness ?? 35,
      backgroundColor: Colors.transparent,
      iconColor: iconColor,
    );

  factory M3EXIconButton.filled({required IconData icon, required Function onClick, double? roundness, EdgeInsetsGeometry? padding, String? tooltip, double iconSize = 30})
    => M3EXIconButton(
      icon: icon, onClick: onClick, tooltip: tooltip, iconSize: iconSize, padding: padding, colorMode: 1, roundness: roundness ?? 35,
    );

  factory M3EXIconButton.filledTonal({required IconData icon, required Function onClick, double? roundness, EdgeInsetsGeometry? padding, String? tooltip, double iconSize = 30})
    => M3EXIconButton(
      icon: icon, onClick: onClick, tooltip: tooltip, iconSize: iconSize, padding: padding, colorMode: 2, roundness: roundness ?? 35,
    );
}

class _M3EXIconButtonState extends State<M3EXIconButton> {
  double roundness = 0;

  @override
  void initState() {
    roundness = widget.roundness;
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return Tooltip(
      message: widget.tooltip ?? "TODO: Add tooltip",
      child: GestureDetector(
        onTap: () => widget.onClick(),
        child: TapRegion(
          onTapInside: (event) => setState(() => roundness = 10),
          onTapUpInside: (event) => setState(() => roundness = widget.roundness),
          child: AnimatedContainer(
            padding: widget.padding ?? .all(8),
            decoration: BoxDecoration(
              color: switch(widget.colorMode) {
                0 => widget.backgroundColor ?? Theme.of(context).colorScheme.surfaceContainer,
                1 => Theme.of(context).colorScheme.primary,
                2 => Theme.of(context).colorScheme.secondaryContainer,
                _ => throw Exception("Color mode can only be: 0, 1 & 2")
              },
              borderRadius: .circular(roundness)
            ),
            duration: Duration(seconds: 1),
            curve: ElasticOutCurve(),
            child: Icon(
              widget.icon,
              size: widget.iconSize * 0.9,
              color: switch(widget.colorMode) {
                0 => widget.iconColor ?? Theme.of(context).colorScheme.onSurfaceVariant,
                1 => Theme.of(context).colorScheme.onPrimary,
                2 => Theme.of(context).colorScheme.onSecondaryContainer,
                _ => throw Exception("Color mode can only be: 0, 1 & 2")
              },
            ),
          ),
        ),
      ),
    );
  }
}
