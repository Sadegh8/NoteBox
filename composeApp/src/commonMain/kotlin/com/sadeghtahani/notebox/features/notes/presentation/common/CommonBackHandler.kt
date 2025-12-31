package com.sadeghtahani.notebox.features.notes.presentation.common

import androidx.compose.runtime.Composable

@Composable
expect fun CommonBackHandler(
    enabled: Boolean = true,
    onBack: () -> Unit
)
