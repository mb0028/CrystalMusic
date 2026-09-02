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
public val artist: ImageVector
    get() {
        if (_artist != null) {
            return _artist!!
        }
        _artist =
            ImageVector.Builder(
                name = "artist",
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
                        moveTo(18.5f, 10f)
                        horizontalLineTo(22f)
                        verticalLineToRelative(2f)
                        horizontalLineTo(20f)
                        verticalLineToRelative(5.5f)
                        quadToRelative(0f, 1.05f, -0.72f, 1.77f)
                        reflectiveQuadTo(17.5f, 20f)
                        reflectiveQuadTo(15.73f, 19.27f)
                        reflectiveQuadTo(15f, 17.5f)
                        reflectiveQuadToRelative(0.73f, -1.78f)
                        reflectiveQuadTo(17.5f, 15f)
                        quadToRelative(0.2f, 0f, 0.45f, 0.04f)
                        reflectiveQuadTo(18.5f, 15.2f)
                        verticalLineTo(10f)
                        close()
                        moveTo(3f, 20f)
                        verticalLineTo(17.2f)
                        quadTo(3f, 16.33f, 3.44f, 15.63f)
                        reflectiveQuadTo(4.6f, 14.55f)
                        quadTo(6.15f, 13.77f, 7.75f, 13.39f)
                        reflectiveQuadTo(11f, 13f)
                        quadToRelative(1.05f, 0f, 2.09f, 0.16f)
                        reflectiveQuadToRelative(2.09f, 0.49f)
                        quadToRelative(-0.5f, 0.3f, -0.9f, 0.73f)
                        reflectiveQuadToRelative(-0.7f, 0.92f)
                        quadTo(12.93f, 15.15f, 12.29f, 15.08f)
                        reflectiveQuadTo(11f, 15f)
                        quadTo(9.58f, 15f, 8.2f, 15.35f)
                        reflectiveQuadToRelative(-2.7f, 1f)
                        quadTo(5.28f, 16.48f, 5.14f, 16.7f)
                        quadTo(5f, 16.93f, 5f, 17.2f)
                        verticalLineTo(18f)
                        horizontalLineToRelative(8.03f)
                        quadToRelative(0.05f, 0.5f, 0.24f, 1f)
                        reflectiveQuadToRelative(0.51f, 1f)
                        horizontalLineTo(3f)
                        close()
                        moveTo(8.18f, 10.83f)
                        quadTo(7f, 9.65f, 7f, 8f)
                        reflectiveQuadTo(8.18f, 5.18f)
                        reflectiveQuadTo(11f, 4f)
                        reflectiveQuadToRelative(2.83f, 1.18f)
                        reflectiveQuadTo(15f, 8f)
                        reflectiveQuadToRelative(-1.17f, 2.82f)
                        reflectiveQuadTo(11f, 12f)
                        reflectiveQuadTo(8.18f, 10.83f)
                        close()
                        moveTo(12.41f, 9.41f)
                        quadTo(13f, 8.82f, 13f, 8f)
                        reflectiveQuadTo(12.41f, 6.59f)
                        reflectiveQuadTo(11f, 6f)
                        quadTo(10.18f, 6f, 9.59f, 6.59f)
                        quadTo(9f, 7.18f, 9f, 8f)
                        reflectiveQuadTo(9.59f, 9.41f)
                        reflectiveQuadTo(11f, 10f)
                        reflectiveQuadTo(12.41f, 9.41f)
                        close()
                        moveTo(11f, 8f)
                        close()
                        moveToRelative(0f, 10f)
                        close()
                    }
                }
                .build()
        return _artist!!
    }

private var _artist: ImageVector? = null
