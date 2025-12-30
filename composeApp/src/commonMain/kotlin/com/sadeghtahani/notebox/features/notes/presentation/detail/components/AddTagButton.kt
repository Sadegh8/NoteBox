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
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun AddTagButton(isDark: Boolean, primaryColor: Color) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .border(
                2.dp,
                if (isDark) Color(0xFF2a4625) else Color.LightGray,
                RoundedCornerShape(8.dp)
            )
            .clickable { }
            .padding(horizontal = 12.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            Icons.Default.Add,
            contentDescription = "Add Tag",
            tint = if (isDark) primaryColor else Color.Gray,
            modifier = Modifier.size(18.dp)
        )
    }
}

@Preview(name = "Add Tag Button - Dark Mode")
@Composable
fun PreviewAddTagButtonDark() {
    Column(
        modifier = Modifier
            .background(Color(0xFF0F0F0F))
            .padding(24.dp)
    ) {
        AddTagButton(
            isDark = true,
            primaryColor = Color(0xFF39FF14)
        )
    }
}

@Preview(name = "Add Tag Button - Light Mode")
@Composable
fun PreviewAddTagButtonLight() {
    Column(
        modifier = Modifier
            .background(Color.White)
            .padding(24.dp)
    ) {
        AddTagButton(
            isDark = false,
            primaryColor = Color(0xFF2196F3)
        )
    }
}

@Preview(name = "Add Tag Button - Grouped with Tags")
@Composable
fun PreviewAddTagButtonGrouped() {
    Row(
        modifier = Modifier
            .background(Color(0xFF1A1A1A))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // TagChip is expected to be in the same package
        TagChip(text = "Meetings", isDark = true, primaryColor = Color(0xFF39FF14))
        TagChip(text = "Project", isDark = true, primaryColor = Color(0xFF39FF14))

        AddTagButton(
            isDark = true,
            primaryColor = Color(0xFF39FF14)
        )
    }
}