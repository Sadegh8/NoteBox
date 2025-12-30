package com.sadeghtahani.notebox.features.notes.data.mapper

import com.sadeghtahani.notebox.features.notes.data.local.NoteEntity
import com.sadeghtahani.notebox.features.notes.domain.model.Note

fun NoteEntity.toDomain(): Note {
    return Note(
        id = id,
        title = title,
        content = content,
        isPinned = isPinned,
        createdAt = createdAt,
        updatedAt = updatedAt,
        color = color,
        tags = tags
    )
}

fun Note.toEntity(): NoteEntity {
    return NoteEntity(
        id = id,
        title = title,
        content = content,
        isPinned = isPinned,
        createdAt = createdAt,
        updatedAt = updatedAt,
        color = color,
        tags = tags
    )
}

fun List<NoteEntity>.toDomainList(): List<Note> = map { it.toDomain() }
