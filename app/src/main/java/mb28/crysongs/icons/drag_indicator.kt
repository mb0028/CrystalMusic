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
public val drag_indicator: ImageVector
    get() {
        if (_drag_indicator != null) {
            return _drag_indicator!!
        }
        _drag_indicator =
            ImageVector.Builder(
                name = "drag_indicator",
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
                        moveTo(9f, 20f)
                        quadTo(8.18f, 20f, 7.59f, 19.41f)
                        reflectiveQuadTo(7f, 18f)
                        reflectiveQuadTo(7.59f, 16.59f)
                        reflectiveQuadTo(9f, 16f)
                        quadToRelative(0.83f, 0f, 1.41f, 0.59f)
                        quadTo(11f, 17.18f, 11f, 18f)
                        reflectiveQuadToRelative(-0.59f, 1.41f)
                        reflectiveQuadTo(9f, 20f)
                        close()
                        moveToRelative(6f, 0f)
                        quadToRelative(-0.82f, 0f, -1.41f, -0.59f)
                        reflectiveQuadTo(13f, 18f)
                        reflectiveQuadToRelative(0.59f, -1.41f)
                        reflectiveQuadTo(15f, 16f)
                        reflectiveQuadToRelative(1.41f, 0.59f)
                        quadTo(17f, 17.18f, 17f, 18f)
                        reflectiveQuadToRelative(-0.59f, 1.41f)
                        reflectiveQuadTo(15f, 20f)
                        close()
                        moveTo(9f, 14f)
                        quadTo(8.18f, 14f, 7.59f, 13.41f)
                        reflectiveQuadTo(7f, 12f)
                        reflectiveQuadTo(7.59f, 10.59f)
                        reflectiveQuadTo(9f, 10f)
                        quadToRelative(0.83f, 0f, 1.41f, 0.59f)
                        quadTo(11f, 11.18f, 11f, 12f)
                        reflectiveQuadToRelative(-0.59f, 1.41f)
                        reflectiveQuadTo(9f, 14f)
                        close()
                        moveToRelative(6f, 0f)
                        quadToRelative(-0.82f, 0f, -1.41f, -0.59f)
                        reflectiveQuadTo(13f, 12f)
                        reflectiveQuadToRelative(0.59f, -1.41f)
                        reflectiveQuadTo(15f, 10f)
                        reflectiveQuadToRelative(1.41f, 0.59f)
                        quadTo(17f, 11.18f, 17f, 12f)
                        reflectiveQuadToRelative(-0.59f, 1.41f)
                        reflectiveQuadTo(15f, 14f)
                        close()
                        moveTo(9f, 8f)
                        quadTo(8.18f, 8f, 7.59f, 7.41f)
                        reflectiveQuadTo(7f, 6f)
                        reflectiveQuadTo(7.59f, 4.59f)
                        reflectiveQuadTo(9f, 4f)
                        quadToRelative(0.83f, 0f, 1.41f, 0.59f)
                        quadTo(11f, 5.18f, 11f, 6f)
                        reflectiveQuadTo(10.41f, 7.41f)
                        reflectiveQuadTo(9f, 8f)
                        close()
                        moveToRelative(6f, 0f)
                        quadTo(14.18f, 8f, 13.59f, 7.41f)
                        reflectiveQuadTo(13f, 6f)
                        reflectiveQuadTo(13.59f, 4.59f)
                        reflectiveQuadTo(15f, 4f)
                        reflectiveQuadToRelative(1.41f, 0.59f)
                        quadTo(17f, 5.18f, 17f, 6f)
                        reflectiveQuadTo(16.41f, 7.41f)
                        reflectiveQuadTo(15f, 8f)
                        close()
                    }
                }
                .build()
        return _drag_indicator!!
    }

private var _drag_indicator: ImageVector? = null
