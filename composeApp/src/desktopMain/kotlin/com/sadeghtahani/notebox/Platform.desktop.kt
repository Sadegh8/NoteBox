package com.sadeghtahani.notebox

import java.awt.Desktop
import java.io.File

class DesktopPlatform : Platform {
    override val name: String = "Java ${System.getProperty("java.version")}"
}

actual fun getPlatform(): Platform = DesktopPlatform()

actual fun openFile(path: String) {
    try {
        val file = File(path)
        if (file.exists()) {
            Desktop.getDesktop().open(file)
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}