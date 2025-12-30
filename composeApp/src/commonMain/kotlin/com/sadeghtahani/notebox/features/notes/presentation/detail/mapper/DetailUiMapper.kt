package com.sadeghtahani.notebox.features.notes.presentation.detail.mapper

import com.sadeghtahani.notebox.features.notes.domain.model.Note
import com.sadeghtahani.notebox.features.notes.presentation.detail.data.NoteDetailUi
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

fun Note.toDetailUi(): NoteDetailUi {
    return NoteDetailUi(
        id = id,
        title = title,
        content = content,
        tags = tags,
        // FIXME  In a real app, use a proper relative time formatter here
        lastEdited = "Edited recently",
        isFavorite = isPinned
    )
}

@OptIn(ExperimentalTime::class)
fun NoteDetailUi.toDomain(): Note {
    val now = Clock.System.now().toEpochMilliseconds()
    return Note(
        id = id ?: 0L,
        title = title,
        content = content,
        isPinned = isFavorite,
        createdAt = now,
        updatedAt = now,
        color = 0xFF37cc19,
        tags = tags
    )
}
