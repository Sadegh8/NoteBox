package com.sadeghtahani.notebox.features.notes.presentation.detail.data

data class NoteDetailUi(
    val id: Long? = null,
    val title: String = "",
    val content: String = "",
    val tags: List<String> = emptyList(),
    val lastEdited: String = "Edited just now",
    val isFavorite: Boolean = false
)

sealed interface DetailUiState {
    data object Loading : DetailUiState
    data class Success(val note: NoteDetailUi) : DetailUiState
    data class Error(val message: String) : DetailUiState
}