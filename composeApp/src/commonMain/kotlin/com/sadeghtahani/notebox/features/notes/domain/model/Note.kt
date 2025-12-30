package com.sadeghtahani.notebox.features.notes.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Note(
    val id: Long = 0,
    val title: String,
    val content: String,
    val isPinned: Boolean = false,
    val createdAt: Long,
    val updatedAt: Long,
    val color: Long,
    val tags: List<String> = emptyList()
)
