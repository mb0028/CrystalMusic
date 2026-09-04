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
public val repeat: ImageVector
    get() {
        if (_repeat != null) {
            return _repeat!!
        }
        _repeat =
            ImageVector.Builder(
                name = "repeat",
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
                        moveTo(7f, 22f)
                        lineTo(3f, 18f)
                        lineTo(7f, 14f)
                        lineToRelative(1.4f, 1.45f)
                        lineTo(6.85f, 17f)
                        horizontalLineTo(17f)
                        verticalLineTo(13f)
                        horizontalLineToRelative(2f)
                        verticalLineToRelative(6f)
                        horizontalLineTo(6.85f)
                        lineTo(8.4f, 20.55f)
                        lineTo(7f, 22f)
                        close()
                        moveTo(5f, 11f)
                        verticalLineTo(5f)
                        horizontalLineTo(17.15f)
                        lineTo(15.6f, 3.45f)
                        lineTo(17f, 2f)
                        lineToRelative(4f, 4f)
                        lineToRelative(-4f, 4f)
                        lineTo(15.6f, 8.55f)
                        lineTo(17.15f, 7f)
                        horizontalLineTo(7f)
                        verticalLineToRelative(4f)
                        horizontalLineTo(5f)
                        close()
                    }
                }
                .build()
        return _repeat!!
    }

private var _repeat: ImageVector? = null
