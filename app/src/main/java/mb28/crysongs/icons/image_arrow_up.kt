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
public val image_arrow_up: ImageVector
    get() {
        if (_image_arrow_up != null) {
            return _image_arrow_up!!
        }
        _image_arrow_up =
            ImageVector.Builder(
                name = "image_arrow_up",
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
                        moveTo(12f, 12f)
                        close()
                        moveTo(5f, 21f)
                        quadTo(4.18f, 21f, 3.59f, 20.41f)
                        reflectiveQuadTo(3f, 19f)
                        verticalLineTo(5f)
                        quadTo(3f, 4.17f, 3.59f, 3.59f)
                        reflectiveQuadTo(5f, 3f)
                        horizontalLineToRelative(7f)
                        quadToRelative(0.43f, 0f, 0.71f, 0.29f)
                        reflectiveQuadTo(13f, 4f)
                        quadToRelative(0f, 0.42f, -0.29f, 0.71f)
                        reflectiveQuadTo(12f, 5f)
                        horizontalLineTo(5f)
                        verticalLineTo(19f)
                        horizontalLineTo(19f)
                        verticalLineTo(13f)
                        quadToRelative(0f, -0.43f, 0.29f, -0.71f)
                        reflectiveQuadTo(20f, 12f)
                        quadToRelative(0.43f, 0f, 0.71f, 0.29f)
                        reflectiveQuadTo(21f, 13f)
                        verticalLineToRelative(6f)
                        quadToRelative(0f, 0.82f, -0.59f, 1.41f)
                        reflectiveQuadTo(19f, 21f)
                        horizontalLineTo(5f)
                        close()
                        moveTo(18f, 5.82f)
                        lineTo(17.1f, 6.7f)
                        quadTo(16.83f, 6.97f, 16.41f, 6.99f)
                        reflectiveQuadTo(15.7f, 6.7f)
                        quadTo(15.43f, 6.43f, 15.43f, 6f)
                        reflectiveQuadTo(15.7f, 5.3f)
                        lineTo(18.3f, 2.7f)
                        quadTo(18.45f, 2.55f, 18.63f, 2.47f)
                        reflectiveQuadTo(19f, 2.4f)
                        reflectiveQuadToRelative(0.38f, 0.07f)
                        reflectiveQuadTo(19.7f, 2.7f)
                        lineToRelative(2.6f, 2.6f)
                        quadToRelative(0.28f, 0.27f, 0.29f, 0.69f)
                        reflectiveQuadTo(22.3f, 6.7f)
                        quadTo(22.03f, 6.97f, 21.6f, 6.97f)
                        reflectiveQuadTo(20.9f, 6.7f)
                        lineTo(20f, 5.82f)
                        verticalLineTo(9f)
                        quadToRelative(0f, 0.42f, -0.29f, 0.71f)
                        reflectiveQuadTo(19f, 10f)
                        reflectiveQuadTo(18.29f, 9.71f)
                        reflectiveQuadTo(18f, 9f)
                        verticalLineTo(5.82f)
                        close()
                        moveTo(7f, 17f)
                        horizontalLineTo(17f)
                        quadToRelative(0.3f, 0f, 0.45f, -0.27f)
                        reflectiveQuadTo(17.4f, 16.2f)
                        lineTo(14.65f, 12.52f)
                        quadToRelative(-0.15f, -0.2f, -0.4f, -0.2f)
                        reflectiveQuadToRelative(-0.4f, 0.2f)
                        lineTo(11.25f, 16f)
                        lineTo(9.4f, 13.52f)
                        quadTo(9.25f, 13.33f, 9f, 13.33f)
                        reflectiveQuadToRelative(-0.4f, 0.2f)
                        lineToRelative(-2f, 2.68f)
                        quadTo(6.4f, 16.45f, 6.55f, 16.73f)
                        reflectiveQuadTo(7f, 17f)
                        close()
                    }
                }
                .build()
        return _image_arrow_up!!
    }

private var _image_arrow_up: ImageVector? = null
