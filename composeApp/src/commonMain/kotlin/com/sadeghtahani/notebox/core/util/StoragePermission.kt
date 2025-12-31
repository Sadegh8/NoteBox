package com.sadeghtahani.notebox.core.util

import androidx.compose.runtime.Composable

interface PermissionLauncher {
    fun launch()
}

@Composable
expect fun rememberStoragePermissionLauncher(
    onResult: (Boolean) -> Unit
): PermissionLauncher