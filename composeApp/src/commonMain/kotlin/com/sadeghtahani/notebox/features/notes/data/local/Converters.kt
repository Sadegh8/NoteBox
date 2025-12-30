package com.sadeghtahani.notebox.features.notes.data.local
import androidx.room.TypeConverter
import kotlinx.serialization.json.Json

class Converters {
    @TypeConverter
    fun fromTagsList(tags: List<String>): String {
        return Json.encodeToString(tags)
    }

    @TypeConverter
    fun toTagsList(tagsString: String): List<String> {
        return try {
            Json.decodeFromString(tagsString)
        } catch (e: Exception) {
            emptyList()
        }
    }
}
