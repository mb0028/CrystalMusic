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
public val swap_horiz: ImageVector
    get() {
        if (_swap_horiz != null) {
            return _swap_horiz!!
        }
        _swap_horiz =
            ImageVector.Builder(
                name = "swap_horiz",
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
                        moveTo(7f, 20f)
                        lineTo(2f, 15f)
                        lineTo(7f, 10f)
                        lineToRelative(1.4f, 1.42f)
                        lineTo(5.83f, 14f)
                        horizontalLineTo(13f)
                        verticalLineToRelative(2f)
                        horizontalLineTo(5.83f)
                        lineTo(8.4f, 18.58f)
                        lineTo(7f, 20f)
                        close()
                        moveTo(17f, 14f)
                        lineTo(15.6f, 12.58f)
                        lineTo(18.18f, 10f)
                        horizontalLineTo(11f)
                        verticalLineTo(8f)
                        horizontalLineToRelative(7.18f)
                        lineTo(15.6f, 5.43f)
                        lineTo(17f, 4f)
                        lineToRelative(5f, 5f)
                        lineToRelative(-5f, 5f)
                        close()
                    }
                }
                .build()
        return _swap_horiz!!
    }

private var _swap_horiz: ImageVector? = null
