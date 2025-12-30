package com.sadeghtahani.notebox

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.sadeghtahani.notebox.core.navigation.MainNavigation
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    MaterialTheme {
        MainNavigation()
    }
}
