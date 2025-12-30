package com.sadeghtahani.notebox.features.notes.data.local.database

import androidx.room.Room
import androidx.room.RoomDatabase
import java.io.File

actual fun getDatabaseBuilder(): RoomDatabase.Builder<AppDatabase> {
    val dbFile = File(System.getProperty("user.home"), "notebox.db")
    return Room.databaseBuilder<AppDatabase>(
        name = dbFile.absolutePath
    )
}
