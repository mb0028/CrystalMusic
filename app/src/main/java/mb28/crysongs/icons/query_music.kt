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
public val queue_music: ImageVector
    get() {
        if (_queue_music != null) {
            return _queue_music!!
        }
        _queue_music =
            ImageVector.Builder(
                name = "queue_music",
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
                        moveTo(16f, 20f)
                        quadToRelative(-1.25f, 0f, -2.13f, -0.88f)
                        reflectiveQuadTo(13f, 17f)
                        reflectiveQuadToRelative(0.88f, -2.13f)
                        reflectiveQuadTo(16f, 14f)
                        quadToRelative(0.28f, 0f, 0.53f, 0.04f)
                        reflectiveQuadTo(17f, 14.2f)
                        verticalLineTo(6f)
                        horizontalLineToRelative(5f)
                        verticalLineTo(8f)
                        horizontalLineTo(19f)
                        verticalLineToRelative(9f)
                        quadToRelative(0f, 1.25f, -0.88f, 2.13f)
                        reflectiveQuadTo(16f, 20f)
                        close()
                        moveTo(3f, 16f)
                        verticalLineTo(14f)
                        horizontalLineToRelative(8f)
                        verticalLineToRelative(2f)
                        horizontalLineTo(3f)
                        close()
                        moveTo(3f, 12f)
                        verticalLineTo(10f)
                        horizontalLineTo(15f)
                        verticalLineToRelative(2f)
                        horizontalLineTo(3f)
                        close()
                        moveTo(3f, 8f)
                        verticalLineTo(6f)
                        horizontalLineTo(15f)
                        verticalLineTo(8f)
                        horizontalLineTo(3f)
                        close()
                    }
                }
                .build()
        return _queue_music!!
    }

private var _queue_music: ImageVector? = null
