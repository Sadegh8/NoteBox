package com.sadeghtahani.notebox

import android.content.Intent
import android.net.Uri
import android.os.Build
import com.sadeghtahani.notebox.features.notes.data.local.database.appContext
import java.io.File

class AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
}

actual fun getPlatform(): Platform = AndroidPlatform()

actual fun openFile(path: String) {
    try {
        val uri = if (path.startsWith("content://")) Uri.parse(path) else Uri.fromFile(File(path))
        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, "text/plain")
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_GRANT_READ_URI_PERMISSION
        }
        appContext.startActivity(intent)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}
