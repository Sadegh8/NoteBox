package com.sadeghtahani.notebox.features.notes.presentation.detail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.FormatListBulleted
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FormatBold
import androidx.compose.material.icons.filled.FormatItalic
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun EditorToolbar(
    isDark: Boolean,
    primaryColor: Color,
    onSaveClick: () -> Unit
) {
    val containerColor = if (isDark) Color(0xFF1f331b) else Color.White

    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(containerColor)
            .border(
                1.dp,
                if (isDark) Color.White.copy(alpha = 0.1f) else Color.Black.copy(alpha = 0.05f),
                RoundedCornerShape(50)
            )
            .padding(6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        ToolbarIconButton(Icons.Default.FormatBold, isDark)
        ToolbarIconButton(Icons.Default.FormatItalic, isDark)
        ToolbarIconButton(Icons.AutoMirrored.Filled.FormatListBulleted, isDark)
        ToolbarIconButton(Icons.Default.Image, isDark)

        Box(
            modifier = Modifier
                .width(1.dp)
                .height(20.dp)
                .background(if (isDark) Color(0xFF2a4625) else Color.LightGray)
                .padding(horizontal = 4.dp)
        )

        Button(
            onClick = onSaveClick,
            colors = ButtonDefaults.buttonColors(containerColor = primaryColor),
            shape = RoundedCornerShape(50),
            contentPadding = PaddingValues(horizontal = 20.dp),
            modifier = Modifier.height(40.dp)
        ) {
            Icon(Icons.Default.Save, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("Save")
        }

        // Delete Button
        IconButton(
            onClick = {},
            modifier = Modifier.size(40.dp)
        ) {
            Icon(
                Icons.Default.Delete,
                contentDescription = "Delete",
                tint = if (isDark) Color(0xFF5a7a55) else Color.Gray
            )
        }
    }
}

@Preview(name = "Editor Toolbar - Dark Mode")
@Composable
fun PreviewEditorToolbarDark() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF0F0F0F))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        EditorToolbar(
            isDark = true,
            primaryColor = Color(0xFF39FF14),
            onSaveClick = {}
        )
    }
}

@Preview(name = "Editor Toolbar - Light Mode")
@Composable
fun PreviewEditorToolbarLight() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF5F5F5))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        EditorToolbar(
            isDark = false,
            primaryColor = Color(0xFF2196F3),
            onSaveClick = {}
        )
    }
}

@Preview(name = "Editor Toolbar - Comparison")
@Composable
fun PreviewEditorToolbarComparison() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Gray)
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        EditorToolbar(
            isDark = true,
            primaryColor = Color(0xFF39FF14),
            onSaveClick = {}
        )

        EditorToolbar(
            isDark = false,
            primaryColor = Color(0xFF2196F3),
            onSaveClick = {}
        )
    }
}