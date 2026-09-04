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
                        moveTo(7f, 22f)
                        lineTo(8.4f, 20.55f)
                        lineTo(6.85f, 19f)
                        horizontalLineTo(19f)
                        verticalLineTo(13f)
                        horizontalLineTo(17f)
                        verticalLineToRelative(4f)
                        horizontalLineTo(6.85f)
                        lineTo(8.4f, 15.45f)
                        lineTo(7f, 14f)
                        lineTo(3f, 18f)
                        lineToRelative(4f, 4f)
                        close()
                        moveTo(5f, 11f)
                        horizontalLineTo(7f)
                        verticalLineTo(7f)
                        horizontalLineTo(17.15f)
                        lineTo(15.6f, 8.55f)
                        lineTo(17f, 10f)
                        lineTo(21f, 6f)
                        lineTo(17f, 2f)
                        lineTo(15.6f, 3.45f)
                        lineTo(17.15f, 5f)
                        horizontalLineTo(5f)
                        verticalLineToRelative(6f)
                        close()
                    }
                }
                .build()
        return _repeat_on!!
    }

private var _repeat_on: ImageVector? = null
