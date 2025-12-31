package com.sadeghtahani.notebox.features.notes.data.service

import com.sadeghtahani.notebox.features.notes.domain.service.FileSaver
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.BetaInteropApi
import platform.Foundation.*

class IosFileSaver : FileSaver {

    @OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
    override suspend fun saveFile(fileName: String, content: String): Result<String> {
        return try {
            val fileManager = NSFileManager.defaultManager
             val tempDir = NSTemporaryDirectory()
            val finalFileName = if (fileName.endsWith(".txt")) fileName else "$fileName.txt"
            val filePath = tempDir + finalFileName

            val url = NSURL.fileURLWithPath(filePath)

            val nsString = (content as NSString)
            val error = null

            nsString.writeToURL(
                url = url,
                atomically = true,
                encoding = NSUTF8StringEncoding,
                error = null
            )

            Result.success(filePath)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    @OptIn(ExperimentalForeignApi::class)
    override suspend fun saveContentToUri(uriString: String, content: String): Result<Unit> {
        return try {
            val url = NSURL.URLWithString(uriString) ?: throw Exception("Invalid URL")
            val nsString = (content as NSString)

            nsString.writeToURL(
                url = url,
                atomically = true,
                encoding = NSUTF8StringEncoding,
                error = null
            )
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
