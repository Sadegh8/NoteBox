package com.sadeghtahani.notebox

import androidx.compose.runtime.Composable
import com.sadeghtahani.notebox.core.navigation.MainNavigation
import com.sadeghtahani.notebox.core.theme.AppTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    AppTheme {
        MainNavigation()
    }
}