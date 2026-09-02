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
public val skip_next: ImageVector
    get() {
        if (_skip_next != null) {
            return _skip_next!!
        }
        _skip_next =
            ImageVector.Builder(
                name = "skip_next",
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
                        moveTo(16.5f, 18f)
                        verticalLineTo(6f)
                        horizontalLineToRelative(2f)
                        verticalLineTo(18f)
                        horizontalLineToRelative(-2f)
                        close()
                        moveToRelative(-11f, 0f)
                        verticalLineTo(6f)
                        lineToRelative(9f, 6f)
                        lineToRelative(-9f, 6f)
                        close()
                        moveToRelative(2f, -6f)
                        close()
                        moveToRelative(0f, 2.25f)
                        lineTo(10.9f, 12f)
                        lineTo(7.5f, 9.75f)
                        verticalLineToRelative(4.5f)
                        close()
                    }
                }
                .build()
        return _skip_next!!
    }

private var _skip_next: ImageVector? = null
