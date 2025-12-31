package com.sadeghtahani.notebox.features.notes.presentation.detail.mapper

import com.sadeghtahani.notebox.features.notes.domain.model.Note
import com.sadeghtahani.notebox.features.notes.presentation.detail.data.NoteDetailUi
import kotlin.time.Clock
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
fun Note.toDetailUi(): NoteDetailUi {
    return NoteDetailUi(
        id = id,
        title = title,
        content = content,
        tags = tags,
        lastEditedTimestamp = updatedAt,
        isFavorite = isPinned
    )
}

@OptIn(ExperimentalTime::class)
fun NoteDetailUi.toDomain(
    currentTime: Long = Clock.System.now().toEpochMilliseconds()
): Note {
    return Note(
        id = id ?: 0L,
        title = title,
        content = content,
        isPinned = isFavorite,
        createdAt = if (id == null || id == 0L) currentTime else 0L,
        updatedAt = currentTime,
        color = 0xFF37cc19,
        tags = tags
    )
}

@OptIn(ExperimentalTime::class)
private fun formatRelativeTime(timestamp: Long): String {
    val now = Clock.System.now().toEpochMilliseconds()
    val diff = (now - timestamp).milliseconds.inWholeSeconds

    return when {
        diff < 60 -> "Just now"
        diff < 3600 -> "${diff / 60} min${if ((diff / 60) != 1L) "s" else ""} ago"
        diff < 86400 -> "${diff / 3600} hr${if ((diff / 3600) != 1L) "s" else ""} ago"
        diff < 604800 -> "${diff / 86400} day${if ((diff / 86400) != 1L) "s" else ""} ago"
        else -> "${diff / 604800} week${if ((diff / 604800) != 1L) "s" else ""} ago"
    }
}
