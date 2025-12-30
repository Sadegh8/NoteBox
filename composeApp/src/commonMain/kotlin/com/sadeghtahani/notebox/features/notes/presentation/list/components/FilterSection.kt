package com.sadeghtahani.notebox.features.notes.presentation.list.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sadeghtahani.notebox.core.theme.DarkSurface
import com.sadeghtahani.notebox.core.theme.LightSurfaceVariant
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun FilterSection(
    isDark: Boolean,
    primaryColor: Color,
    selectedFilter: String,
    onFilterClick: (String) -> Unit
) {
    val chips = listOf("All", "Favorites", "Work", "Personal", "Ideas")

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        items(chips) { chip ->
            val isSelected = chip == selectedFilter // <--- 3. Use the passed state

            val bgColor =
                if (isSelected) primaryColor.copy(alpha = 0.2f) else if (isDark) DarkSurface else LightSurfaceVariant
            val textColor = if (isSelected) primaryColor else Color.Gray
            val borderColor =
                if (isSelected) primaryColor.copy(alpha = 0.3f) else Color.White.copy(alpha = 0.05f)

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(bgColor)
                    .border(1.dp, borderColor, RoundedCornerShape(10.dp))
                    .clickable { onFilterClick(chip) } // <--- 4. Trigger the event
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

@Preview(name = "Dark Theme - Neon Green")
@Composable
fun PreviewFilterSectionDark() {
    Column(
        modifier = Modifier
            .background(Color(0xFF121212))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        FilterSection(
            isDark = true,
            primaryColor = Color(0xFF39FF14),
            selectedFilter = "Favorites",
            onFilterClick = {}
        )
    }
}

@Preview(name = "Light Theme - Blue")
@Composable
fun PreviewFilterSectionLight() {
    Column(
        modifier = Modifier
            .background(Color.White)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        FilterSection(
            isDark = false,
            primaryColor = Color(0xFF2196F3),
            selectedFilter = "Favorites",
            onFilterClick = {}
        )
    }
}

@Preview(name = "Dark Theme - Purple")
@Composable
fun PreviewFilterSectionCustom() {
    Column(
        modifier = Modifier
            .background(Color(0xFF1A1A1A))
            .padding(16.dp)
    ) {
        FilterSection(
            isDark = true,
            primaryColor = Color(0xFFBB86FC),
            selectedFilter = "Favorites",
            onFilterClick = {}
        )
    }
}