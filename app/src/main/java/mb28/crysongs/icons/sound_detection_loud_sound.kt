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
public val sound_detection_loud_sound: ImageVector
    get() {
        if (_sound_detection_loud_sound != null) {
            return _sound_detection_loud_sound!!
        }
        _sound_detection_loud_sound =
            ImageVector.Builder(
                name = "sound_detection_loud_sound",
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
                        moveTo(17.73f, 17.63f)
                        horizontalLineTo(12.05f)
                        lineTo(9.23f, 20.45f)
                        quadTo(8.65f, 21.03f, 7.81f, 21.03f)
                        reflectiveQuadTo(6.4f, 20.45f)
                        lineTo(3.58f, 17.63f)
                        quadTo(3f, 17.05f, 3f, 16.2f)
                        reflectiveQuadTo(3.58f, 14.78f)
                        lineToRelative(2.8f, -2.8f)
                        verticalLineTo(6.3f)
                        lineTo(17.73f, 17.63f)
                        close()
                        moveToRelative(-4.83f, -2f)
                        lineTo(8.38f, 11.1f)
                        verticalLineToRelative(1.7f)
                        lineToRelative(-3.4f, 3.4f)
                        lineTo(7.8f, 19.02f)
                        lineToRelative(3.4f, -3.4f)
                        horizontalLineToRelative(1.7f)
                        close()
                        moveTo(7.23f, 4.38f)
                        quadTo(9.9f, 2.67f, 13.01f, 3.01f)
                        reflectiveQuadTo(18.38f, 5.6f)
                        reflectiveQuadToRelative(2.59f, 5.36f)
                        reflectiveQuadTo(19.6f, 16.75f)
                        lineTo(18.15f, 15.3f)
                        quadToRelative(1.13f, -2.05f, 0.79f, -4.34f)
                        reflectiveQuadTo(16.95f, 7.02f)
                        quadTo(15.3f, 5.38f, 13.01f, 5.04f)
                        reflectiveQuadTo(8.68f, 5.82f)
                        lineTo(7.23f, 4.38f)
                        close()
                        moveToRelative(2.95f, 2.95f)
                        quadTo(11.6f, 6.9f, 13.05f, 7.15f)
                        reflectiveQuadToRelative(2.5f, 1.3f)
                        reflectiveQuadToRelative(1.29f, 2.49f)
                        reflectiveQuadTo(16.65f, 13.8f)
                        lineToRelative(-1.7f, -1.7f)
                        quadToRelative(0f, -0.63f, -0.19f, -1.21f)
                        reflectiveQuadTo(14.15f, 9.88f)
                        quadTo(13.7f, 9.42f, 13.11f, 9.23f)
                        quadTo(12.53f, 9.02f, 11.88f, 9.02f)
                        lineToRelative(-1.7f, -1.7f)
                        close()
                        moveTo(8.95f, 15.05f)
                        close()
                    }
                }
                .build()
        return _sound_detection_loud_sound!!
    }

private var _sound_detection_loud_sound: ImageVector? = null
