package com.sadeghtahani.notebox.core.theme

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
expect fun rememberPlatformDarkTheme(): Boolean

@Composable
fun AppTheme(
    useDarkTheme: Boolean = rememberPlatformDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (useDarkTheme) DarkColorScheme else LightColorScheme

    SystemAppearance(isDark = useDarkTheme)

    MaterialTheme(
        colorScheme = colors,
        content = content
    )
}