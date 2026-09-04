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
public val arrow_cool_down: ImageVector
    get() {
        if (_arrow_cool_down != null) {
            return _arrow_cool_down!!
        }
        _arrow_cool_down =
            ImageVector.Builder(
                name = "arrow_cool_down",
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
                        moveTo(12f, 22f)
                        lineTo(5f, 15f)
                        lineTo(6.4f, 13.58f)
                        lineToRelative(4.6f, 4.6f)
                        verticalLineTo(11f)
                        horizontalLineToRelative(2f)
                        verticalLineToRelative(7.18f)
                        lineTo(17.6f, 13.6f)
                        lineTo(19f, 15f)
                        lineToRelative(-7f, 7f)
                        close()
                        moveTo(11f, 9f)
                        verticalLineTo(6f)
                        horizontalLineToRelative(2f)
                        verticalLineTo(9f)
                        horizontalLineTo(11f)
                        close()
                        moveTo(11f, 4f)
                        verticalLineTo(2f)
                        horizontalLineToRelative(2f)
                        verticalLineTo(4f)
                        horizontalLineTo(11f)
                        close()
                    }
                }
                .build()
        return _arrow_cool_down!!
    }

private var _arrow_cool_down: ImageVector? = null
