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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun SectionLabel(
    text: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp, top = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            color = color,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
        )
        Spacer(modifier = Modifier.width(12.dp))

        HorizontalDivider(
            modifier = Modifier.weight(1f),
            color = color.copy(alpha = 0.3f)
        )
    }
}

// --- PREVIEWS ---
@Preview(showBackground = true, name = "Section Label - Dark Theme")
@Composable
fun PreviewSectionLabelDark() {
    MaterialTheme(colorScheme = darkColorScheme()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Simulating "Pinned"
            SectionLabel(
                text = "ACTIVE NOTES",
                color = MaterialTheme.colorScheme.primary
            )

            // Simulating "Recent"
            SectionLabel(
                text = "ARCHIVED",
                color = MaterialTheme.colorScheme.secondary
            )
        }
    }
}

@Preview(showBackground = true, name = "Section Label - Light Theme")
@Composable
fun PreviewSectionLabelLight() {
    MaterialTheme(colorScheme = lightColorScheme()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            SectionLabel(
                text = "ACTIVE NOTES",
                color = MaterialTheme.colorScheme.primary
            )

            SectionLabel(
                text = "ARCHIVED",
                color = MaterialTheme.colorScheme.secondary
            )
        }
    }
}