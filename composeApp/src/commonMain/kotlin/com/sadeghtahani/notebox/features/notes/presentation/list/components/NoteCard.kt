package com.sadeghtahani.notebox.features.notes.presentation.list.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sadeghtahani.notebox.core.theme.DarkSurface
import com.sadeghtahani.notebox.core.theme.DarkSurfaceVariant
import com.sadeghtahani.notebox.core.theme.LightSurface
import com.sadeghtahani.notebox.core.theme.LightSurfaceVariant
import com.sadeghtahani.notebox.core.theme.TextDark
import com.sadeghtahani.notebox.core.theme.TextGray
import com.sadeghtahani.notebox.features.notes.presentation.list.data.NoteUi
import org.jetbrains.compose.ui.tooling.preview.Preview


@Composable
fun NoteCard(note: NoteUi, isDark: Boolean, primaryColor: Color, onClick: (Long) -> Unit) {
    val cardBg = if (isDark) DarkSurface else LightSurface
    val hoverColor = if (isDark) DarkSurfaceVariant else LightSurfaceVariant

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp)
            .clickable { onClick(note.id) },
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = cardBg),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .border(1.dp, Color.White.copy(alpha = 0.05f), RoundedCornerShape(24.dp))
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = note.title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = if (isDark) Color.White else TextDark
                )
                if (note.isPinned) {
                    Icon(
                        Icons.Default.PushPin,
                        contentDescription = "Pinned",
                        tint = primaryColor,
                        modifier = Modifier.size(20.dp).rotate(45f).alpha(0.8f)
                    )
                } else {
                    Text(
                        text = note.date,
                        fontSize = 12.sp,
                        color = Color.Gray,
                        modifier = Modifier
                            .background(Color.White.copy(alpha = 0.05f), RoundedCornerShape(6.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = note.preview,
                fontSize = 14.sp,
                color = TextGray,
                lineHeight = 20.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Tag Chip
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(note.tagBg)
                    .border(1.dp, note.tagColor.copy(alpha = 0.1f), RoundedCornerShape(6.dp))
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Text(
                    text = note.tag,
                    color = note.tagColor,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Preview(name = "Note Card - Pinned (Dark)")
@Composable
fun PreviewNoteCardPinnedDark() {
    val note = NoteUi(
        id = 1,
        title = "Design Meeting",
        preview = "Discussion about the new design system and component library for the upcoming project release.",
        date = "Oct 24",
        isPinned = true,
        tag = "Work",
        tagColor = Color(0xFF39FF14),
        tagBg = Color(0xFF39FF14).copy(alpha = 0.1f)
    )

    Column(
        modifier = Modifier
            .background(Color(0xFF0F0F0F))
            .padding(16.dp)
    ) {
        NoteCard(
            note = note,
            isDark = true,
            primaryColor = Color(0xFF39FF14),
            onClick = {}
        )
    }
}

@Preview(name = "Note Card - Regular (Light)")
@Composable
fun PreviewNoteCardRegularLight() {
    val note = NoteUi(
        id = 2,
        title = "Grocery List",
        preview = "Milk, Eggs, Bread, Greek Yogurt, Avocados, and some fresh blueberries.",
        date = "Oct 23",
        isPinned = false,
        tag = "Personal",
        tagColor = Color(0xFF2196F3),
        tagBg = Color(0xFF2196F3).copy(alpha = 0.1f)
    )

    Column(
        modifier = Modifier
            .background(Color(0xFFF5F5F5))
            .padding(16.dp)
    ) {
        NoteCard(
            note = note,
            isDark = false,
            primaryColor = Color(0xFF2196F3),
            onClick = {}
        )
    }
}

@Preview(name = "Note Card - Long Text (Dark)")
@Composable
fun PreviewNoteCardLongText() {
    val note = NoteUi(
        id = 3,
        title = "Inspiration and Ideas for 2024",
        preview = "This is a very long description to test the ellipsis behavior of the note card component. It should cut off after two lines of text to maintain the card height consistency.",
        date = "Oct 20",
        isPinned = false,
        tag = "Ideas",
        tagColor = Color(0xFFFF9800),
        tagBg = Color(0xFFFF9800).copy(alpha = 0.1f)
    )

    Column(
        modifier = Modifier
            .background(Color(0xFF0F0F0F))
            .padding(16.dp)
    ) {
        NoteCard(
            note = note,
            isDark = true,
            primaryColor = Color(0xFF39FF14),
            onClick = {}
        )
    }
}
