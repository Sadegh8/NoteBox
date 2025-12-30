package com.sadeghtahani.notebox.features.notes.presentation.detail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.FormatListBulleted
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FormatBold
import androidx.compose.material.icons.filled.FormatItalic
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.sadeghtahani.notebox.features.notes.presentation.detail.data.FormattingType
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun EditorToolbar(
    activeFormats: Set<FormattingType>,
    onSaveClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onFormatClick: (FormattingType) -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colorScheme

    Row(
        modifier = modifier
            .clip(RoundedCornerShape(50))
            .background(colors.surface)
            .border(1.dp, colors.outline.copy(alpha = 0.1f), RoundedCornerShape(50))
            .padding(6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        // Formatting Toggles
        FormatToggleButton(
            icon = Icons.Default.FormatBold,
            isActive = activeFormats.contains(FormattingType.BOLD),
            onClick = { onFormatClick(FormattingType.BOLD) }
        )
        FormatToggleButton(
            icon = Icons.Default.FormatItalic,
            isActive = activeFormats.contains(FormattingType.ITALIC),
            onClick = { onFormatClick(FormattingType.ITALIC) }
        )
        FormatToggleButton(
            icon = Icons.AutoMirrored.Filled.FormatListBulleted,
            isActive = activeFormats.contains(FormattingType.LIST),
            onClick = { onFormatClick(FormattingType.LIST) }
        )

        Box(
            modifier = Modifier
                .width(1.dp)
                .height(20.dp)
                .background(colors.outlineVariant)
                .padding(horizontal = 4.dp)
        )

        Button(
            onClick = onSaveClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = colors.primary,
                contentColor = colors.onPrimary
            ),
            shape = RoundedCornerShape(50),
            contentPadding = PaddingValues(horizontal = 20.dp),
            modifier = Modifier.height(40.dp)
        ) {
            Icon(Icons.Default.Save, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("Save")
        }

        IconButton(
            onClick = onDeleteClick,
            modifier = Modifier.size(40.dp)
        ) {
            Icon(
                Icons.Default.Delete,
                contentDescription = "Delete",
                tint = colors.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun FormatToggleButton(
    icon: ImageVector,
    isActive: Boolean,
    onClick: () -> Unit
) {
    val colors = MaterialTheme.colorScheme

    val containerColor = if (isActive) colors.primaryContainer else Color.Transparent
    val iconColor = if (isActive) colors.onPrimaryContainer else colors.onSurfaceVariant

    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(RoundedCornerShape(50))
            .background(containerColor)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = iconColor
        )
    }
}

// --- PREVIEWS ---
@Preview(name = "Editor Toolbar - Dark Mode")
@Composable
fun PreviewEditorToolbarDark() {
    MaterialTheme(
        colorScheme = darkColorScheme(
            primary = Color(0xFF86d678),
            surface = Color(0xFF1f331b),
            outline = Color.White
        )
    ) {
        Box(modifier = Modifier.background(Color.Black).padding(16.dp)) {
            EditorToolbar(
                onSaveClick = {},
                onDeleteClick = {},
                onFormatClick = {},
                activeFormats = setOf(FormattingType.BOLD, FormattingType.ITALIC)
            )
        }
    }
}

@Preview(name = "Editor Toolbar - Light Mode")
@Composable
fun PreviewEditorToolbarLight() {
    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = Color(0xFF2e7d32),
            surface = Color.White,
            outline = Color.Black
        )
    ) {
        Box(modifier = Modifier.background(Color(0xFFF5F5F5)).padding(16.dp)) {
            EditorToolbar(
                onSaveClick = {},
                onDeleteClick = {},
                onFormatClick = {},
                activeFormats = setOf(FormattingType.BOLD, FormattingType.ITALIC)
            )
        }
    }
}
