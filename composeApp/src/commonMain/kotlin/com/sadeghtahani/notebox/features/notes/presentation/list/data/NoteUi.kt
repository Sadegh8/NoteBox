package com.sadeghtahani.notebox.features.notes.presentation.list.data

import androidx.compose.ui.graphics.Color

data class NoteUi(
    val id: Long,
    val title: String,
    val preview: String,
    val date: String,
    val isPinned: Boolean,
    val tag: String,
    val tagColor: Color,
    val tagBg: Color
)

sealed interface NoteListUiState {
    data object Loading : NoteListUiState
    data object Empty : NoteListUiState
    data class Success(
        val notes: List<NoteUi>,
        val pinnedNotes: List<NoteUi>,
        val otherNotes: List<NoteUi>
    ) : NoteListUiState
}
