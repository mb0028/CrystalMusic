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
val flag: ImageVector
    get() {
        if (_flag != null) {
            return _flag!!
        }
        _flag =
            ImageVector.Builder(
                name = "flag",
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
                        moveTo(7f, 14f)
                        verticalLineToRelative(6f)
                        quadToRelative(0f, 0.43f, -0.29f, 0.71f)
                        reflectiveQuadTo(6f, 21f)
                        quadTo(5.58f, 21f, 5.29f, 20.71f)
                        quadTo(5f, 20.43f, 5f, 20f)
                        verticalLineTo(5f)
                        quadTo(5f, 4.57f, 5.29f, 4.29f)
                        reflectiveQuadTo(6f, 4f)
                        horizontalLineToRelative(7.18f)
                        quadToRelative(0.35f, 0f, 0.63f, 0.22f)
                        reflectiveQuadTo(14.15f, 4.8f)
                        lineTo(14.4f, 6f)
                        horizontalLineTo(19f)
                        quadToRelative(0.43f, 0f, 0.71f, 0.29f)
                        reflectiveQuadTo(20f, 7f)
                        verticalLineToRelative(8f)
                        quadToRelative(0f, 0.42f, -0.29f, 0.71f)
                        reflectiveQuadTo(19f, 16f)
                        horizontalLineTo(13.83f)
                        quadTo(13.48f, 16f, 13.2f, 15.78f)
                        reflectiveQuadTo(12.85f, 15.2f)
                        lineTo(12.6f, 14f)
                        horizontalLineTo(7f)
                        close()
                        moveToRelative(7.65f, 0f)
                        horizontalLineTo(18f)
                        verticalLineTo(8f)
                        horizontalLineTo(13.58f)
                        quadTo(13.23f, 8f, 12.95f, 7.77f)
                        reflectiveQuadTo(12.6f, 7.2f)
                        lineTo(12.35f, 6f)
                        horizontalLineTo(7f)
                        verticalLineToRelative(6f)
                        horizontalLineToRelative(6.43f)
                        quadToRelative(0.35f, 0f, 0.63f, 0.22f)
                        reflectiveQuadTo(14.4f, 12.8f)
                        lineTo(14.65f, 14f)
                        close()
                        moveTo(12.5f, 10f)
                        close()
                    }
                }
                .build()
        return _flag!!
    }

private var _flag: ImageVector? = null
