package com.sadeghtahani.notebox.features.notes.domain.usecase

import com.sadeghtahani.notebox.features.notes.domain.model.Note
import com.sadeghtahani.notebox.features.notes.domain.repo.NoteRepository

class GetNoteByIdUseCase(
    private val repository: NoteRepository
) {
    suspend operator fun invoke(id: Long): Note? {
        return repository.getNoteById(id)
    }
}
