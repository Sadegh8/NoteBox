package com.sadeghtahani.notebox.features.notes.domain.repo

import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    fun isGridView(): Flow<Boolean>
    suspend fun setGridView(isGrid: Boolean)
}
