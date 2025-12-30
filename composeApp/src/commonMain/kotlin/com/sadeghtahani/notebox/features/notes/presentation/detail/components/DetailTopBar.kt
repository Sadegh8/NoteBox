package com.sadeghtahani.notebox.features.notes.presentation.detail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun DetailTopBar(
    isDark: Boolean,
    onBackClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    isFavorite: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onBackClick,
            modifier = Modifier
                .clip(CircleShape)
                .background(if (isDark) Color.White.copy(alpha = 0.1f) else Color.Black.copy(alpha = 0.05f))
        ) {
            // TODO fix this it's not center and click doesn't work
            Icon(
                Icons.AutoMirrored.Filled.ArrowBackIos,
                contentDescription = "Back",
                modifier = Modifier.size(18.dp)
            )
        }

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            IconButton(
                onClick = onFavoriteClick,
                modifier = Modifier
                    .clip(CircleShape)
                    .background(
                        if (isDark) Color.White.copy(alpha = 0.1f) else Color.Black.copy(
                            alpha = 0.05f
                        )
                    )
            ) {
                Icon(
                    if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = "Favorite",
                    tint = if (isFavorite) Color.Red else LocalContentColor.current
                )
            }
            IconButton(
                onClick = {},
                modifier = Modifier
                    .clip(CircleShape)
                    .background(
                        if (isDark) Color.White.copy(alpha = 0.1f) else Color.Black.copy(
                            alpha = 0.05f
                        )
                    )
            ) {
                Icon(Icons.Default.MoreVert, contentDescription = "More")
            }
        }
    }
}


@Preview(name = "Detail Top Bar - Dark Favorite")
@Composable
fun PreviewDetailTopBarDarkFavorite() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF0F0F0F))
    ) {
        DetailTopBar(
            isDark = true,
            isFavorite = true,
            onBackClick = {},
            onFavoriteClick = {}
        )
    }
}

@Preview(name = "Detail Top Bar - Light Unfavorite")
@Composable
fun PreviewDetailTopBarLight() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
    ) {
        DetailTopBar(
            isDark = false,
            isFavorite = false,
            onBackClick = {},
            onFavoriteClick = {}
        )
    }
}
