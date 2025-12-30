package com.sadeghtahani.notebox.features.notes.presentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sadeghtahani.notebox.features.notes.domain.repo.SettingsRepository
import com.sadeghtahani.notebox.features.notes.domain.usecase.GetNotesUseCase
import com.sadeghtahani.notebox.features.notes.domain.usecase.ToggleNotePinUseCase
import com.sadeghtahani.notebox.features.notes.presentation.list.data.NoteListUiState
import com.sadeghtahani.notebox.features.notes.presentation.list.mapper.toUi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlin.collections.listOf

class NoteListViewModel(
    getNotesUseCase: GetNotesUseCase,
    private val toggleNotePinUseCase: ToggleNotePinUseCase,
    private val settingsRepository: SettingsRepository,
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _selectedFilter = MutableStateFlow("All")
    val selectedFilter = _selectedFilter.asStateFlow()

    val isGridView: StateFlow<Boolean> = settingsRepository.isGridView()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )

    val tags: StateFlow<List<String>> = getNotesUseCase()
        .map { notes ->
            val defaultTags = listOf("All", "Work", "Personal", "Ideas", "Important")


            val userTags = notes
                .flatMap { it.tags }
                .distinct()
                .sorted()

            defaultTags + userTags
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = listOf("All", "Work", "Personal", "Ideas", "Important")

        )

    val uiState: StateFlow<NoteListUiState> = combine(
        getNotesUseCase(),
        _searchQuery,
        _selectedFilter
    ) { notes, query, filter ->

        var resultingNotes = if (query.isBlank()) {
            notes
        } else {
            notes.filter { note ->
                note.title.contains(query, ignoreCase = true) ||
                        note.content.contains(query, ignoreCase = true)
            }
        }

        if (filter != "All") {
            resultingNotes = resultingNotes.filter { note ->
                when (filter) {
                    "Favorites" -> note.isPinned
                    else -> note.tags.any { it.equals(filter, ignoreCase = true) }
                }
            }
        }

        if (resultingNotes.isEmpty() && query.isBlank() && filter == "All") {
            NoteListUiState.Empty
        } else {
            NoteListUiState.Success(resultingNotes.map { it.toUi() })
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = NoteListUiState.Loading
    )

    fun onSearchQueryChange(newQuery: String) {
        _searchQuery.value = newQuery
    }

    fun onFilterChange(newFilter: String) {
        _selectedFilter.value = newFilter
    }

    fun togglePin(noteId: Long) {
        viewModelScope.launch {
            toggleNotePinUseCase(noteId)
        }
    }

    fun toggleView() {
        viewModelScope.launch {
            settingsRepository.setGridView(!isGridView.value)
        }
    }
}
