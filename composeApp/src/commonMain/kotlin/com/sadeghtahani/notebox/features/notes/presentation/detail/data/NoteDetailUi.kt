package com.sadeghtahani.notebox.features.notes.presentation.detail.data

data class NoteDetailUi(
    val id: Long? = null,
    val title: String = "",
    val content: String = "",
    val tags: List<String> = emptyList(),
    val lastEditedTimestamp: Long = 0L,
    val isFavorite: Boolean = false
)

sealed interface DetailUiState {
    data object Loading : DetailUiState
    data class Error(val message: String) : DetailUiState
    data class Success(
        val note: NoteDetailUi,
        val availableTags: List<String>
    ) : DetailUiState
}

enum class FormattingType { BOLD, ITALIC, LIST }

data class ActiveFormats(
    val isBold: Boolean = false,
    val isItalic: Boolean = false,
    val isList: Boolean = false
)

sealed interface DetailUiAction {
    data class UpdateTitle(val newTitle: String) : DetailUiAction
    data class UpdateContent(val newContent: String) : DetailUiAction
    data class UpdateTags(val newTags: List<String>) : DetailUiAction
    data object ToggleFavorite : DetailUiAction
    data object SaveNote : DetailUiAction
    data object DeleteNote : DetailUiAction
    data object ExportNote : DetailUiAction
    data object NavigateBack : DetailUiAction
    data class ExportNoteToUri(val uriString: String) : DetailUiAction
}

sealed interface DetailUiEvent {
    data object NavigateBack : DetailUiEvent
    data class ShowMessage(val message: String) : DetailUiEvent
    data class ExportSuccess(val filePath: String) : DetailUiEvent
}
