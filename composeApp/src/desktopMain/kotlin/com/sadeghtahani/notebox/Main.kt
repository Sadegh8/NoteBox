package com.sadeghtahani.notebox

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.sadeghtahani.notebox.di.appModule
import com.sadeghtahani.notebox.di.desktopModule
import org.koin.core.context.GlobalContext.startKoin

fun main() = application {
    startKoin {
        modules(appModule, desktopModule)
    }

    Window(onCloseRequest = ::exitApplication) {
        App()
    }
}
