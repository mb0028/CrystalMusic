package mb28.crysongs.ui.theme

import android.os.Environment
import androidx.compose.material3.Typography
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import mb28.crysongs.core.Settings
import java.io.File

val appFont: FontFamily get() {
    return if (Settings.loadFont) FontFamily(Font(File(
        "${Environment.getExternalStorageDirectory().path}/Documents/.Crystal/UI Font.ttf")))
    else FontFamily.Default
}

private val defaultTypography = Typography()

val Typography = Typography(
    displayLarge = defaultTypography.displayLarge.copy(fontFamily = appFont),
    displayMedium = defaultTypography.displayMedium.copy(fontFamily = appFont),
    displaySmall = defaultTypography.displaySmall.copy(fontFamily = appFont),

    headlineLarge = defaultTypography.headlineLarge.copy(fontFamily = appFont),
    headlineMedium = defaultTypography.headlineMedium.copy(fontFamily = appFont),
    headlineSmall = defaultTypography.headlineSmall.copy(fontFamily = appFont),

    titleLarge = defaultTypography.titleLarge.copy(fontFamily = appFont),
    titleMedium = defaultTypography.titleMedium.copy(fontFamily = appFont),
    titleSmall = defaultTypography.titleSmall.copy(fontFamily = appFont),

    bodyLarge = defaultTypography.bodyLarge.copy(fontFamily = appFont),
    bodyMedium = defaultTypography.bodyMedium.copy(fontFamily = appFont),
    bodySmall = defaultTypography.bodySmall.copy(fontFamily = appFont),

    labelLarge = defaultTypography.labelLarge.copy(fontFamily = appFont),
    labelMedium = defaultTypography.labelMedium.copy(fontFamily = appFont),
    labelSmall = defaultTypography.labelSmall.copy(fontFamily = appFont)
)