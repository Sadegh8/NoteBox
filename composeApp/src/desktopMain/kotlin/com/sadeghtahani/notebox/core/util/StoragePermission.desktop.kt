package com.sadeghtahani.notebox.core.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

@Composable
actual fun rememberStoragePermissionLauncher(
    onResult: (Boolean) -> Unit
): PermissionLauncher {
    return remember {
        object : PermissionLauncher {
            override fun launch() {
                onResult(true)
            }
        }
    }
}
