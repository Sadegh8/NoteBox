package com.sadeghtahani.notebox.features.notes.presentation.detail.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.sadeghtahani.notebox.core.theme.NeonGreen
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun DetailTopBar(
    isDark: Boolean,
    showBackButton: Boolean = true,
    onBackClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    onExport: () -> Unit,
    isFavorite: Boolean
) {
    val animatedTint by animateColorAsState(
        targetValue = if (isFavorite) NeonGreen else if (isDark) Color.White.copy(0.5f) else Color.Black.copy(
            0.5f
        ),
        label = "Icon Tint Animation"
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (showBackButton) {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier
                    .clip(CircleShape)
                    .background(if (isDark) Color.White.copy(alpha = 0.1f) else Color.Black.copy(alpha = 0.05f))
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBackIos,
                    contentDescription = "Back",
                    modifier = Modifier.size(18.dp).offset(x = 3.dp),
                    tint = if (isDark) Color.White else Color.Black
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

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
                    Icons.Default.PushPin,
                    contentDescription = "Favorite",
                    tint = animatedTint
                )
            }

            IconButton(
                onClick = onExport,
                modifier = Modifier
                    .clip(CircleShape)
                    .background(
                        if (isDark) Color.White.copy(alpha = 0.1f) else Color.Black.copy(
                            alpha = 0.05f
                        )
                    )
            ) {
                Icon(
                    Icons.Default.Save,
                    contentDescription = "Export",
                    tint = NeonGreen
                )
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
            showBackButton = true,
            onBackClick = {},
            onFavoriteClick = {},
            onExport = {}
        )
    }
}

@Preview(name = "Detail Top Bar - No Back Button")
@Composable
fun PreviewDetailTopBarNoBack() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
    ) {
        DetailTopBar(
            isDark = false,
            isFavorite = false,
            showBackButton = false,
            onBackClick = {},
            onFavoriteClick = {},
            onExport = {}
        )
    }
}
