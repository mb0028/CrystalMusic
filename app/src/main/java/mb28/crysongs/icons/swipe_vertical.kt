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
public val swipe_vertical: ImageVector
    get() {
        if (_swipe_vertical != null) {
            return _swipe_vertical!!
        }
        _swipe_vertical =
            ImageVector.Builder(
                name = "swipe_vertical",
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
                        moveTo(2f, 22f)
                        verticalLineTo(20.5f)
                        horizontalLineTo(4.03f)
                        quadTo(2.55f, 18.7f, 1.78f, 16.52f)
                        reflectiveQuadTo(1f, 12f)
                        reflectiveQuadTo(1.78f, 7.47f)
                        reflectiveQuadTo(4.03f, 3.5f)
                        horizontalLineTo(2f)
                        verticalLineTo(2f)
                        horizontalLineTo(7f)
                        verticalLineTo(7f)
                        horizontalLineTo(5.5f)
                        verticalLineTo(4.1f)
                        quadTo(4.05f, 5.75f, 3.28f, 7.77f)
                        reflectiveQuadTo(2.5f, 12f)
                        reflectiveQuadToRelative(0.78f, 4.23f)
                        reflectiveQuadTo(5.5f, 19.9f)
                        verticalLineTo(17f)
                        horizontalLineTo(7f)
                        verticalLineToRelative(5f)
                        horizontalLineTo(2f)
                        close()
                        moveTo(16.45f, 20.83f)
                        quadToRelative(-0.57f, 0.2f, -1.16f, 0.19f)
                        reflectiveQuadTo(14.15f, 20.73f)
                        lineTo(7.6f, 17.68f)
                        lineToRelative(0.45f, -1f)
                        quadToRelative(0.25f, -0.5f, 0.7f, -0.81f)
                        reflectiveQuadToRelative(1f, -0.36f)
                        lineToRelative(1.7f, -0.13f)
                        lineTo(8.65f, 7.7f)
                        quadTo(8.5f, 7.3f, 8.68f, 6.94f)
                        reflectiveQuadTo(9.25f, 6.43f)
                        quadToRelative(0.4f, -0.15f, 0.76f, 0.02f)
                        reflectiveQuadToRelative(0.51f, 0.57f)
                        lineToRelative(3.7f, 10.18f)
                        lineToRelative(-2.5f, 0.18f)
                        lineTo(15f, 18.9f)
                        quadToRelative(0.18f, 0.08f, 0.38f, 0.09f)
                        reflectiveQuadToRelative(0.38f, -0.04f)
                        lineToRelative(3.93f, -1.43f)
                        quadToRelative(0.78f, -0.27f, 1.13f, -1.04f)
                        reflectiveQuadToRelative(0.07f, -1.54f)
                        lineTo(19.5f, 11.2f)
                        quadToRelative(-0.15f, -0.4f, 0.03f, -0.76f)
                        quadTo(19.7f, 10.07f, 20.1f, 9.92f)
                        reflectiveQuadToRelative(0.76f, 0.03f)
                        reflectiveQuadToRelative(0.51f, 0.57f)
                        lineToRelative(1.38f, 3.75f)
                        quadToRelative(0.58f, 1.58f, -0.11f, 3.06f)
                        quadToRelative(-0.69f, 1.49f, -2.26f, 2.06f)
                        lineToRelative(-3.93f, 1.43f)
                        close()
                        moveTo(14.2f, 14.2f)
                        lineTo(12.85f, 10.43f)
                        quadTo(12.7f, 10.02f, 12.88f, 9.66f)
                        reflectiveQuadTo(13.45f, 9.15f)
                        reflectiveQuadToRelative(0.76f, 0.03f)
                        reflectiveQuadToRelative(0.51f, 0.58f)
                        lineTo(16.1f, 13.5f)
                        lineToRelative(-1.9f, 0.7f)
                        close()
                        moveToRelative(2.83f, -1.03f)
                        lineTo(16f, 10.35f)
                        quadTo(15.85f, 9.95f, 16.03f, 9.59f)
                        quadTo(16.2f, 9.23f, 16.6f, 9.07f)
                        reflectiveQuadTo(17.36f, 9.1f)
                        quadToRelative(0.36f, 0.17f, 0.51f, 0.57f)
                        lineToRelative(1.03f, 2.8f)
                        lineToRelative(-1.88f, 0.7f)
                        close()
                        moveToRelative(0.2f, 2.2f)
                        close()
                    }
                }
                .build()
        return _swipe_vertical!!
    }

private var _swipe_vertical: ImageVector? = null
