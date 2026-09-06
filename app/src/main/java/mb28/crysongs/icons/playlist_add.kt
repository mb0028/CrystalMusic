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
public val playlist_add: ImageVector
    get() {
        if (_playlist_add != null) {
            return _playlist_add!!
        }
        _playlist_add =
            ImageVector.Builder(
                name = "playlist_add",
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
                        moveTo(3f, 16f)
                        verticalLineTo(14f)
                        horizontalLineToRelative(7f)
                        verticalLineToRelative(2f)
                        horizontalLineTo(3f)
                        close()
                        moveTo(3f, 12f)
                        verticalLineTo(10f)
                        horizontalLineTo(14f)
                        verticalLineToRelative(2f)
                        horizontalLineTo(3f)
                        close()
                        moveTo(3f, 8f)
                        verticalLineTo(6f)
                        horizontalLineTo(14f)
                        verticalLineTo(8f)
                        horizontalLineTo(3f)
                        close()
                        moveTo(16f, 20f)
                        verticalLineTo(16f)
                        horizontalLineTo(12f)
                        verticalLineTo(14f)
                        horizontalLineToRelative(4f)
                        verticalLineTo(10f)
                        horizontalLineToRelative(2f)
                        verticalLineToRelative(4f)
                        horizontalLineToRelative(4f)
                        verticalLineToRelative(2f)
                        horizontalLineTo(18f)
                        verticalLineToRelative(4f)
                        horizontalLineTo(16f)
                        close()
                    }
                }
                .build()
        return _playlist_add!!
    }

private var _playlist_add: ImageVector? = null
