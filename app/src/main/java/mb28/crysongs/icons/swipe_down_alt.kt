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
public val swipe_down_alt: ImageVector
    get() {
        if (_swipe_down_alt != null) {
            return _swipe_down_alt!!
        }
        _swipe_down_alt =
            ImageVector.Builder(
                name = "swipe_down_alt",
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
                        lineTo(8f, 18f)
                        lineTo(9.4f, 16.58f)
                        lineToRelative(1.6f, 1.6f)
                        verticalLineTo(13.9f)
                        quadTo(9.28f, 13.55f, 8.14f, 12.19f)
                        reflectiveQuadTo(7f, 9f)
                        quadTo(7f, 6.93f, 8.46f, 5.46f)
                        reflectiveQuadTo(12f, 4f)
                        reflectiveQuadToRelative(3.54f, 1.46f)
                        quadTo(17f, 6.93f, 17f, 9f)
                        quadToRelative(0f, 1.82f, -1.14f, 3.19f)
                        reflectiveQuadTo(13f, 13.9f)
                        verticalLineToRelative(4.28f)
                        lineTo(14.6f, 16.6f)
                        lineTo(16f, 18f)
                        lineToRelative(-4f, 4f)
                        close()
                        moveTo(14.13f, 11.13f)
                        quadTo(15f, 10.25f, 15f, 9f)
                        reflectiveQuadTo(14.13f, 6.88f)
                        reflectiveQuadTo(12f, 6f)
                        reflectiveQuadTo(9.88f, 6.88f)
                        reflectiveQuadTo(9f, 9f)
                        reflectiveQuadToRelative(0.88f, 2.13f)
                        reflectiveQuadTo(12f, 12f)
                        reflectiveQuadToRelative(2.13f, -0.88f)
                        close()
                        moveTo(12f, 9f)
                        close()
                    }
                }
                .build()
        return _swipe_down_alt!!
    }

private var _swipe_down_alt: ImageVector? = null
