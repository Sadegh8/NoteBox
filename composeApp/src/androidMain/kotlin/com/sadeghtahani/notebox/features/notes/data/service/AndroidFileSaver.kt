package com.sadeghtahani.notebox.features.notes.data.service

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.ContextCompat
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
                 if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED
                ) {
                    return@withContext Result.failure(
                        SecurityException("Permission denied: WRITE_EXTERNAL_STORAGE is required for this Android version.")
                    )
                }

                try {
                    val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                    if (!downloadsDir.exists()) downloadsDir.mkdirs()

                    val file = File(downloadsDir, "$fileName.txt")
                    file.writeText(content)

                    Result.success(file.absolutePath)
                } catch (e: SecurityException) {
                    e.printStackTrace()
                    Result.failure(Exception("Permission denied: Unable to write to storage."))
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    override suspend fun saveContentToUri(uriString: String, content: String): Result<Unit> {
        return try {
            val uri = Uri.parse(uriString)
            context.contentResolver.openOutputStream(uri)?.use { stream ->
                stream.write(content.toByteArray())
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
