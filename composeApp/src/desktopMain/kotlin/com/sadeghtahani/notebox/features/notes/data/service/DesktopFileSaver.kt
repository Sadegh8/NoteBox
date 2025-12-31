package com.sadeghtahani.notebox.features.notes.data.service

import com.sadeghtahani.notebox.features.notes.domain.service.FileSaver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.awt.FileDialog
import java.awt.Frame
import java.io.File
import java.net.URI

class DesktopFileSaver : FileSaver {

    override suspend fun saveFile(fileName: String, content: String): Result<String> {
        return try {
            val selectedFile = withContext(Dispatchers.Main) {
                val dialog = FileDialog(null as Frame?, "Export Note", FileDialog.SAVE)
                dialog.file = "$fileName.txt"

                dialog.isVisible = true

                if (dialog.file != null && dialog.directory != null) {
                    File(dialog.directory, dialog.file)
                } else {
                    null
                }
            }

            if (selectedFile != null) {
                withContext(Dispatchers.IO) {
                    val finalFile = if (selectedFile.name.endsWith(".txt", ignoreCase = true)) {
                        selectedFile
                    } else {
                        File(selectedFile.absolutePath + ".txt")
                    }

                    finalFile.writeText(content)
                    Result.success(finalFile.absolutePath)
                }
            } else {
                Result.failure(Exception("Export cancelled by user"))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    override suspend fun saveContentToUri(
        uriString: String,
        content: String
    ): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val file = if (uriString.startsWith("file:")) {
                File(URI(uriString))
            } else {
                File(uriString)
            }

            file.parentFile?.mkdirs()

            file.writeText(content)
            Result.success(Unit)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }
}
