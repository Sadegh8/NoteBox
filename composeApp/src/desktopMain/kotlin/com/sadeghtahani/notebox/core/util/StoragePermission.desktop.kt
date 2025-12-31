package com.sadeghtahani.notebox.core.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState

@Composable
actual fun rememberStoragePermissionLauncher(
    onResult: (Boolean) -> Unit
): PermissionLauncher {
    val currentOnResult by rememberUpdatedState(onResult)

    return remember {
        object : PermissionLauncher {
            override fun launch() {
                currentOnResult(true)
            }
        }
    }
}
