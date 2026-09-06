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
public val reorder: ImageVector
    get() {
        if (_reorder != null) {
            return _reorder!!
        }
        _reorder =
            ImageVector.Builder(
                name = "reorder",
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
                        moveTo(3f, 19f)
                        verticalLineTo(17f)
                        horizontalLineTo(21f)
                        verticalLineToRelative(2f)
                        horizontalLineTo(3f)
                        close()
                        moveTo(3f, 15f)
                        verticalLineTo(13f)
                        horizontalLineTo(21f)
                        verticalLineToRelative(2f)
                        horizontalLineTo(3f)
                        close()
                        moveTo(3f, 11f)
                        verticalLineTo(9f)
                        horizontalLineTo(21f)
                        verticalLineToRelative(2f)
                        horizontalLineTo(3f)
                        close()
                        moveTo(3f, 7f)
                        verticalLineTo(5f)
                        horizontalLineTo(21f)
                        verticalLineTo(7f)
                        horizontalLineTo(3f)
                        close()
                    }
                }
                .build()
        return _reorder!!
    }

private var _reorder: ImageVector? = null
