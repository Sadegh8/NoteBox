package com.sadeghtahani.notebox.features.notes.domain.repo

import com.sadeghtahani.notebox.features.notes.domain.model.Note
import kotlinx.coroutines.flow.Flow

interface NoteRepository {
    fun getAllNotes(): Flow<List<Note>>
    suspend fun getNoteById(id: Long): Note?
    suspend fun saveNote(note: Note)
    suspend fun deleteNote(id: Long)
}
