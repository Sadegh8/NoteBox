package com.sadeghtahani.notebox.features.notes.presentation.detail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.FormatListBulleted
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FormatBold
import androidx.compose.material.icons.filled.FormatItalic
import androidx.compose.material.icons.filled.FormatListBulleted
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ToolbarIconButton(icon: ImageVector, isDark: Boolean) {
    val tint = if (isDark) Color(0xFF9dc794) else Color.Gray
    IconButton(onClick = {}, modifier = Modifier.size(40.dp)) {
        Icon(icon, contentDescription = null, tint = tint, modifier = Modifier.size(20.dp))
    }
}

@Preview(name = "Toolbar Icons - Dark Mode")
@Composable
fun PreviewToolbarIconButtonDark() {
    Row(
        modifier = Modifier
            .background(Color(0xFF0F0F0F))
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ToolbarIconButton(Icons.Default.FormatBold, true)
        ToolbarIconButton(Icons.Default.FormatItalic, true)
        ToolbarIconButton(Icons.AutoMirrored.Filled.FormatListBulleted, true)
        ToolbarIconButton(Icons.Default.Image, true)
    }
}

@Preview(name = "Toolbar Icons - Light Mode")
@Composable
fun PreviewToolbarIconButtonLight() {
    Row(
        modifier = Modifier
            .background(Color.White)
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ToolbarIconButton(Icons.Default.FormatBold, false)
        ToolbarIconButton(Icons.Default.FormatItalic, false)
        ToolbarIconButton(Icons.AutoMirrored.Filled.FormatListBulleted, false)
        ToolbarIconButton(Icons.Default.Image, false)
    }
}