package com.sadeghtahani.notebox.features.notes.domain.usecase

import com.sadeghtahani.notebox.features.notes.domain.repo.NoteRepository

class DeleteNoteUseCase(
    private val repository: NoteRepository
) {
    suspend operator fun invoke(id: Long) {
        repository.deleteNote(id)
    }
}
