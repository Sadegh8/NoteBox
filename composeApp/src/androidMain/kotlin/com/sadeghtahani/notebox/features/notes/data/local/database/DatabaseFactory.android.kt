package com.sadeghtahani.notebox.features.notes.data.local.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.sadeghtahani.notebox.BuildConfig
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory

lateinit var appContext: Context

actual fun getDatabaseBuilder(): RoomDatabase.Builder<AppDatabase> {
    val dbFile = appContext.getDatabasePath("notebox.db")

    SQLiteDatabase.loadLibs(appContext)

    val passphraseChars = BuildConfig.DB_PASSPHRASE.toCharArray()
    val passphrase = SQLiteDatabase.getBytes(passphraseChars)
    val factory = SupportFactory(passphrase)

    return Room.databaseBuilder<AppDatabase>(
        context = appContext,
        name = dbFile.absolutePath
    ).openHelperFactory(factory)
}
