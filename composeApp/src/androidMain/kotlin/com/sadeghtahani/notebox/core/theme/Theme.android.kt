package com.sadeghtahani.notebox.core.theme

import androidx.compose.runtime.Composable
import androidx.compose.foundation.isSystemInDarkTheme

@Composable
actual fun rememberPlatformDarkTheme(): Boolean = isSystemInDarkTheme()
