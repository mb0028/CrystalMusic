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
public val align_justify_flex_end: ImageVector
    get() {
        if (_align_justify_flex_end != null) {
            return _align_justify_flex_end!!
        }
        _align_justify_flex_end =
            ImageVector.Builder(
                name = "align_justify_flex_end",
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
                        moveTo(20f, 22f)
                        verticalLineTo(2f)
                        horizontalLineToRelative(2f)
                        verticalLineTo(22f)
                        horizontalLineTo(20f)
                        close()
                        moveTo(14f, 17f)
                        verticalLineTo(7f)
                        horizontalLineToRelative(3f)
                        verticalLineTo(17f)
                        horizontalLineTo(14f)
                        close()
                        moveTo(8f, 17f)
                        verticalLineTo(7f)
                        horizontalLineToRelative(3f)
                        verticalLineTo(17f)
                        horizontalLineTo(8f)
                        close()
                    }
                }
                .build()
        return _align_justify_flex_end!!
    }

private var _align_justify_flex_end: ImageVector? = null
