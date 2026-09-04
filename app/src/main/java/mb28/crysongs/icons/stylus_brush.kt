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
public val stylus_brush: ImageVector
    get() {
        if (_stylus_brush != null) {
            return _stylus_brush!!
        }
        _stylus_brush =
            ImageVector.Builder(
                name = "stylus_brush",
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
                        moveTo(12f, 16f)
                        quadTo(9.5f, 16f, 7.75f, 14.26f)
                        reflectiveQuadTo(6f, 9.95f)
                        quadTo(6f, 8.1f, 7.1f, 6.47f)
                        reflectiveQuadTo(9.51f, 3.63f)
                        reflectiveQuadTo(11.91f, 1.7f)
                        reflectiveQuadTo(13f, 1f)
                        quadToRelative(0f, 1.42f, 0.51f, 2.4f)
                        reflectiveQuadToRelative(1.94f, 2.07f)
                        quadToRelative(1.47f, 1.15f, 2.01f, 2.14f)
                        reflectiveQuadTo(18f, 9.95f)
                        quadToRelative(0f, 2.57f, -1.75f, 4.31f)
                        reflectiveQuadTo(12f, 16f)
                        close()
                        moveToRelative(2.83f, -3.16f)
                        quadTo(16f, 11.68f, 16f, 9.95f)
                        quadTo(16f, 9.07f, 15.58f, 8.41f)
                        reflectiveQuadTo(14.18f, 7f)
                        quadTo(13.4f, 6.43f, 12.79f, 5.76f)
                        reflectiveQuadTo(11.75f, 4.3f)
                        quadTo(9.78f, 5.93f, 8.89f, 7.3f)
                        quadTo(8f, 8.67f, 8f, 9.95f)
                        quadToRelative(0f, 1.72f, 1.18f, 2.89f)
                        reflectiveQuadTo(12f, 14f)
                        reflectiveQuadToRelative(2.83f, -1.16f)
                        close()
                        moveTo(12f, 9.15f)
                        close()
                        moveTo(4f, 21f)
                        lineTo(4.55f, 19.38f)
                        quadToRelative(0.2f, -0.63f, 0.73f, -1f)
                        reflectiveQuadTo(6.45f, 18f)
                        horizontalLineToRelative(11.1f)
                        quadToRelative(0.65f, 0f, 1.18f, 0.38f)
                        reflectiveQuadToRelative(0.73f, 1f)
                        lineTo(20f, 21f)
                        horizontalLineTo(4f)
                        close()
                    }
                }
                .build()
        return _stylus_brush!!
    }

private var _stylus_brush: ImageVector? = null
