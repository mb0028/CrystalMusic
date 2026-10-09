package mb28.monoP.icons


import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

@Suppress("CheckReturnValue")
public val add_2: ImageVector
    get() {
        if (_add_2 != null) {
            return _add_2!!
        }
        _add_2 =
            ImageVector.Builder(
                name = "add_2",
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
                        moveTo(11.29f, 20.71f)
                        quadTo(11f, 20.43f, 11f, 20f)
                        verticalLineTo(13f)
                        horizontalLineTo(4f)
                        quadTo(3.58f, 13f, 3.29f, 12.71f)
                        quadTo(3f, 12.43f, 3f, 12f)
                        reflectiveQuadTo(3.29f, 11.29f)
                        reflectiveQuadTo(4f, 11f)
                        horizontalLineToRelative(7f)
                        verticalLineTo(4f)
                        quadTo(11f, 3.57f, 11.29f, 3.29f)
                        reflectiveQuadTo(12f, 3f)
                        reflectiveQuadToRelative(0.71f, 0.29f)
                        reflectiveQuadTo(13f, 4f)
                        verticalLineToRelative(7f)
                        horizontalLineToRelative(7f)
                        quadToRelative(0.43f, 0f, 0.71f, 0.29f)
                        reflectiveQuadTo(21f, 12f)
                        reflectiveQuadToRelative(-0.29f, 0.71f)
                        reflectiveQuadTo(20f, 13f)
                        horizontalLineTo(13f)
                        verticalLineToRelative(7f)
                        quadToRelative(0f, 0.43f, -0.29f, 0.71f)
                        reflectiveQuadTo(12f, 21f)
                        reflectiveQuadTo(11.29f, 20.71f)
                        close()
                    }
                }
                .build()
        return _add_2!!
    }

private var _add_2: ImageVector? = null
