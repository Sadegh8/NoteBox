package com.sadeghtahani.notebox.features.notes.domain.usecase

import com.sadeghtahani.notebox.features.notes.domain.model.Note
import com.sadeghtahani.notebox.features.notes.domain.repo.NoteRepository

class SaveNoteUseCase(
    private val repository: NoteRepository
) {
    suspend operator fun invoke(note: Note): Result<Unit> {
        if (note.title.isBlank() && note.content.isBlank()) {
            return Result.failure(Exception("Note cannot be empty"))
        }
        repository.saveNote(note)
        return Result.success(Unit)
    }
}
