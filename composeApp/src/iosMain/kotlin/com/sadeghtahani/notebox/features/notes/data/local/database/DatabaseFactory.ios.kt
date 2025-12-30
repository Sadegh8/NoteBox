package com.sadeghtahani.notebox.features.notes.data.local.database

import androidx.room.Room
import androidx.room.RoomDatabase
import platform.Foundation.NSHomeDirectory

actual fun getDatabaseBuilder(): RoomDatabase.Builder<AppDatabase> {
    val dbPath = NSHomeDirectory() + "/notebox.db"

    return Room.databaseBuilder<AppDatabase>(
        name = dbPath
    )
}
