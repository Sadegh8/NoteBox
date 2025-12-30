package com.sadeghtahani.notebox.features.notes.data.repo

import com.sadeghtahani.notebox.features.notes.data.local.dao.NoteDao
import com.sadeghtahani.notebox.features.notes.data.mapper.toDomain
import com.sadeghtahani.notebox.features.notes.data.mapper.toEntity
import com.sadeghtahani.notebox.features.notes.domain.model.Note
import com.sadeghtahani.notebox.features.notes.domain.repo.NoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.collections.map

class NoteRepositoryImpl(
    private val dao: NoteDao
) : NoteRepository {

    override fun getAllNotes(): Flow<List<Note>> {
        return dao.getAllNotes().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getNoteById(id: Long): Note? {
        return dao.getNoteById(id)?.toDomain()
    }

    override suspend fun saveNote(note: Note) {
        dao.insertNote(note.toEntity())
    }

    override suspend fun deleteNote(id: Long) {
        dao.deleteNote(id)
    }
}
