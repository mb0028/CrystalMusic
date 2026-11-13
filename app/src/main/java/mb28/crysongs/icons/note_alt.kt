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
public val note_alt: ImageVector
    get() {
        if (_note_alt != null) {
            return _note_alt!!
        }
        _note_alt =
            ImageVector.Builder(
                name = "note_alt",
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
                        moveTo(5f, 21f)
                        quadTo(4.18f, 21f, 3.59f, 20.41f)
                        reflectiveQuadTo(3f, 19f)
                        verticalLineTo(5f)
                        quadTo(3f, 4.17f, 3.59f, 3.59f)
                        reflectiveQuadTo(5f, 3f)
                        horizontalLineTo(9.2f)
                        quadTo(9.53f, 2.1f, 10.29f, 1.55f)
                        reflectiveQuadTo(12f, 1f)
                        reflectiveQuadToRelative(1.71f, 0.55f)
                        reflectiveQuadTo(14.8f, 3f)
                        horizontalLineTo(19f)
                        quadToRelative(0.83f, 0f, 1.41f, 0.59f)
                        reflectiveQuadTo(21f, 5f)
                        verticalLineTo(19f)
                        quadToRelative(0f, 0.82f, -0.59f, 1.41f)
                        reflectiveQuadTo(19f, 21f)
                        horizontalLineTo(5f)
                        close()
                        moveTo(5f, 19f)
                        horizontalLineTo(19f)
                        verticalLineTo(5f)
                        horizontalLineTo(5f)
                        verticalLineTo(19f)
                        close()
                        moveTo(12.54f, 4.04f)
                        quadTo(12.75f, 3.82f, 12.75f, 3.5f)
                        quadToRelative(0f, -0.33f, -0.21f, -0.54f)
                        reflectiveQuadTo(12f, 2.75f)
                        reflectiveQuadTo(11.46f, 2.96f)
                        reflectiveQuadTo(11.25f, 3.5f)
                        quadToRelative(0f, 0.32f, 0.21f, 0.54f)
                        reflectiveQuadTo(12f, 4.25f)
                        reflectiveQuadTo(12.54f, 4.04f)
                        close()
                        moveTo(5f, 19f)
                        verticalLineTo(5f)
                        verticalLineTo(19f)
                        close()
                        moveTo(7.5f, 17f)
                        horizontalLineTo(8.7f)
                        quadToRelative(0.2f, 0f, 0.39f, -0.09f)
                        reflectiveQuadTo(9.4f, 16.7f)
                        lineToRelative(5.7f, -5.65f)
                        lineTo(12.95f, 8.9f)
                        lineTo(7.3f, 14.55f)
                        quadTo(7.15f, 14.7f, 7.08f, 14.89f)
                        reflectiveQuadTo(7f, 15.28f)
                        verticalLineTo(16.5f)
                        quadToRelative(0f, 0.2f, 0.15f, 0.35f)
                        reflectiveQuadTo(7.5f, 17f)
                        close()
                        moveToRelative(8.3f, -6.65f)
                        lineToRelative(1.05f, -1.1f)
                        quadTo(17f, 9.1f, 17f, 8.9f)
                        reflectiveQuadTo(16.85f, 8.55f)
                        lineToRelative(-1.4f, -1.4f)
                        quadTo(15.3f, 7f, 15.1f, 7f)
                        reflectiveQuadTo(14.75f, 7.15f)
                        lineTo(13.65f, 8.2f)
                        lineToRelative(2.15f, 2.15f)
                        close()
                    }
                }
                .build()
        return _note_alt!!
    }

private var _note_alt: ImageVector? = null
