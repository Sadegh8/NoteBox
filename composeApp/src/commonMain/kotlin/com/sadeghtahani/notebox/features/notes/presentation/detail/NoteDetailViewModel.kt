package com.sadeghtahani.notebox.features.notes.presentation.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sadeghtahani.notebox.features.notes.domain.usecase.DeleteNoteUseCase
import com.sadeghtahani.notebox.features.notes.domain.usecase.GetNoteByIdUseCase
import com.sadeghtahani.notebox.features.notes.domain.usecase.GetNotesUseCase
import com.sadeghtahani.notebox.features.notes.domain.usecase.SaveNoteUseCase
import com.sadeghtahani.notebox.features.notes.presentation.detail.data.DetailUiState
import com.sadeghtahani.notebox.features.notes.presentation.detail.data.NoteDetailUi
import com.sadeghtahani.notebox.features.notes.presentation.detail.mapper.toDetailUi
import com.sadeghtahani.notebox.features.notes.presentation.detail.mapper.toDomain
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class NoteDetailViewModel(
    private val noteId: Long?,
    private val getNoteByIdUseCase: GetNoteByIdUseCase,
    private val saveNoteUseCase: SaveNoteUseCase,
    private val deleteNoteUseCase: DeleteNoteUseCase,
    getNotesUseCase: GetNotesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<DetailUiState>(DetailUiState.Loading)
    val uiState: StateFlow<DetailUiState> = _uiState.asStateFlow()

    val existingTags: StateFlow<List<String>> = getNotesUseCase()
        .map { notes ->
            val defaults = listOf("All", "Work", "Personal", "Ideas", "Important")
            val userTags = notes.flatMap { it.tags }
            (defaults + userTags).distinct().sorted()
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    init {
        loadNote()
    }

    private fun loadNote() {
        viewModelScope.launch {
            if (noteId == null || noteId == 0L) {
                _uiState.value = DetailUiState.Success(NoteDetailUi())
            } else {
                val note = getNoteByIdUseCase(noteId)
                if (note != null) {
                    _uiState.value = DetailUiState.Success(note.toDetailUi())
                } else {
                    _uiState.value = DetailUiState.Error("Note not found")
                }
            }
        }
    }

    fun saveNote(currentUiState: NoteDetailUi) {
        viewModelScope.launch {
            saveNoteUseCase(currentUiState.toDomain())
        }
    }

    fun deleteNote(onSuccess: () -> Unit) {
        if (noteId != null && noteId != 0L) {
            viewModelScope.launch {
                deleteNoteUseCase(noteId)
                onSuccess()
            }
        } else {
            onSuccess()
        }
    }
}
