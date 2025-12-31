package com.sadeghtahani.notebox.features.notes.domain.service

interface FileSaver {
    suspend fun saveFile(fileName: String, content: String): Result<String>
}
