package com.sadeghtahani.notebox.features.notes.presentation.list.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material3.*
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
import com.sadeghtahani.notebox.features.notes.presentation.list.data.NoteUi
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun NoteCard(
    modifier: Modifier = Modifier,
    note: NoteUi,
    onClick: (Long) -> Unit,
    onPinClick: (Long) -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick(note.id) },
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .border(
                    1.dp,
                    MaterialTheme.colorScheme.outline.copy(alpha = 0.1f),
                    RoundedCornerShape(24.dp)
                )
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
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp)
                )

                if (note.isPinned) {
                    Icon(
                        imageVector = Icons.Default.PushPin,
                        contentDescription = "Unpin Note",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .size(24.dp)
                            .rotate(45f)
                            .alpha(0.8f)
                            .clip(CircleShape)
                            .clickable { onPinClick(note.id) }
                            .padding(2.dp)
                    )
                } else {
                    Text(
                        text = note.date,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        modifier = Modifier
                            .background(
                                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f),
                                RoundedCornerShape(6.dp)
                            )
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = note.preview,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 20.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(12.dp))

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(note.tagBg)
                    .border(
                        1.dp,
                        note.tagColor.copy(alpha = 0.1f),
                        RoundedCornerShape(6.dp)
                    )
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

// --- PREVIEWS ---
@Preview(name = "Note Card - Pinned (Dark)")
@Composable
fun PreviewNoteCardPinnedDark() {
    val note = NoteUi(
        id = 1,
        title = "Design Meeting with a very long title that truncates",
        preview = "Discussion about the new design system.",
        date = "Oct 24",
        isPinned = true,
        tag = "Work",
        tagColor = Color(0xFF39FF14),
        tagBg = Color(0xFF39FF14).copy(alpha = 0.1f)
    )

    MaterialTheme(colorScheme = darkColorScheme()) {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp)
        ) {
            NoteCard(note = note, onClick = {}, onPinClick = {})
        }
    }
}

@Preview(name = "Note Card - Regular (Light)")
@Composable
fun PreviewNoteCardRegularLight() {
    val note = NoteUi(
        id = 2,
        title = "Grocery List",
        preview = "Milk, Eggs, Bread.",
        date = "Today",
        isPinned = false,
        tag = "Personal",
        tagColor = Color(0xFF2196F3),
        tagBg = Color(0xFF2196F3).copy(alpha = 0.1f)
    )

    MaterialTheme(colorScheme = lightColorScheme()) {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp)
        ) {
            NoteCard(note = note, onClick = {}, onPinClick = {})
        }
    }
}
