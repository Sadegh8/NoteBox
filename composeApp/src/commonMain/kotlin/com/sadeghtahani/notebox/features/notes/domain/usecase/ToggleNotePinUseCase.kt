package com.sadeghtahani.notebox.features.notes.domain.usecase

import com.sadeghtahani.notebox.features.notes.domain.repo.NoteRepository
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

class ToggleNotePinUseCase(
    private val repository: NoteRepository
) {
    @OptIn(ExperimentalTime::class)
    suspend operator fun invoke(id: Long) {
        val note = repository.getNoteById(id)
        // Only proceed if the note actually exists
        if (note != null) {
            val updatedNote = note.copy(
                isPinned = !note.isPinned,
                updatedAt = Clock.System.now().toEpochMilliseconds()
            )
            repository.saveNote(updatedNote)
        }
    }
}
