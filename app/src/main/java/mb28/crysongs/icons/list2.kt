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
public val list_2: ImageVector
    get() {
        if (_list_2 != null) {
            return _list_2!!
        }
        _list_2 =
            ImageVector.Builder(
                name = "list_2",
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
                        moveTo(7f, 18f)
                        verticalLineTo(16f)
                        horizontalLineTo(17f)
                        verticalLineToRelative(2f)
                        horizontalLineTo(7f)
                        close()
                        moveTo(3f, 13f)
                        verticalLineTo(11f)
                        horizontalLineTo(21f)
                        verticalLineToRelative(2f)
                        horizontalLineTo(3f)
                        close()
                        moveTo(7f, 8f)
                        verticalLineTo(6f)
                        horizontalLineTo(17f)
                        verticalLineTo(8f)
                        horizontalLineTo(7f)
                        close()
                    }
                }
                .build()
        return _list_2!!
    }

private var _list_2: ImageVector? = null
