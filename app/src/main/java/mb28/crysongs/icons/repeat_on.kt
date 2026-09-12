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
public val repeat_on: ImageVector
    get() {
        if (_repeat_on != null) {
            return _repeat_on!!
        }
        _repeat_on =
            ImageVector.Builder(
                name = "repeat_on",
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
                        moveTo(3f, 23f)
                        quadTo(2.18f, 23f, 1.59f, 22.41f)
                        reflectiveQuadTo(1f, 21f)
                        verticalLineTo(3f)
                        quadTo(1f, 2.17f, 1.59f, 1.59f)
                        reflectiveQuadTo(3f, 1f)
                        horizontalLineTo(21f)
                        quadToRelative(0.83f, 0f, 1.41f, 0.59f)
                        reflectiveQuadTo(23f, 3f)
                        verticalLineTo(21f)
                        quadToRelative(0f, 0.82f, -0.59f, 1.41f)
                        reflectiveQuadTo(21f, 23f)
                        horizontalLineTo(3f)
                        close()
                        moveTo(6.85f, 19f)
                        horizontalLineTo(17f)
                        quadToRelative(0.82f, 0f, 1.41f, -0.59f)
                        reflectiveQuadTo(19f, 17f)
                        verticalLineTo(14f)
                        quadToRelative(0f, -0.43f, -0.29f, -0.71f)
                        reflectiveQuadTo(18f, 13f)
                        reflectiveQuadToRelative(-0.71f, 0.29f)
                        reflectiveQuadTo(17f, 14f)
                        verticalLineToRelative(3f)
                        horizontalLineTo(6.85f)
                        lineTo(7.7f, 16.15f)
                        quadTo(8f, 15.85f, 7.99f, 15.45f)
                        reflectiveQuadTo(7.7f, 14.75f)
                        quadTo(7.4f, 14.45f, 6.99f, 14.44f)
                        reflectiveQuadTo(6.28f, 14.73f)
                        lineTo(3.7f, 17.3f)
                        quadTo(3.55f, 17.45f, 3.49f, 17.63f)
                        reflectiveQuadTo(3.43f, 18f)
                        reflectiveQuadToRelative(0.06f, 0.38f)
                        reflectiveQuadTo(3.7f, 18.7f)
                        lineToRelative(2.58f, 2.57f)
                        quadToRelative(0.3f, 0.3f, 0.71f, 0.29f)
                        reflectiveQuadTo(7.7f, 21.25f)
                        quadToRelative(0.28f, -0.3f, 0.29f, -0.7f)
                        reflectiveQuadTo(7.7f, 19.85f)
                        lineTo(6.85f, 19f)
                        close()
                        moveTo(17.15f, 7f)
                        lineTo(16.3f, 7.85f)
                        quadTo(16f, 8.15f, 16.01f, 8.55f)
                        quadToRelative(0.01f, 0.4f, 0.29f, 0.7f)
                        quadToRelative(0.3f, 0.3f, 0.71f, 0.31f)
                        quadToRelative(0.41f, 0.01f, 0.71f, -0.29f)
                        lineTo(20.3f, 6.7f)
                        quadTo(20.45f, 6.55f, 20.51f, 6.38f)
                        reflectiveQuadTo(20.58f, 6f)
                        reflectiveQuadTo(20.51f, 5.63f)
                        reflectiveQuadTo(20.3f, 5.3f)
                        lineTo(17.73f, 2.72f)
                        quadTo(17.43f, 2.42f, 17.01f, 2.44f)
                        reflectiveQuadTo(16.3f, 2.75f)
                        quadToRelative(-0.27f, 0.3f, -0.29f, 0.7f)
                        reflectiveQuadToRelative(0.29f, 0.7f)
                        lineTo(17.15f, 5f)
                        horizontalLineTo(7f)
                        quadTo(6.18f, 5f, 5.59f, 5.59f)
                        quadTo(5f, 6.18f, 5f, 7f)
                        verticalLineToRelative(3f)
                        quadToRelative(0f, 0.42f, 0.29f, 0.71f)
                        reflectiveQuadTo(6f, 11f)
                        reflectiveQuadTo(6.71f, 10.71f)
                        quadTo(7f, 10.43f, 7f, 10f)
                        verticalLineTo(7f)
                        horizontalLineTo(17.15f)
                        close()
                    }
                }
                .build()
        return _repeat_on!!
    }

private var _repeat_on: ImageVector? = null
