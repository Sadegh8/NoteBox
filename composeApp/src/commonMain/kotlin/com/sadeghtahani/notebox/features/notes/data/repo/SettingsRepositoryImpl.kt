package com.sadeghtahani.notebox.features.notes.data.repo

import com.sadeghtahani.notebox.features.notes.data.local.UserPreference
import com.sadeghtahani.notebox.features.notes.data.local.dao.UserPreferenceDao
import com.sadeghtahani.notebox.features.notes.domain.repo.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SettingsRepositoryImpl(
    private val dao: UserPreferenceDao
) : SettingsRepository {

    companion object {
        private const val KEY_IS_GRID_VIEW = "is_grid_view"
    }

    override fun isGridView(): Flow<Boolean> {
        return dao.observeString(KEY_IS_GRID_VIEW).map { value ->
            value?.toBoolean() ?: false
        }
    }

    override suspend fun setGridView(isGrid: Boolean) {
        dao.insert(UserPreference(KEY_IS_GRID_VIEW, isGrid.toString()))
    }
}
