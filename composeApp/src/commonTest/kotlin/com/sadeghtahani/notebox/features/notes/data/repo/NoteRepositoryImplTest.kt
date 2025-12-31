package com.sadeghtahani.notebox.features.notes.data.repo

import com.sadeghtahani.notebox.features.notes.data.local.NoteEntity
import com.sadeghtahani.notebox.features.notes.data.local.dao.NoteDao
import com.sadeghtahani.notebox.features.notes.domain.model.Note
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class NoteRepositoryImplTest {

    // --- Tests ---

    @Test
    fun `getAllNotes transforms entity to domain model correctly`() = runTest {
        // Arrange
        val fakeDao = FakeNoteDao()
        val repository = NoteRepositoryImpl(fakeDao)

        val entity = NoteEntity(
            id = 1L,
            title = "Test Title",
            content = "Test Content",
            isPinned = true,
            createdAt = 100L,
            updatedAt = 200L,
            color = 0xFF0000,
            tags = listOf("Work")
        )
        fakeDao.insertNote(entity) // Pre-populate the fake DB

        // Act
        val resultList = repository.getAllNotes().first()
        val resultNote = resultList.first()

        // Assert
        assertEquals(1, resultList.size)
        assertEquals("Test Title", resultNote.title)
        assertEquals(true, resultNote.isPinned)
        assertEquals(200L, resultNote.updatedAt)
    }

    @Test
    fun `saveNote converts domain to entity and inserts into dao`() = runTest {
        // Arrange
        val fakeDao = FakeNoteDao()
        val repository = NoteRepositoryImpl(fakeDao)

        val newNote = Note(
            id = 5L,
            title = "New Note",
            content = "New Content",
            isPinned = false,
            createdAt = 500L,
            updatedAt = 500L,
            color = 123L,
            tags = listOf("Work")
        )

        // Act
        repository.saveNote(newNote)

        // Assert
        val insertedEntity = fakeDao.getNoteById(5L)
        assertEquals("New Note", insertedEntity?.title)
        assertEquals("New Content", insertedEntity?.content)
    }

    @Test
    fun `deleteNote removes item from dao`() = runTest {
        // Arrange
        val fakeDao = FakeNoteDao()
        val repository = NoteRepositoryImpl(fakeDao)
        val entity = NoteEntity(
            id = 1L,
            title = "To Delete",
            content = "",
            createdAt = 0,
            updatedAt = 0,
            color = 0,
            tags = listOf("Work"),
            isPinned = false
        )
        fakeDao.insertNote(entity)

        // Act
        repository.deleteNote(1L)

        // Assert
        val deletedNode = fakeDao.getNoteById(1L)
        assertEquals(null, deletedNode)
    }
}

// --- Helper: Manual Fake DAO ---
private class FakeNoteDao : NoteDao {

    // "Database"
    private val db = mutableMapOf<Long, NoteEntity>()

    override fun getAllNotes(): Flow<List<NoteEntity>> = flow {
        emit(db.values.toList())
    }

    override suspend fun getNoteById(id: Long): NoteEntity? {
        return db[id]
    }

    override suspend fun insertNote(note: NoteEntity) {
        db[note.id] = note
    }

    override suspend fun deleteNote(id: Long) {
        db.remove(id)
    }
}
