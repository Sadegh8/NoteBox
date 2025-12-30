package com.sadeghtahani.notebox.features.notes.presentation.common

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color

@Composable
fun Modifier.alpha(alpha: Float) = this.then(Modifier.drawWithContent {
    drawContent()
    drawRect(Color.Black, alpha = 1f - alpha, blendMode = androidx.compose.ui.graphics.BlendMode.DstIn)
})
