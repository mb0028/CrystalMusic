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
public val timer_arrow_down: ImageVector
    get() {
        if (_timer_arrow_down != null) {
            return _timer_arrow_down!!
        }
        _timer_arrow_down =
            ImageVector.Builder(
                name = "timer_arrow_down",
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
                        moveTo(3.19f, 17.81f)
                        quadTo(1f, 15.63f, 1f, 12.5f)
                        reflectiveQuadTo(3.19f, 7.19f)
                        reflectiveQuadTo(8.5f, 5f)
                        quadToRelative(1.3f, 0f, 2.45f, 0.41f)
                        reflectiveQuadToRelative(2.1f, 1.14f)
                        lineTo(13.4f, 6.2f)
                        quadTo(13.68f, 5.93f, 14.09f, 5.91f)
                        reflectiveQuadTo(14.8f, 6.2f)
                        quadToRelative(0.28f, 0.27f, 0.28f, 0.7f)
                        reflectiveQuadTo(14.8f, 7.6f)
                        lineTo(14.45f, 7.95f)
                        quadToRelative(0.72f, 0.95f, 1.14f, 2.11f)
                        reflectiveQuadTo(16f, 12.5f)
                        quadToRelative(0f, 3.13f, -2.19f, 5.31f)
                        reflectiveQuadTo(8.5f, 20f)
                        reflectiveQuadTo(3.19f, 17.81f)
                        close()
                        moveTo(7f, 4f)
                        quadTo(6.58f, 4f, 6.29f, 3.71f)
                        reflectiveQuadTo(6f, 3f)
                        quadTo(6f, 2.57f, 6.29f, 2.29f)
                        reflectiveQuadTo(7f, 2f)
                        horizontalLineToRelative(3f)
                        quadToRelative(0.43f, 0f, 0.71f, 0.29f)
                        reflectiveQuadTo(11f, 3f)
                        quadToRelative(0f, 0.42f, -0.29f, 0.71f)
                        reflectiveQuadTo(10f, 4f)
                        horizontalLineTo(7f)
                        close()
                        moveToRelative(5.4f, 12.4f)
                        quadTo(14f, 14.8f, 14f, 12.5f)
                        reflectiveQuadTo(12.4f, 8.6f)
                        reflectiveQuadTo(8.5f, 7f)
                        reflectiveQuadTo(4.6f, 8.6f)
                        reflectiveQuadTo(3f, 12.5f)
                        reflectiveQuadToRelative(1.6f, 3.9f)
                        reflectiveQuadTo(8.5f, 18f)
                        reflectiveQuadToRelative(3.9f, -1.6f)
                        close()
                        moveTo(9.21f, 13.21f)
                        quadTo(9.5f, 12.93f, 9.5f, 12.5f)
                        verticalLineToRelative(-3f)
                        quadTo(9.5f, 9.07f, 9.21f, 8.79f)
                        reflectiveQuadTo(8.5f, 8.5f)
                        quadTo(8.08f, 8.5f, 7.79f, 8.79f)
                        reflectiveQuadTo(7.5f, 9.5f)
                        verticalLineToRelative(3f)
                        quadToRelative(0f, 0.42f, 0.29f, 0.71f)
                        reflectiveQuadTo(8.5f, 13.5f)
                        quadToRelative(0.43f, 0f, 0.71f, -0.29f)
                        close()
                        moveTo(8.5f, 12.5f)
                        close()
                        moveToRelative(10.3f, 6.8f)
                        lineTo(16.5f, 17f)
                        quadTo(16.23f, 16.7f, 16.23f, 16.29f)
                        reflectiveQuadToRelative(0.3f, -0.69f)
                        reflectiveQuadToRelative(0.71f, -0.29f)
                        reflectiveQuadToRelative(0.69f, 0.29f)
                        lineToRelative(0.57f, 0.58f)
                        verticalLineTo(5f)
                        quadToRelative(0f, -0.43f, 0.29f, -0.71f)
                        reflectiveQuadTo(19.5f, 4f)
                        reflectiveQuadToRelative(0.71f, 0.29f)
                        reflectiveQuadTo(20.5f, 5f)
                        verticalLineTo(16.2f)
                        lineToRelative(0.6f, -0.6f)
                        quadToRelative(0.27f, -0.28f, 0.7f, -0.28f)
                        reflectiveQuadToRelative(0.7f, 0.28f)
                        reflectiveQuadToRelative(0.28f, 0.7f)
                        reflectiveQuadTo(22.5f, 17f)
                        lineToRelative(-2.3f, 2.3f)
                        quadToRelative(-0.3f, 0.3f, -0.7f, 0.3f)
                        reflectiveQuadTo(18.8f, 19.3f)
                        close()
                    }
                }
                .build()
        return _timer_arrow_down!!
    }

private var _timer_arrow_down: ImageVector? = null
