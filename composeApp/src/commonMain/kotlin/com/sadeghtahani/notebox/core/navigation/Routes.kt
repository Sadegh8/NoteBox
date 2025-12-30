package com.sadeghtahani.notebox.core.navigation

import kotlinx.serialization.Serializable

sealed interface Route {
    @Serializable
    data object Home : Route

    @Serializable
    data class Detail(val noteId: Long) : Route
}
