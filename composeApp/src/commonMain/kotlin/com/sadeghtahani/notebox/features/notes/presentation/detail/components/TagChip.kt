package com.sadeghtahani.notebox.features.notes.presentation.detail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Tag
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun TagChip(text: String, isDark: Boolean, primaryColor: Color, onDelete: () -> Unit = {}) {
    val bgColor = if (isDark) Color(0xFF2a4625) else Color.White
    val borderColor = if (isDark) Color.Transparent else Color.LightGray

    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(bgColor)
            .border(1.dp, borderColor, RoundedCornerShape(8.dp))
            .padding(horizontal = 12.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Icon(
            Icons.Default.Tag,
            contentDescription = null,
            tint = if (isDark) primaryColor else Color.Gray,
            modifier = Modifier.size(14.dp)
        )
        Text(
            text = text,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = if (isDark) Color.White else Color.DarkGray
        )
    }
}


@Preview(name = "Tag Chips - Dark Mode")
@Composable
fun PreviewTagChipDark() {
    Column(
        modifier = Modifier
            .background(Color(0xFF0F0F0F))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            TagChip(
                text = "Work",
                isDark = true,
                primaryColor = Color(0xFF39FF14)
            )
            TagChip(
                text = "Important",
                isDark = true,
                primaryColor = Color(0xFF39FF14)
            )
        }
    }
}

@Preview(name = "Tag Chips - Light Mode")
@Composable
fun PreviewTagChipLight() {
    Column(
        modifier = Modifier
            .background(Color(0xFFF5F5F5))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            TagChip(
                text = "Personal",
                isDark = false,
                primaryColor = Color(0xFF2196F3)
            )
            TagChip(
                text = "Travel",
                isDark = false,
                primaryColor = Color(0xFF2196F3)
            )
        }
    }
}

@Preview(name = "Tag Chips - Mixed Sizes")
@Composable
fun PreviewTagChipVariants() {
    Column(
        modifier = Modifier
            .background(Color(0xFF1A1A1A))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        TagChip(
            text = "Project Research 2024",
            isDark = true,
            primaryColor = Color(0xFFBB86FC)
        )
        TagChip(
            text = "Ideas",
            isDark = true,
            primaryColor = Color(0xFFBB86FC)
        )
    }
}