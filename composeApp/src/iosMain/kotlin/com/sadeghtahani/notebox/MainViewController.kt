package com.sadeghtahani.notebox

import androidx.compose.ui.window.ComposeUIViewController
import com.sadeghtahani.notebox.di.appModule
import org.koin.core.context.startKoin

fun MainViewController() = ComposeUIViewController(
    configure = {
        // Init Koin here (safest for simple setups)
        startKoin {
            modules(appModule)
        }
    }
) {
    App()
}
