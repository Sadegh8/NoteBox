package com.sadeghtahani.notebox.features.notes.presentation.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sadeghtahani.notebox.features.notes.domain.service.FileSaver
import com.sadeghtahani.notebox.features.notes.domain.usecase.*
import com.sadeghtahani.notebox.features.notes.presentation.detail.data.*
import com.sadeghtahani.notebox.features.notes.presentation.detail.mapper.toDetailUi
import com.sadeghtahani.notebox.features.notes.presentation.detail.mapper.toDomain
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.time.ExperimentalTime

class NoteDetailViewModel(
    private val noteId: Long?,
    private val getNoteByIdUseCase: GetNoteByIdUseCase,
    private val saveNoteUseCase: SaveNoteUseCase,
    private val deleteNoteUseCase: DeleteNoteUseCase,
    getNotesUseCase: GetNotesUseCase,
    private val fileSaver: FileSaver
) : ViewModel() {

    private val _noteInputState = MutableStateFlow(NoteDetailUi())
    private val _isLoading = MutableStateFlow(true)
    private val _error = MutableStateFlow<String?>(null)

    private val _uiEvent = Channel<DetailUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private val existingTagsFlow: Flow<List<String>> = getNotesUseCase()
        .map { notes ->
            withContext(Dispatchers.Default) {
                val defaults = listOf("All", "Work", "Personal", "Ideas", "Important")
                val userTags = notes.flatMap { it.tags }
                (defaults + userTags).distinct().sorted()
            }
        }

    val uiState: StateFlow<DetailUiState> = combine(
        _noteInputState,
        _isLoading,
        _error,
        existingTagsFlow.onStart { emit(emptyList()) }
    ) { note, isLoading, error, tags ->
        if (isLoading) {
            DetailUiState.Loading
        } else if (error != null) {
            DetailUiState.Error(error)
        } else {
            DetailUiState.Success(note, tags)
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = DetailUiState.Loading
    )

    init {
        initializeNote()
    }

    @OptIn(ExperimentalTime::class)
    private fun initializeNote() {
        viewModelScope.launch {
            _isLoading.value = true
            if (noteId != null && noteId != 0L) {
                val note = getNoteByIdUseCase(noteId)
                if (note != null) {
                    _noteInputState.value = note.toDetailUi()
                } else {
                    _error.value = "Note not found"
                }
            }
            _isLoading.value = false
        }
    }

    fun onAction(action: DetailUiAction) {
        when (action) {
            is DetailUiAction.UpdateTitle -> {
                _noteInputState.update { it.copy(title = action.newTitle) }
            }

            is DetailUiAction.UpdateContent -> {
                _noteInputState.update { it.copy(content = action.newContent) }
            }

            is DetailUiAction.UpdateTags -> {
                _noteInputState.update { it.copy(tags = action.newTags) }
            }

            is DetailUiAction.ToggleFavorite -> {
                _noteInputState.update { it.copy(isFavorite = !it.isFavorite) }
            }

            is DetailUiAction.SaveNote -> saveNote()
            is DetailUiAction.DeleteNote -> deleteNote()
            is DetailUiAction.ExportNote -> exportNote()
            is DetailUiAction.NavigateBack -> saveOnExit()
            is DetailUiAction.ExportNoteToUri -> exportNoteToUri(action.uriString)
        }
    }

    private fun saveNote() {
        val currentNote = _noteInputState.value
        viewModelScope.launch {
            saveNoteUseCase(currentNote.toDomain())
                .onSuccess { _uiEvent.send(DetailUiEvent.NavigateBack) }
                .onFailure { _uiEvent.send(DetailUiEvent.ShowMessage("Cannot save empty note")) }
        }
    }

    private fun saveOnExit() {
        val currentNote = _noteInputState.value
        viewModelScope.launch(NonCancellable) {
            saveNoteUseCase(currentNote.toDomain())
            _uiEvent.send(DetailUiEvent.NavigateBack)
        }
    }

    private fun deleteNote() {
        _noteInputState.value = NoteDetailUi()

        if (noteId != null && noteId != 0L) {
            viewModelScope.launch {
                deleteNoteUseCase(noteId)
                _uiEvent.send(DetailUiEvent.NavigateBack)
            }
        } else {
            viewModelScope.launch { _uiEvent.send(DetailUiEvent.NavigateBack) }
        }
    }

    private fun exportNote() {
        val note = _noteInputState.value

        if (note.title.isBlank() && note.content.isBlank()) {
            viewModelScope.launch { _uiEvent.send(DetailUiEvent.ShowMessage("Note is empty")) }
            return
        }

        viewModelScope.launch {
            val result = fileSaver.saveFile(
                fileName = note.title.ifBlank { "Untitled" },
                content = note.content
            )

            if (result.isSuccess) {
                result.getOrNull()?.let { path ->
                    _uiEvent.send(DetailUiEvent.ExportSuccess(path))
                }
            } else {
                _uiEvent.send(DetailUiEvent.ShowMessage("Export failed"))
            }
        }
    }

    private fun exportNoteToUri(uriString: String) {
        val note = _noteInputState.value
        viewModelScope.launch {
            val result = fileSaver.saveContentToUri(
                uriString = uriString,
                content = note.content
            )

            if (result.isSuccess) {
                _uiEvent.send(DetailUiEvent.ShowMessage("Exported successfully!"))
            } else {
                _uiEvent.send(DetailUiEvent.ShowMessage("Export failed"))
            }
        }
    }
}
