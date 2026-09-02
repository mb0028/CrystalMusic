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
public val library_music: ImageVector
    get() {
        if (_library_music != null) {
            return _library_music!!
        }
        _library_music =
            ImageVector.Builder(
                name = "library_music",
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
                        moveTo(12.5f, 15f)
                        quadToRelative(1.05f, 0f, 1.78f, -0.73f)
                        reflectiveQuadTo(15f, 12.5f)
                        verticalLineTo(7f)
                        horizontalLineToRelative(3f)
                        verticalLineTo(5f)
                        horizontalLineTo(14f)
                        verticalLineToRelative(5.5f)
                        quadTo(13.68f, 10.25f, 13.3f, 10.13f)
                        reflectiveQuadTo(12.5f, 10f)
                        quadToRelative(-1.05f, 0f, -1.77f, 0.72f)
                        reflectiveQuadTo(10f, 12.5f)
                        reflectiveQuadToRelative(0.73f, 1.77f)
                        reflectiveQuadTo(12.5f, 15f)
                        close()
                        moveTo(8f, 18f)
                        quadTo(7.18f, 18f, 6.59f, 17.41f)
                        reflectiveQuadTo(6f, 16f)
                        verticalLineTo(4f)
                        quadTo(6f, 3.17f, 6.59f, 2.59f)
                        reflectiveQuadTo(8f, 2f)
                        horizontalLineTo(20f)
                        quadToRelative(0.83f, 0f, 1.41f, 0.59f)
                        reflectiveQuadTo(22f, 4f)
                        verticalLineTo(16f)
                        quadToRelative(0f, 0.82f, -0.59f, 1.41f)
                        reflectiveQuadTo(20f, 18f)
                        horizontalLineTo(8f)
                        close()
                        moveTo(8f, 16f)
                        horizontalLineTo(20f)
                        verticalLineTo(4f)
                        horizontalLineTo(8f)
                        verticalLineTo(16f)
                        close()
                        moveTo(4f, 22f)
                        quadTo(3.18f, 22f, 2.59f, 21.41f)
                        reflectiveQuadTo(2f, 20f)
                        verticalLineTo(6f)
                        horizontalLineTo(4f)
                        verticalLineTo(20f)
                        horizontalLineTo(18f)
                        verticalLineToRelative(2f)
                        horizontalLineTo(4f)
                        close()
                        moveTo(8f, 4f)
                        verticalLineTo(16f)
                        verticalLineTo(4f)
                        close()
                    }
                }
                .build()
        return _library_music!!
    }

private var _library_music: ImageVector? = null
