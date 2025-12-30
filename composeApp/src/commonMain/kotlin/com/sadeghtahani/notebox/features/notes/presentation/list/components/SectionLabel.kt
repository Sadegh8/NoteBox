package com.sadeghtahani.notebox.features.notes.presentation.list.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sadeghtahani.notebox.core.theme.NeonGreen
import com.sadeghtahani.notebox.features.notes.presentation.common.alpha
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun SectionLabel(text: String, color: Color) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            color = color,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp,
            modifier = Modifier.alpha(if (color == NeonGreen) 0.9f else 0.5f)
        )
        Spacer(modifier = Modifier.width(12.dp))
        HorizontalDivider(modifier = Modifier.weight(1f), color = color.copy(alpha = 0.1f))
    }
}

@Preview(showBackground = true, name = "Section Label Variants")
@Composable
fun PreviewSectionLabel() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF121212))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        SectionLabel(
            text = "ACTIVE NOTES",
            color = Color(0xFF39FF14)
        )

        SectionLabel(
            text = "ARCHIVED",
            color = Color.Gray
        )

        // Custom Color
        SectionLabel(
            text = "IMPORTANT",
            color = Color.Red
        )
    }
}