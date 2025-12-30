package com.sadeghtahani.notebox.features.notes.presentation.detail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun AddTagButton(
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colorScheme

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .border(
                1.dp,
                colors.outline.copy(alpha = 0.2f),
                RoundedCornerShape(8.dp)
            )
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "Add Tag",
            tint = colors.primary,
            modifier = Modifier.size(18.dp)
        )
    }
}

// --- PREVIEWS ---
@Preview(name = "Add Tag Button - Dark Mode")
@Composable
fun PreviewAddTagButtonDark() {
    MaterialTheme(
        colorScheme = darkColorScheme(
            primary = Color(0xFF86d678),
            background = Color(0xFF0F0F0F),
            outline = Color.White
        )
    ) {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(24.dp)
        ) {
            AddTagButton()
        }
    }
}

@Preview(name = "Add Tag Button - Light Mode")
@Composable
fun PreviewAddTagButtonLight() {
    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = Color(0xFF2e7d32),
            background = Color.White,
            outline = Color.Black
        )
    ) {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(24.dp)
        ) {
            AddTagButton()
        }
    }
}

@Preview(name = "Add Tag Button - Grouped")
@Composable
fun PreviewAddTagButtonGrouped() {
    MaterialTheme(colorScheme = darkColorScheme(primary = Color(0xFF86d678))) {
        Row(
            modifier = Modifier
                .background(Color(0xFF1A1A1A))
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            AddTagButton()
        }
    }
}
