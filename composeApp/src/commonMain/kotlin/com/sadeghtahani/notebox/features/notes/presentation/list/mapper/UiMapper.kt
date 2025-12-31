package com.sadeghtahani.notebox.features.notes.presentation.list.mapper

import androidx.compose.ui.graphics.Color
import com.sadeghtahani.notebox.features.notes.domain.model.Note
import com.sadeghtahani.notebox.features.notes.presentation.list.data.NoteUi
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
fun formatRelativeDate(timestamp: Long): String {
    val now = Clock.System.now().toEpochMilliseconds()
    val diff = now - timestamp

    return when {
        diff < 60000 -> "Just now"
        diff < 3600000 -> "${diff / 60000}m ago"
        diff < 86400000 -> "Today"
        diff < 172800000 -> "Yesterday"
        else -> "Earlier"
    }
}

fun Note.toUi(): NoteUi {
    val noteColor = if (color == 0L) Color(0xFF37cc19) else Color(color)

    return NoteUi(
        id = id,
        title = title.ifBlank { "Untitled Note" },
        preview = content.take(100).replace("\n", " "),
        date = formatRelativeDate(updatedAt),
        isPinned = isPinned,
        tag = tags.firstOrNull() ?: "No Tag",
        tagColor = noteColor,
        tagBg = noteColor.copy(alpha = 0.2f)
    )
}
