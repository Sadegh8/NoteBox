package com.sadeghtahani.notebox.features.notes.presentation.util

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.graphicsLayer
import kotlinx.coroutines.launch

fun Modifier.animateEnter(
    index: Int,
    delayPerItem: Int = 30
): Modifier = composed {
    val alphaAnim = remember { Animatable(0f) }
    val scaleAnim = remember { Animatable(0.9f) }
    val slideAnim = remember { Animatable(50f) }

    LaunchedEffect(Unit) {
        val delay = index * delayPerItem

        // Parallel animation
        launch {
            alphaAnim.animateTo(
                targetValue = 1f,
                animationSpec = tween(300, delayMillis = delay, easing = LinearOutSlowInEasing)
            )
        }
        launch {
            scaleAnim.animateTo(
                targetValue = 1f,
                animationSpec = tween(300, delayMillis = delay, easing = LinearOutSlowInEasing)
            )
        }
        launch {
            slideAnim.animateTo(
                targetValue = 0f,
                animationSpec = tween(400, delayMillis = delay, easing = LinearOutSlowInEasing)
            )
        }
    }

    this.graphicsLayer {
        alpha = alphaAnim.value
        scaleX = scaleAnim.value
        scaleY = scaleAnim.value
        translationY = slideAnim.value
    }
}
