package com.sadeghtahani.notebox.features.notes.presentation.detail.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Tag
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TagChip(
    text: String,
    onLongClick: () -> Unit = {},
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colorScheme

    Row(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(colors.primary.copy(alpha = 0.1f))
            .border(
                1.dp,
                colors.primary.copy(alpha = 0.2f),
                RoundedCornerShape(8.dp)
            )
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongClick
            )
            .padding(horizontal = 10.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Tag,
            contentDescription = null,
            tint = colors.primary,
            modifier = Modifier.size(14.dp)
        )
        Text(
            text = text,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = colors.onSurface
        )
    }
}

// --- PREVIEWS ---
@Preview(name = "Tag Chips - Dark Mode")
@Composable
fun PreviewTagChipDark() {
    MaterialTheme(colorScheme = darkColorScheme(
        primary = Color(0xFF86d678),
        background = Color(0xFF0F0F0F),
        onSurface = Color.White
    )) {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                TagChip(text = "Work")
                TagChip(text = "Important")
            }
        }
    }
}

@Preview(name = "Tag Chips - Light Mode")
@Composable
fun PreviewTagChipLight() {
    MaterialTheme(colorScheme = lightColorScheme(
        primary = Color(0xFF2e7d32),
        background = Color(0xFFF5F5F5),
        onSurface = Color(0xFF1A1C19)
    )) {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                TagChip(text = "Personal")
                TagChip(text = "Travel")
            }
        }
    }
}

@Preview(name = "Tag Chips - Mixed Sizes")
@Composable
fun PreviewTagChipVariants() {
    MaterialTheme(colorScheme = darkColorScheme(primary = Color(0xFFBB86FC))) {
        Column(
            modifier = Modifier
                .background(Color(0xFF1A1A1A))
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TagChip(text = "Project Research 2024")
            TagChip(text = "Ideas")
        }
    }
}
