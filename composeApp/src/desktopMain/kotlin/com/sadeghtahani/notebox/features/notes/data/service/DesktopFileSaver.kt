package com.sadeghtahani.notebox.features.notes.data.service

import com.sadeghtahani.notebox.features.notes.domain.service.FileSaver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import javax.swing.JFileChooser
import javax.swing.filechooser.FileNameExtensionFilter

class DesktopFileSaver : FileSaver {
    override suspend fun saveFile(fileName: String, content: String): Result<String> {
        return try {
            val selectedFile = withContext(Dispatchers.Main) {
                val fileChooser = JFileChooser()
                fileChooser.dialogTitle = "Export Note"
                fileChooser.selectedFile = File("$fileName.txt")
                fileChooser.fileFilter = FileNameExtensionFilter("Text Files", "txt")

                val userSelection = fileChooser.showSaveDialog(null)

                if (userSelection == JFileChooser.APPROVE_OPTION) {
                    fileChooser.selectedFile
                } else {
                    null
                }
            }

            if (selectedFile != null) {
                withContext(Dispatchers.IO) {
                    var fileToSave = selectedFile
                    // Ensure proper extension
                    if (!fileToSave.absolutePath.endsWith(".txt")) {
                        fileToSave = File(fileToSave.absolutePath + ".txt")
                    }
                    fileToSave.writeText(content)
                    Result.success(fileToSave.absolutePath)
                }
            } else {
                Result.failure(Exception("Cancelled by user"))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }
}
