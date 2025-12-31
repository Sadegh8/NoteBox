package com.sadeghtahani.notebox.core.util

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

@Composable
actual fun rememberStoragePermissionLauncher(
    onResult: (Boolean) -> Unit
): PermissionLauncher {

    val permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        arrayOf(Manifest.permission.READ_MEDIA_IMAGES)
    } else {
        arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissionsMap ->
        val allGranted = permissionsMap.values.all { it }
        onResult(allGranted)
    }

    return remember(launcher) {
        object : PermissionLauncher {
            override fun launch() {
                launcher.launch(permissions)
            }
        }
    }
}
