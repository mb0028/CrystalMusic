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
public val music_note_2: ImageVector
    get() {
        if (_music_note_2 != null) {
            return _music_note_2!!
        }
        _music_note_2 =
            ImageVector.Builder(
                name = "music_note_2",
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
                        moveTo(3.18f, 19.83f)
                        quadTo(2f, 18.65f, 2f, 17f)
                        reflectiveQuadTo(3.18f, 14.18f)
                        reflectiveQuadTo(6f, 13f)
                        quadToRelative(0.58f, 0f, 1.06f, 0.14f)
                        reflectiveQuadTo(8f, 13.55f)
                        verticalLineTo(5f)
                        lineTo(20f, 3f)
                        verticalLineTo(15f)
                        quadToRelative(0f, 1.65f, -1.18f, 2.82f)
                        reflectiveQuadTo(16f, 19f)
                        reflectiveQuadTo(13.18f, 17.83f)
                        reflectiveQuadTo(12f, 15f)
                        reflectiveQuadToRelative(1.18f, -2.83f)
                        reflectiveQuadTo(16f, 11f)
                        quadToRelative(0.57f, 0f, 1.06f, 0.14f)
                        reflectiveQuadTo(18f, 11.55f)
                        verticalLineTo(7.43f)
                        lineTo(10f, 9f)
                        verticalLineToRelative(8f)
                        quadToRelative(0f, 1.65f, -1.17f, 2.82f)
                        reflectiveQuadTo(6f, 21f)
                        reflectiveQuadTo(3.18f, 19.83f)
                        close()
                    }
                }
                .build()
        return _music_note_2!!
    }

private var _music_note_2: ImageVector? = null
