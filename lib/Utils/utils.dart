
String formattedDuration(Duration duration) {
  final minSec = "${duration.inMinutes.toString().padLeft(2, "0")}:${(duration.inSeconds.remainder(60)).toString().padLeft(2, "0")}";
  final h = duration.inHours > 0 ? "${duration.inHours.toString().padLeft(2, "0")}:" : "";
  return h + minSec;
}

String formattedDurationWithSeconds(Duration duration) {
  final minSec = "${duration.inMinutes.toString().padLeft(2, "0")}:${(duration.inSeconds.remainder(60)).toString().padLeft(2, "0")}.${duration.inMilliseconds.remainder(100).toString().padLeft(2, "0")}";
  return minSec;
}