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
public val move_down: ImageVector
    get() {
        if (_move_down != null) {
            return _move_down!!
        }
        _move_down =
            ImageVector.Builder(
                name = "move_down",
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
                        moveTo(7f, 21f)
                        lineTo(5.6f, 19.6f)
                        lineTo(7.18f, 17.95f)
                        quadTo(4.53f, 17.65f, 2.76f, 15.66f)
                        reflectiveQuadTo(1f, 11f)
                        quadTo(1f, 8.07f, 3.04f, 6.04f)
                        reflectiveQuadTo(8f, 4f)
                        horizontalLineToRelative(3f)
                        verticalLineTo(6f)
                        horizontalLineTo(8f)
                        quadTo(5.93f, 6f, 4.46f, 7.46f)
                        reflectiveQuadTo(3f, 11f)
                        quadToRelative(0f, 1.8f, 1.15f, 3.17f)
                        reflectiveQuadTo(7.08f, 15.9f)
                        lineTo(5.6f, 14.43f)
                        lineTo(7f, 13f)
                        lineToRelative(4f, 4f)
                        lineTo(7f, 21f)
                        close()
                        moveToRelative(6f, -1f)
                        verticalLineTo(13f)
                        horizontalLineToRelative(9f)
                        verticalLineToRelative(7f)
                        horizontalLineTo(13f)
                        close()
                        moveToRelative(0f, -9f)
                        verticalLineTo(4f)
                        horizontalLineToRelative(9f)
                        verticalLineToRelative(7f)
                        horizontalLineTo(13f)
                        close()
                        moveTo(15f, 9f)
                        horizontalLineToRelative(5f)
                        verticalLineTo(6f)
                        horizontalLineTo(15f)
                        verticalLineTo(9f)
                        close()
                    }
                }
                .build()
        return _move_down!!
    }

private var _move_down: ImageVector? = null
