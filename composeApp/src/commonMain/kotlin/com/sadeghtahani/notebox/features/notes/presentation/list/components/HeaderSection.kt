package com.sadeghtahani.notebox.features.notes.presentation.list.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
fun HeaderSection(
    textColor: Color,
    surfaceColor: Color,
    primaryColor: Color,
    isGridView: Boolean,
    onToggleView: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(surfaceColor)
                    .border(1.dp, Color.White.copy(alpha = 0.05f), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.Lock,
                    contentDescription = null,
                    tint = primaryColor,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "Note Box",
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold,
                color = textColor,
                letterSpacing = (-0.5).sp
            )
        }

        IconButton(
            onClick = onToggleView,
            modifier = Modifier
                .clip(CircleShape)
                .background(Color.Transparent)
        ) {
            val icon = if (isGridView) Icons.AutoMirrored.Filled.List else Icons.Default.GridView
            Icon(icon, contentDescription = "Toggle View", tint = Color.Gray)
        }
    }
}

@Preview(name = "Header - Dark Mode")
@Composable
fun PreviewHeaderSectionDark() {
    Column(
        modifier = Modifier
            .background(Color(0xFF0F0F0F))
            .padding(16.dp)
    ) {
        HeaderSection(
            textColor = Color.White,
            surfaceColor = Color(0xFF1E1E1E),
            primaryColor = Color(0xFF39FF14),
            isGridView = true,
            onToggleView = {}
        )
    }
}

@Preview(name = "Header - Light Mode")
@Composable
fun PreviewHeaderSectionLight() {
    Column(
        modifier = Modifier
            .background(Color(0xFFF5F5F5))
            .padding(16.dp)
    ) {
        HeaderSection(
            textColor = Color.Black,
            surfaceColor = Color.White,
            primaryColor = Color(0xFF6200EE),
            isGridView = true,
            onToggleView = {}
        )
    }
}

@Preview(name = "Header - High Contrast")
@Composable
fun PreviewHeaderSectionHighContrast() {
    Column(
        modifier = Modifier
            .background(Color.Black)
            .padding(16.dp)
    ) {
        HeaderSection(
            textColor = Color.White,
            surfaceColor = Color(0xFF2C2C2C),
            primaryColor = Color.Cyan,
            isGridView = false,
            onToggleView = {}
        )
    }
}