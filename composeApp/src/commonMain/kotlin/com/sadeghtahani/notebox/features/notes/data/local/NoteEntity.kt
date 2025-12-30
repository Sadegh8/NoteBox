package com.sadeghtahani.notebox.features.notes.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
data class NoteEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val content: String,
    val isPinned: Boolean,
    val createdAt: Long,
    val updatedAt: Long,
    val color: Long,
    val tags: List<String>
)
