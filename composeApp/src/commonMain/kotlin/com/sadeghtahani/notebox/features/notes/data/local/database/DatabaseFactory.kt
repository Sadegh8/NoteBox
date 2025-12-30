package com.sadeghtahani.notebox.features.notes.data.local.database

import androidx.room.RoomDatabase

expect fun getDatabaseBuilder(): RoomDatabase.Builder<AppDatabase>
