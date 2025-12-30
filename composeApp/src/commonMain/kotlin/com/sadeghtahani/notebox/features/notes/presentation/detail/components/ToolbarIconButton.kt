package com.sadeghtahani.notebox.features.notes.presentation.detail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.FormatListBulleted
import androidx.compose.material.icons.filled.FormatBold
import androidx.compose.material.icons.filled.FormatItalic
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ToolbarIconButton(
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = onClick,
        modifier = modifier.size(40.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(20.dp)
        )
    }
}

@Preview(name = "Toolbar Icons - Dark Mode")
@Composable
fun PreviewToolbarIconButtonDark() {
    MaterialTheme(colorScheme = darkColorScheme(onSurfaceVariant = Color(0xFF9dc794))) {
        Row(
            modifier = Modifier
                .background(Color(0xFF0F0F0F))
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ToolbarIconButton(Icons.Default.FormatBold, onClick = {})
            ToolbarIconButton(Icons.Default.FormatItalic, onClick = {})
            ToolbarIconButton(Icons.AutoMirrored.Filled.FormatListBulleted, onClick = {})
            ToolbarIconButton(Icons.Default.Image, onClick = {})
        }
    }
}

@Preview(name = "Toolbar Icons - Light Mode")
@Composable
fun PreviewToolbarIconButtonLight() {
    MaterialTheme(colorScheme = lightColorScheme(onSurfaceVariant = Color.Gray)) {
        Row(
            modifier = Modifier
                .background(Color.White)
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ToolbarIconButton(Icons.Default.FormatBold, onClick = {})
            ToolbarIconButton(Icons.Default.FormatItalic, onClick = {})
            ToolbarIconButton(Icons.AutoMirrored.Filled.FormatListBulleted, onClick = {})
            ToolbarIconButton(Icons.Default.Image, onClick = {})
        }
    }
}