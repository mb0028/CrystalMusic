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
public val swipe_up_alt: ImageVector
    get() {
        if (_swipe_up_alt != null) {
            return _swipe_up_alt!!
        }
        _swipe_up_alt =
            ImageVector.Builder(
                name = "swipe_up_alt",
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
                        moveTo(8.46f, 18.54f)
                        quadTo(7f, 17.08f, 7f, 15f)
                        quadTo(7f, 13.18f, 8.14f, 11.81f)
                        reflectiveQuadTo(11f, 10.1f)
                        verticalLineTo(5.82f)
                        lineTo(9.4f, 7.4f)
                        lineTo(8f, 6f)
                        lineTo(12f, 2f)
                        lineToRelative(4f, 4f)
                        lineTo(14.6f, 7.43f)
                        lineTo(13f, 5.82f)
                        verticalLineTo(10.1f)
                        quadToRelative(1.73f, 0.35f, 2.86f, 1.71f)
                        reflectiveQuadTo(17f, 15f)
                        quadToRelative(0f, 2.07f, -1.46f, 3.54f)
                        reflectiveQuadTo(12f, 20f)
                        quadTo(9.93f, 20f, 8.46f, 18.54f)
                        close()
                        moveToRelative(5.66f, -1.41f)
                        quadTo(15f, 16.25f, 15f, 15f)
                        reflectiveQuadTo(14.13f, 12.88f)
                        reflectiveQuadTo(12f, 12f)
                        reflectiveQuadTo(9.88f, 12.88f)
                        reflectiveQuadTo(9f, 15f)
                        reflectiveQuadToRelative(0.88f, 2.13f)
                        reflectiveQuadTo(12f, 18f)
                        reflectiveQuadToRelative(2.13f, -0.88f)
                        close()
                        moveTo(12f, 15f)
                        close()
                    }
                }
                .build()
        return _swipe_up_alt!!
    }

private var _swipe_up_alt: ImageVector? = null
