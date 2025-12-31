// composeApp/src/androidMain/kotlin/.../database/AppDatabase.android.kt

package com.sadeghtahani.notebox.features.notes.data.local.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.sadeghtahani.notebox.BuildConfig
import net.zetetic.database.sqlcipher.SupportOpenHelperFactory

lateinit var appContext: Context

actual fun getDatabaseBuilder(): RoomDatabase.Builder<AppDatabase> {
    val dbFile = appContext.getDatabasePath("notebox.db")

    System.loadLibrary("sqlcipher")

    val passphraseBytes =
        BuildConfig.DB_PASSPHRASE.toByteArray(Charsets.UTF_8)
    val factory = SupportOpenHelperFactory(passphraseBytes)

    return Room.databaseBuilder<AppDatabase>(
        context = appContext,
        name = dbFile.absolutePath
    ).openHelperFactory(factory)
}
