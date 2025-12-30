// file: composeApp/src/commonMain/kotlin/com/sadeghtahani/notebox/core/theme/Theme.kt
package com.sadeghtahani.notebox.core.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color


private val DarkColorScheme = darkColorScheme(
    primary = NeonGreen,
    onPrimary = Color.Black,
    background = DarkBackground,
    onBackground = Color.White,
    surface = DarkSurface,
    onSurface = Color.White,
    secondary = TextGray,
    onSecondary = Color.Black
)

private val LightColorScheme = lightColorScheme(
    primary = LightAccentGreen,
    onPrimary = Color.White,
    background = LightBackground,
    onBackground = TextDark,
    surface = LightSurface,
    onSurface = TextDark,
    secondary = Color.Gray,
    onSecondary = Color.White
)

@Composable
fun AppTheme(
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (useDarkTheme) DarkColorScheme else LightColorScheme

    // Helper to change Status Bar colors (Platform specific)
    SystemAppearance(isDark = useDarkTheme)

    MaterialTheme(
        colorScheme = colors,
        // typography = Typography, // Add your typography here if you have it
        // shapes = Shapes,         // Add your shapes here if you have it
        content = content
    )
}