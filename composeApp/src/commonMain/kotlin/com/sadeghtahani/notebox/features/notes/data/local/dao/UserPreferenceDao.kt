package com.sadeghtahani.notebox.features.notes.data.local.dao

import com.sadeghtahani.notebox.features.notes.data.local.UserPreference
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface UserPreferenceDao {
    @Query("SELECT value FROM user_preferences WHERE `key` = :key")
    fun observeString(key: String): Flow<String?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(preference: UserPreference)
}
