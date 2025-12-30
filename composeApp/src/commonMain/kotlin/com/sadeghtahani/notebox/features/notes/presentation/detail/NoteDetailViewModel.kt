package com.sadeghtahani.notebox.features.notes.presentation.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sadeghtahani.notebox.features.notes.domain.usecase.GetNoteByIdUseCase
import com.sadeghtahani.notebox.features.notes.domain.usecase.SaveNoteUseCase
import com.sadeghtahani.notebox.features.notes.presentation.detail.data.DetailUiState
import com.sadeghtahani.notebox.features.notes.presentation.detail.data.NoteDetailUi
import com.sadeghtahani.notebox.features.notes.presentation.detail.mapper.toDetailUi
import com.sadeghtahani.notebox.features.notes.presentation.detail.mapper.toDomain
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class NoteDetailViewModel(
    private val noteId: Long?, // Null for new note, ID for editing
    private val getNoteByIdUseCase: GetNoteByIdUseCase,
    private val saveNoteUseCase: SaveNoteUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<DetailUiState>(DetailUiState.Loading)
    val uiState: StateFlow<DetailUiState> = _uiState.asStateFlow()

    init {
        loadNote()
    }

    private fun loadNote() {
        viewModelScope.launch {
            if (noteId == null || noteId == 0L) {
                // Creating a new note
                _uiState.value = DetailUiState.Success(NoteDetailUi())
            } else {
                // Editing existing note
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
            // Ideally navigate back or show a success message here
        }
    }
}

