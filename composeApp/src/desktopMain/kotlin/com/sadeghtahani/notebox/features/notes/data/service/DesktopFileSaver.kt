package com.sadeghtahani.notebox.features.notes.data.service

import com.sadeghtahani.notebox.features.notes.domain.service.FileSaver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.net.URI
import javax.swing.JFileChooser
import javax.swing.filechooser.FileNameExtensionFilter

class DesktopFileSaver : FileSaver {

    override suspend fun saveFile(fileName: String, content: String): Result<String> {
        return try {
            val selectedFile = withContext(Dispatchers.Main) {
                val chooser = JFileChooser()
                chooser.dialogTitle = "Export Note"
                chooser.selectedFile = File("$fileName.txt")
                chooser.fileFilter = FileNameExtensionFilter("Text Files", "txt")

                val userSelection = chooser.showSaveDialog(null)

                if (userSelection == JFileChooser.APPROVE_OPTION) {
                    chooser.selectedFile
                } else {
                    null
                }
            }

            if (selectedFile != null) {
                withContext(Dispatchers.IO) {
                    val finalFile = if (selectedFile.absolutePath.endsWith(".txt")) {
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

            file.writeText(content)
            Result.success(Unit)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }
}
