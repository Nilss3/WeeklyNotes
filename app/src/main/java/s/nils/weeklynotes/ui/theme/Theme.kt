package s.nils.weeklynotes.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

enum class ColorScheme {
    LIGHT,      // White background, black text (current e-ink mode)
    DARK,       // Black background, white text
    SYSTEM,     // Follow Android system theme
    CUSTOM      // Custom colors
}

private val EInkColorScheme = lightColorScheme(
    primary = Color.Black,
    secondary = Color.Black,
    tertiary = Color.Black,
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color.Black,
    onSurface = Color.Black,
    surfaceVariant = Color.White,
    onSurfaceVariant = Color.Black
)

private val DarkColorScheme = darkColorScheme(
    primary = Color.White,
    secondary = Color.White,
    tertiary = Color.White,
    background = Color.Black,
    surface = Color.Black,
    onPrimary = Color.Black,
    onSecondary = Color.Black,
    onTertiary = Color.Black,
    onBackground = Color.White,
    onSurface = Color.White,
    surfaceVariant = Color.Black,
    onSurfaceVariant = Color.White
)

@Composable
fun WeeklyNotesTheme(
    colorScheme: ColorScheme = ColorScheme.LIGHT,
    customTextColor: androidx.compose.ui.graphics.Color = androidx.compose.ui.graphics.Color.Black,
    customBackgroundColor: androidx.compose.ui.graphics.Color = androidx.compose.ui.graphics.Color.White,
    content: @Composable () -> Unit
) {
    val colorSchemeToUse = when (colorScheme) {
        ColorScheme.LIGHT -> EInkColorScheme
        ColorScheme.DARK -> DarkColorScheme
        ColorScheme.SYSTEM -> if (isSystemInDarkTheme()) DarkColorScheme else EInkColorScheme
        ColorScheme.CUSTOM -> lightColorScheme(
            primary = customTextColor,
            secondary = customTextColor,
            tertiary = customTextColor,
            background = customBackgroundColor,
            surface = customBackgroundColor,
            onPrimary = customBackgroundColor,
            onSecondary = customBackgroundColor,
            onTertiary = customBackgroundColor,
            onBackground = customTextColor,
            onSurface = customTextColor,
            surfaceVariant = customBackgroundColor,
            onSurfaceVariant = customTextColor
        )
    }
    
    MaterialTheme(
        colorScheme = colorSchemeToUse,
        typography = Typography,
        content = content
    )
}