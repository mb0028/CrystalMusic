import 'package:flutter/material.dart';

Future<bool> showM3exDialog(BuildContext context, String title,
  {List<Widget>? children, String ok = "Ok", String cancel = "Cancel"}) async {
  return await showDialog(context: context, builder: (context) => TweenAnimationBuilder(
    duration: Duration(milliseconds: 500),
    tween: Tween<double>(begin: 0.75, end: 1),
    curve: Curves.easeOutCirc,
    builder: (context, value, child) => Transform.scale(
      scale: value,
      child: SimpleDialog(
        backgroundColor: Theme.of(context).colorScheme.secondaryContainer,
        title: Text(title, textAlign: .center),
        contentPadding: .symmetric(horizontal: 20, vertical: 15),
        insetPadding: .all(30),
        children: [
          ...?children,
          SizedBox(height: 20, width: 600),
          Row(
            spacing: 15,
            mainAxisAlignment: .end,
            children: [
              OutlinedButton(
                child: Text(cancel),
                onPressed: () => Navigator.of(context).pop(false),
              ),
              FilledButton(
                child: Text(ok),
                onPressed: () => Navigator.of(context).pop(true),
              ),
            ],
          )
        ]),
    ),
  ),
  ) ?? false;
}

Future<String?> showM3exTextInput(BuildContext context, String title, String placeholder,
  int maxlines, { String initText = "" }) async {
  final input = TextEditingController(text: initText);
  final confirmed = await showM3exDialog(context, title, 
    children: [
      TextField(
        controller: input,
        maxLines: maxlines,
        decoration: InputDecoration(
          hintText: placeholder,
          border: OutlineInputBorder(
            borderRadius: .circular(25),
          )
        ),
      ),
    ],
  );

  return confirmed ? input.text : null;
}