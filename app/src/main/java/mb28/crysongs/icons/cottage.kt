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
public val cottage: ImageVector
    get() {
        if (_cottage != null) {
            return _cottage!!
        }
        _cottage =
            ImageVector.Builder(
                name = "cottage",
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
                        moveTo(4f, 19f)
                        verticalLineTo(11.63f)
                        lineTo(3f, 12.4f)
                        quadTo(2.65f, 12.65f, 2.25f, 12.6f)
                        reflectiveQuadTo(1.6f, 12.2f)
                        reflectiveQuadTo(1.4f, 11.45f)
                        reflectiveQuadTo(1.78f, 10.8f)
                        lineTo(4f, 9.1f)
                        verticalLineTo(7f)
                        quadTo(4f, 6.57f, 4.29f, 6.29f)
                        reflectiveQuadTo(5f, 6f)
                        reflectiveQuadTo(5.71f, 6.29f)
                        reflectiveQuadTo(6f, 7f)
                        verticalLineTo(7.57f)
                        lineTo(10.78f, 3.92f)
                        quadTo(11.33f, 3.5f, 12f, 3.5f)
                        reflectiveQuadToRelative(1.23f, 0.42f)
                        lineToRelative(9f, 6.88f)
                        quadToRelative(0.32f, 0.25f, 0.38f, 0.65f)
                        reflectiveQuadTo(22.4f, 12.2f)
                        quadToRelative(-0.25f, 0.32f, -0.65f, 0.38f)
                        reflectiveQuadToRelative(-0.72f, -0.2f)
                        lineTo(20f, 11.63f)
                        verticalLineTo(19f)
                        quadToRelative(0f, 0.82f, -0.59f, 1.41f)
                        reflectiveQuadTo(18f, 21f)
                        horizontalLineTo(6f)
                        quadTo(5.18f, 21f, 4.59f, 20.41f)
                        reflectiveQuadTo(4f, 19f)
                        close()
                        moveToRelative(2f, 0f)
                        horizontalLineToRelative(5f)
                        verticalLineTo(16f)
                        quadToRelative(0f, -0.43f, 0.29f, -0.71f)
                        reflectiveQuadTo(12f, 15f)
                        reflectiveQuadToRelative(0.71f, 0.29f)
                        reflectiveQuadTo(13f, 16f)
                        verticalLineToRelative(3f)
                        horizontalLineToRelative(5f)
                        verticalLineTo(10.1f)
                        lineTo(12f, 5.52f)
                        lineTo(6f, 10.1f)
                        verticalLineTo(19f)
                        close()
                        moveToRelative(0f, 0f)
                        horizontalLineToRelative(5f)
                        horizontalLineToRelative(2f)
                        horizontalLineToRelative(5f)
                        horizontalLineTo(12f)
                        horizontalLineTo(6f)
                        close()
                        moveTo(5.3f, 5f)
                        quadTo(4.73f, 5f, 4.41f, 4.52f)
                        reflectiveQuadTo(4.38f, 3.55f)
                        quadTo(4.8f, 2.82f, 5.49f, 2.41f)
                        reflectiveQuadTo(7f, 2f)
                        quadTo(7.28f, 2f, 7.53f, 1.86f)
                        reflectiveQuadTo(7.9f, 1.47f)
                        quadTo(8.03f, 1.25f, 8.24f, 1.13f)
                        reflectiveQuadTo(8.73f, 1f)
                        quadTo(9.3f, 1f, 9.6f, 1.47f)
                        reflectiveQuadTo(9.63f, 2.45f)
                        quadTo(9.2f, 3.17f, 8.51f, 3.59f)
                        reflectiveQuadTo(7f, 4f)
                        quadTo(6.73f, 4f, 6.48f, 4.13f)
                        reflectiveQuadTo(6.1f, 4.52f)
                        quadTo(5.98f, 4.75f, 5.78f, 4.88f)
                        reflectiveQuadTo(5.3f, 5f)
                        close()
                    }
                }
                .build()
        return _cottage!!
    }

private var _cottage: ImageVector? = null
