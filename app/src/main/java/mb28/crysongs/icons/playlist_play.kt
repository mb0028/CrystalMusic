package mb28.crysongs.icons


import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

@Suppress("CheckReturnValue")
public val playlist_play: ImageVector
    get() {
        if (_playlist_play != null) {
            return _playlist_play!!
        }
        _playlist_play =
            ImageVector.Builder(
                name = "playlist_play",
                defaultWidth = 24.dp,
                defaultHeight = 24.dp,
                viewportWidth = 24f,
                viewportHeight = 24f,
            )
                .apply {
                    path(
                        fill = SolidColor(Color.Black),
                        fillAlpha = 1f,
                        stroke = null,
                        strokeAlpha = 1f,
                        strokeLineWidth = 1f,
                        strokeLineCap = StrokeCap.Butt,
                        strokeLineJoin = StrokeJoin.Bevel,
                        strokeLineMiter = 1f,
                        pathFillType = PathFillType.Companion.NonZero,
                    ) {
                        moveTo(3f, 16f)
                        verticalLineTo(14f)
                        horizontalLineToRelative(8f)
                        verticalLineToRelative(2f)
                        horizontalLineTo(3f)
                        close()
                        moveTo(3f, 12f)
                        verticalLineTo(10f)
                        horizontalLineTo(15f)
                        verticalLineToRelative(2f)
                        horizontalLineTo(3f)
                        close()
                        moveTo(3f, 8f)
                        verticalLineTo(6f)
                        horizontalLineTo(15f)
                        verticalLineTo(8f)
                        horizontalLineTo(3f)
                        close()
                        moveTo(16f, 21f)
                        verticalLineTo(13f)
                        lineToRelative(6f, 4f)
                        lineToRelative(-6f, 4f)
                        close()
                    }
                }
                .build()
        return _playlist_play!!
    }

private var _playlist_play: ImageVector? = null
