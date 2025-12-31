package com.sadeghtahani.notebox.features.notes.data.service

import android.content.ContentValues
import android.content.Context
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import com.sadeghtahani.notebox.features.notes.domain.service.FileSaver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class AndroidFileSaver(private val context: Context) : FileSaver {
    override suspend fun saveFile(fileName: String, content: String): Result<String> = withContext(Dispatchers.IO) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val values = ContentValues().apply {
                    put(MediaStore.Downloads.DISPLAY_NAME, "$fileName.txt")
                    put(MediaStore.Downloads.MIME_TYPE, "text/plain")
                    put(MediaStore.Downloads.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
                }

                val resolver = context.contentResolver
                val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, values)
                    ?: throw Exception("Could not create file in Downloads via MediaStore")

                resolver.openOutputStream(uri)?.use { stream ->
                    stream.write(content.toByteArray())
                }

                Result.success(uri.toString())
            } else {
                val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                if (!downloadsDir.exists()) downloadsDir.mkdirs()

                val file = File(downloadsDir, "$fileName.txt")
                file.writeText(content)

                Result.success(file.absolutePath)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }
}