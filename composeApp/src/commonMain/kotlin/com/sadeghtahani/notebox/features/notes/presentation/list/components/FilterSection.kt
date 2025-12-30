package com.sadeghtahani.notebox.features.notes.presentation.list.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun FilterSection(
    tags: List<String>,
    selectedFilter: String,
    onFilterClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val primary = MaterialTheme.colorScheme.primary
    val surfaceVariant = MaterialTheme.colorScheme.surfaceVariant
    val onSurfaceVariant = MaterialTheme.colorScheme.onSurfaceVariant
    val outline = MaterialTheme.colorScheme.outline

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        // 2. Add content padding.
        // 'end = 24.dp' ensures the last item has breathing room from the screen edge when scrolled.
        // 'start = 0.dp' assumes the parent already has left padding.
        contentPadding = PaddingValues(end = 24.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        items(tags) { chip ->
            val isSelected = chip == selectedFilter

            // Theme-aware color logic
            val bgColor = if (isSelected) {
                primary.copy(alpha = 0.2f)
            } else {
                surfaceVariant
            }

            val textColor = if (isSelected) primary else onSurfaceVariant

            val borderColor = if (isSelected) {
                primary.copy(alpha = 0.3f)
            } else {
                outline.copy(alpha = 0.1f)
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(bgColor)
                    .border(1.dp, borderColor, RoundedCornerShape(10.dp))
                    .clickable { onFilterClick(chip) }
                    .padding(horizontal = 20.dp, vertical = 10.dp)
            ) {
                Text(
                    text = chip,
                    color = textColor,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

// --- PREVIEWS ---
@Preview(name = "Dark Theme - Neon Green")
@Composable
fun PreviewFilterSectionDark() {
    MaterialTheme(
        colorScheme = darkColorScheme(
            primary = Color(0xFF39FF14), // Neon Green
            surfaceVariant = Color(0xFF1e211e),
            onSurfaceVariant = Color.Gray
        )
    ) {
        Column(
            modifier = Modifier
                .background(Color(0xFF121212))
                .padding(16.dp)
        ) {
            FilterSection(
                selectedFilter = "Favorites",
                onFilterClick = {},
                tags = listOf("All", "Work", "Favorites")
            )
        }
    }
}

@Preview(name = "Light Theme - Blue")
@Composable
fun PreviewFilterSectionLight() {
    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = Color(0xFF2196F3), // Blue
            surfaceVariant = Color(0xFFE8EAE8),
            onSurfaceVariant = Color.Gray
        )
    ) {
        Column(
            modifier = Modifier
                .background(Color.White)
                .padding(16.dp)
        ) {
            FilterSection(
                selectedFilter = "Favorites",
                onFilterClick = {},
                tags = listOf("All", "Work", "Favorites")
            )
        }
    }
}
