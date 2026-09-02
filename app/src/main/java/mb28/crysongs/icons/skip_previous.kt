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
public val skip_previous: ImageVector
    get() {
        if (_skip_previous != null) {
            return _skip_previous!!
        }
        _skip_previous =
            ImageVector.Builder(
                name = "skip_previous",
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
                        moveTo(5.5f, 18f)
                        verticalLineTo(6f)
                        horizontalLineToRelative(2f)
                        verticalLineTo(18f)
                        horizontalLineToRelative(-2f)
                        close()
                        moveToRelative(13f, 0f)
                        lineToRelative(-9f, -6f)
                        lineToRelative(9f, -6f)
                        verticalLineTo(18f)
                        close()
                        moveToRelative(-2f, -6f)
                        close()
                        moveToRelative(0f, 2.25f)
                        verticalLineTo(9.75f)
                        lineTo(13.1f, 12f)
                        lineToRelative(3.4f, 2.25f)
                        close()
                    }
                }
                .build()
        return _skip_previous!!
    }

private var _skip_previous: ImageVector? = null
