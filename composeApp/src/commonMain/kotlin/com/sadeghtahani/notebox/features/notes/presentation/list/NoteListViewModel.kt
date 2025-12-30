package com.sadeghtahani.notebox.features.notes.presentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sadeghtahani.notebox.features.notes.domain.usecase.GetNotesUseCase
import com.sadeghtahani.notebox.features.notes.presentation.list.data.NoteListUiState
import com.sadeghtahani.notebox.features.notes.presentation.list.data.NoteUi
import com.sadeghtahani.notebox.features.notes.presentation.list.mapper.toUi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class NoteListViewModel(
    getNotesUseCase: GetNotesUseCase
) : ViewModel() {

    // Hot Flow: Stays active only while UI is subscribed (5 seconds buffer)
    // This prevents memory leaks and unnecessary processing when app is in background.
    val uiState: StateFlow<NoteListUiState> = getNotesUseCase()
        .map { notes ->
            if (notes.isEmpty()) {
                NoteListUiState.Empty
            } else {
                NoteListUiState.Success(notes.map { it.toUi() })
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = NoteListUiState.Loading
        )
}

