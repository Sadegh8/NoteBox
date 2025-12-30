package com.sadeghtahani.notebox.features.notes.presentation.list.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sadeghtahani.notebox.core.theme.DarkSurface
import com.sadeghtahani.notebox.core.theme.LightSurfaceVariant
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun SearchBar(isDark: Boolean) {
    val containerColor = if (isDark) DarkSurface else LightSurfaceVariant
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
            .clip(RoundedCornerShape(50)) // Pill shape
            .background(containerColor)
            .border(1.dp, Color.White.copy(alpha = 0.05f), RoundedCornerShape(50))
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(Icons.Default.Search, contentDescription = null, tint = Color.Gray)
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = "Search encrypted notes...",
            color = Color.Gray,
            fontSize = 14.sp
        )
    }
}


@Preview(name = "Search Bar - Dark Mode")
@Composable
fun PreviewSearchBarDark() {
    Column(
        modifier = Modifier
            .background(Color(0xFF0F0F0F))
            .padding(16.dp)
    ) {
        SearchBar(isDark = true)
    }
}

@Preview(name = "Search Bar - Light Mode")
@Composable
fun PreviewSearchBarLight() {
    Column(
        modifier = Modifier
            .background(Color.White)
            .padding(16.dp)
    ) {
        SearchBar(isDark = false)
    }
}

@Preview(name = "Search Bar - Comparison")
@Composable
fun PreviewSearchBarComparison() {
    Column(
        modifier = Modifier
            .background(Color(0xFF222222))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        SearchBar(isDark = true)

        SearchBar(isDark = false)
    }
}