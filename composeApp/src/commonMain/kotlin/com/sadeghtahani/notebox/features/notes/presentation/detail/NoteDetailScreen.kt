package com.sadeghtahani.notebox.features.notes.presentation.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sadeghtahani.notebox.features.notes.presentation.detail.components.*
import com.sadeghtahani.notebox.features.notes.presentation.detail.data.DetailUiState
import com.sadeghtahani.notebox.features.notes.presentation.detail.data.FormattingType
import com.sadeghtahani.notebox.features.notes.presentation.detail.data.NoteDetailUi
import com.sadeghtahani.notebox.features.notes.presentation.detail.helper.MarkdownVisualTransformation
import com.sadeghtahani.notebox.features.notes.presentation.detail.helper.applyFormatting
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

// STATEFUL
@Composable
fun NoteDetailScreen(
    noteId: Long?,
    onBackClick: () -> Unit = {}
) {
    val viewModel = koinViewModel<NoteDetailViewModel>(
        parameters = { parametersOf(noteId) }
    )

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // Dialogs
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showAddTagDialog by remember { mutableStateOf(false) }
    var newTagText by remember { mutableStateOf("") }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete Note") },
            text = { Text("Are you sure?") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.deleteNote { showDeleteDialog = false; onBackClick() }
                }) {
                    Text("Delete", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancel", color = MaterialTheme.colorScheme.onSurface)
                }
            }
        )
    }

    if (showAddTagDialog) {
        AlertDialog(
            onDismissRequest = { showAddTagDialog = false },
            title = { Text("Add Tag") },
            text = {
                OutlinedTextField(
                    value = newTagText,
                    onValueChange = { if (it.length <= 15) newTagText = it },
                    label = { Text("Tag Name") },
                    singleLine = true
                )
            },
            confirmButton = {
                Button(
                    onClick = { showAddTagDialog = false },
                    enabled = newTagText.isNotBlank()
                ) { Text("Add") }
            },
            dismissButton = {
                TextButton(onClick = {
                    showAddTagDialog = false
                }) { Text("Cancel", color = MaterialTheme.colorScheme.onSurface) }
            }
        )
    }

    when (val state = uiState) {
        is DetailUiState.Loading -> Box(
            Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) { CircularProgressIndicator(color = MaterialTheme.colorScheme.primary) }

        is DetailUiState.Error -> Box(
            Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) { Text(state.message, color = MaterialTheme.colorScheme.error) }

        is DetailUiState.Success -> {
            var currentNote by remember(state.note) { mutableStateOf(state.note) }
            var contentFieldValue by remember(state.note.content) {
                mutableStateOf(TextFieldValue(state.note.content))
            }

            // Sync
            LaunchedEffect(contentFieldValue.text) {
                if (currentNote.content != contentFieldValue.text) {
                    currentNote = currentNote.copy(content = contentFieldValue.text)
                }
            }

            // Tag Logic
            if (!showAddTagDialog && newTagText.isNotBlank()) {
                if (!currentNote.tags.contains(newTagText)) currentNote =
                    currentNote.copy(tags = currentNote.tags + newTagText)
                newTagText = ""
            }

            NoteDetailContent(
                noteUi = currentNote,
                contentFieldValue = contentFieldValue,
                onBackClick = onBackClick,
                onTitleChange = { currentNote = currentNote.copy(title = it) },
                onContentChange = { newValue ->
                    val autoListValue = handleAutoList(contentFieldValue, newValue)
                    contentFieldValue = autoListValue
                },
                onFavoriteToggle = {
                    currentNote = currentNote.copy(isFavorite = !currentNote.isFavorite)
                },
                onSaveClick = { viewModel.saveNote(currentNote); onBackClick() },
                onDeleteClick = { showDeleteDialog = true },
                onAddTagClick = { showAddTagDialog = true },
                onFormatClick = { type ->
                    contentFieldValue = applyFormatting(contentFieldValue, type)
                }
            )
        }
    }
}

// LOGIC: Auto-insert Bullet when pressing Enter
private fun handleAutoList(oldValue: TextFieldValue, newValue: TextFieldValue): TextFieldValue {
    if (newValue.text.length > oldValue.text.length) {
        if (newValue.text.endsWith("\n") && oldValue.text.isNotEmpty()) {
            val cursor = oldValue.selection.end
            val textBefore = oldValue.text.substring(0, cursor)
            val lineStart = textBefore.lastIndexOf('\n') + 1
            val currentLine = textBefore.substring(lineStart)

            if (currentLine.trim().startsWith("-")) {
                val newText = newValue.text + "- "
                return newValue.copy(
                    text = newText,
                    selection = TextRange(newText.length)
                )
            }
        }
    }
    return newValue
}

// STATELESS
@Composable
fun NoteDetailContent(
    noteUi: NoteDetailUi,
    contentFieldValue: TextFieldValue,
    onBackClick: () -> Unit,
    onTitleChange: (String) -> Unit,
    onContentChange: (TextFieldValue) -> Unit,
    onFavoriteToggle: () -> Unit,
    onSaveClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onAddTagClick: () -> Unit,
    onFormatClick: (FormattingType) -> Unit
) {
    // Access Theme Colors
    val colors = MaterialTheme.colorScheme
    val isDark =
        isSystemInDarkTheme() // Kept specifically for components that still need boolean logic (like Markdown helpers)

    val focusRequester = remember { FocusRequester() }
    val interactionSource = remember { MutableInteractionSource() }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = colors.background,
        topBar = {
            Box(modifier = Modifier.statusBarsPadding()) {
                DetailTopBar(isDark, onBackClick, onFavoriteToggle, noteUi.isFavorite)
            }
        },
        floatingActionButton = {
            Box(
                Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp)
                    .navigationBarsPadding()
                    .imePadding(),
                contentAlignment = Alignment.Center
            ) {
                EditorToolbar(
                    onSaveClick = onSaveClick,
                    onDeleteClick = onDeleteClick,
                    onFormatClick = onFormatClick
                )
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
                    Modifier
                        .background(colors.primary.copy(alpha = 0.1f), RoundedCornerShape(4.dp))
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text(
                        "Personal",
                        color = colors.primary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
                Text(
                    noteUi.lastEdited,
                    color = colors.onSurfaceVariant, // Secondary Text
                    fontSize = 12.sp
                )
            }
            Spacer(Modifier.height(16.dp))

            // Title
            BasicTextField(
                value = noteUi.title,
                onValueChange = onTitleChange,
                textStyle = TextStyle(
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    color = colors.onBackground // Primary Text
                ),
                cursorBrush = SolidColor(colors.primary),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            ) { innerTextField ->
                if (noteUi.title.isEmpty()) {
                    Text(
                        "Untitled Note",
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                        color = colors.onSurfaceVariant.copy(alpha = 0.5f)
                    )
                }
                innerTextField()
            }
            Spacer(Modifier.height(24.dp))

            // Tags
            LazyRow(
                contentPadding = PaddingValues(horizontal = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(noteUi.tags) {
                    TagChip(it, onDelete = { /* TODO */ })
                }
                item {
                    AddTagButton(onClick = onAddTagClick)
                }
            }
            Spacer(Modifier.height(24.dp))

            // Editor
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(colors.surface)
                    .border(
                        1.dp,
                        colors.outline.copy(alpha = 0.1f), // Standard outline
                        RoundedCornerShape(16.dp)
                    )
                    .heightIn(min = 500.dp)
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null
                    ) { focusRequester.requestFocus() }
                    .padding(16.dp)
            ) {
                BasicTextField(
                    value = contentFieldValue,
                    onValueChange = onContentChange,
                    visualTransformation = remember(isDark) {
                        MarkdownVisualTransformation(
                            textColor = colors.onSurface,
                            primaryColor = colors.primary
                        )
                    },
                    textStyle = TextStyle(
                        fontSize = 16.sp,
                        color = colors.onSurface,
                        lineHeight = 24.sp
                    ),
                    cursorBrush = SolidColor(colors.primary),
                    modifier = Modifier
                        .fillMaxSize()
                        .focusRequester(focusRequester)
                ) { innerTextField ->
                    if (contentFieldValue.text.isEmpty()) {
                        Text(
                            "Start typing...",
                            fontSize = 16.sp,
                            color = colors.onSurfaceVariant.copy(alpha = 0.5f)
                        )
                    }
                    innerTextField()
                }
            }
            Spacer(Modifier.height(100.dp))
        }
    }
}

// PREVIEWS
@Preview
@Composable
private fun PreviewDetailDark() {
    MaterialTheme(colorScheme = darkColorScheme()) {
        NoteDetailContent(
            noteUi = NoteDetailUi(
                title = "Project Ideas 2024",
                content = "Refine roadmap...",
                tags = listOf("ideas", "work")
            ),
            contentFieldValue = TextFieldValue("Refine roadmap..."),
            onBackClick = {},
            onTitleChange = {},
            onContentChange = {},
            onFavoriteToggle = {},
            onSaveClick = {},
            onDeleteClick = {},
            onAddTagClick = {},
            onFormatClick = {}
        )
    }
}