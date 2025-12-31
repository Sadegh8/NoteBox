package com.sadeghtahani.notebox.core.theme

import androidx.compose.runtime.*
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import java.io.BufferedReader
import java.io.InputStreamReader

@Composable
actual fun rememberPlatformDarkTheme(): Boolean {
    var isDark by remember { mutableStateOf(detectMacDarkMode()) }

    LaunchedEffect(Unit) {
        while (currentCoroutineContext().isActive) {
            val newValue = detectMacDarkMode()
            if (newValue != isDark) {
                isDark = newValue
            }
            delay(600)
        }
    }

    return isDark
}

private fun detectMacDarkMode(): Boolean {
    return try {
        val process = ProcessBuilder(
            "defaults", "read", "-g", "AppleInterfaceStyle"
        ).start()
        val output = BufferedReader(InputStreamReader(process.inputStream)).readText()
        output.contains("Dark", ignoreCase = true)
    } catch (e: Exception) {
        false
    }
}
