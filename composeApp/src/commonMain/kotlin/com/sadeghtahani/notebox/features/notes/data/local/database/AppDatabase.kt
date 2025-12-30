package com.sadeghtahani.notebox.features.notes.data.local.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.room.TypeConverters
import com.sadeghtahani.notebox.features.notes.data.local.Converters
import com.sadeghtahani.notebox.features.notes.data.local.NoteEntity
import com.sadeghtahani.notebox.features.notes.data.local.dao.NoteDao

@Database(
    entities = [NoteEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
@ConstructedBy(AppDatabaseConstructor::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao
}

@Suppress("KotlinNoActualForExpect")
expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase> {
    override fun initialize(): AppDatabase
}
