package com.sadeghtahani.notebox.features.notes.data.local.database

import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import java.io.File

actual fun getDatabaseBuilder(): RoomDatabase.Builder<AppDatabase> {
    val dbFile = File(System.getProperty("user.home"), "notebox.db")


    // NOTE:
    // On Desktop we currently use BundledSQLiteDriver, which is *not* encrypted.
    // Room KMP does not ship an official SQLCipher-based driver for desktop yet.
    // Android uses SQLCipher via sqlcipher-android; desktop is for developer use
    // only and stores non-sensitive data.
    return Room.databaseBuilder<AppDatabase>(
        name = dbFile.absolutePath
    ).setDriver(BundledSQLiteDriver())
}
