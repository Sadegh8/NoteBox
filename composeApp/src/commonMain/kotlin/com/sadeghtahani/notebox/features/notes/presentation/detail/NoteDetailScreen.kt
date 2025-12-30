package com.sadeghtahani.notebox.features.notes.presentation.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sadeghtahani.notebox.core.theme.*
import com.sadeghtahani.notebox.features.notes.presentation.detail.components.*
import com.sadeghtahani.notebox.features.notes.presentation.detail.data.DetailUiState
import com.sadeghtahani.notebox.features.notes.presentation.detail.data.NoteDetailUi
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

// 1. STATEFUL
@Composable
fun NoteDetailScreen(
    noteId: Long?,
    onBackClick: () -> Unit = {}
) {
    val viewModel = koinViewModel<NoteDetailViewModel>(
        parameters = { parametersOf(noteId) }
    )

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val isDark = isSystemInDarkTheme()

    when (val state = uiState) {
        is DetailUiState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        is DetailUiState.Error -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(state.message, color = Color.Red)
            }
        }

        is DetailUiState.Success -> {
            // We use a local state initialized from the DB data
            // This ensures instant typing response without waiting for DB roundtrips on every keystroke
            var currentNote by remember(state.note) { mutableStateOf(state.note) }

            NoteDetailContent(
                noteUi = currentNote,
                isDark = isDark,
                onBackClick = onBackClick,
                onTitleChange = { currentNote = currentNote.copy(title = it) },
                onContentChange = { currentNote = currentNote.copy(content = it) },
                onFavoriteToggle = {
                    currentNote = currentNote.copy(isFavorite = !currentNote.isFavorite)
                },
                onSaveClick = {
                    viewModel.saveNote(currentNote)
                    onBackClick()
                }
            )
        }
    }
}

// 2. STATELESS
@Composable
fun NoteDetailContent(
    noteUi: NoteDetailUi,
    isDark: Boolean,
    onBackClick: () -> Unit,
    onTitleChange: (String) -> Unit,
    onContentChange: (String) -> Unit,
    onFavoriteToggle: () -> Unit,
    onSaveClick: () -> Unit
) {
    val bgColor = if (isDark) DarkDetailBackground else Color(0xFFf6f8f6)
    val surfaceColor = if (isDark) DarkDetailSurface else Color.White
    val textColor = if (isDark) Color.White else Color(0xFF1e293b)
    val secondaryText = if (isDark) DarkTextSecondary else Color.Gray
    val primaryColor = NeonGreen

    Scaffold(
        modifier = Modifier.fillMaxSize().systemBarsPadding(),
        containerColor = bgColor,
        topBar = {
            DetailTopBar(
                isDark = isDark,
                onBackClick = onBackClick,
                isFavorite = noteUi.isFavorite,
                onFavoriteClick = onFavoriteToggle
            )
        },
        floatingActionButton = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                contentAlignment = Alignment.Center
            ) {
                EditorToolbar(isDark, primaryColor, onSaveClick = onSaveClick)
            }
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            // Meta Data
            Row(
                modifier = Modifier.padding(horizontal = 24.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .background(primaryColor.copy(alpha = 0.1f), RoundedCornerShape(4.dp))
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text(
                        "Personal",
                        color = primaryColor,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
                Text(noteUi.lastEdited, color = secondaryText, fontSize = 12.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Title Input
            BasicTextField(
                value = noteUi.title,
                onValueChange = onTitleChange,
                textStyle = TextStyle(
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    color = textColor
                ),
                cursorBrush = SolidColor(primaryColor),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            ) { innerTextField ->
                if (noteUi.title.isEmpty()) {
                    Text(
                        "Untitled Note",
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Gray
                    )
                }
                innerTextField()
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Tags Row
            LazyRow(
                contentPadding = PaddingValues(horizontal = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(noteUi.tags) { tag ->
                    TagChip(tag, isDark, primaryColor)
                }
                item {
                    AddTagButton(isDark, primaryColor)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Editor Surface
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(surfaceColor)
                    .border(
                        1.dp,
                        if (isDark) Color.White.copy(alpha = 0.05f) else Color.Black.copy(alpha = 0.05f),
                        RoundedCornerShape(16.dp)
                    )
                    .heightIn(min = 500.dp) // Minimum height as per design
                    .padding(16.dp)
            ) {
                BasicTextField(
                    value = noteUi.content,
                    onValueChange = onContentChange,
                    textStyle = TextStyle(
                        fontSize = 16.sp,
                        color = if (isDark) Color(0xFFe2e8f0) else Color(0xFF334155),
                        lineHeight = 24.sp
                    ),
                    cursorBrush = SolidColor(primaryColor),
                    modifier = Modifier.fillMaxSize()
                ) { innerTextField ->
                    if (noteUi.content.isEmpty()) {
                        Text("Start typing...", fontSize = 16.sp, color = Color.Gray)
                    }
                    innerTextField()
                }
            }

            // Extra space for FAB
            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}

// 3. PREVIEWS

@Preview
@Composable
private fun PreviewDetailDark() {
    MaterialTheme {
        NoteDetailContent(
            noteUi = NoteDetailUi(
                title = "Project Ideas 2024",
                content = "Refine roadmap...",
                tags = listOf("ideas", "work")
            ),
            isDark = true,
            onBackClick = {},
            onTitleChange = {},
            onContentChange = {},
            onFavoriteToggle = {},
            onSaveClick = {},
        )
    }
}

@Preview
@Composable
private fun PreviewDetailLight() {
    MaterialTheme {
        NoteDetailContent(
            noteUi = NoteDetailUi(
                title = "Project Ideas 2024",
                content = "Refine roadmap...",
                tags = listOf("ideas", "work")
            ),
            isDark = false,
            onBackClick = {},
            onTitleChange = {},
            onContentChange = {},
            onFavoriteToggle = {},
            onSaveClick = {},
        )
    }
}
