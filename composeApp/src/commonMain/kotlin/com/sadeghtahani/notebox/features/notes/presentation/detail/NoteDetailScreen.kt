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
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sadeghtahani.notebox.features.notes.presentation.common.CommonBackHandler
import com.sadeghtahani.notebox.features.notes.presentation.detail.components.*
import com.sadeghtahani.notebox.features.notes.presentation.detail.data.DetailUiState
import com.sadeghtahani.notebox.features.notes.presentation.detail.data.FormattingType
import com.sadeghtahani.notebox.features.notes.presentation.detail.data.NoteDetailUi
import com.sadeghtahani.notebox.features.notes.presentation.detail.helper.MarkdownVisualTransformation
import com.sadeghtahani.notebox.features.notes.presentation.detail.helper.applyFormatting
import com.sadeghtahani.notebox.features.notes.presentation.detail.helper.getActiveFormats
import com.sadeghtahani.notebox.features.notes.presentation.detail.helper.handleAutoList
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun NoteDetailScreen(
    noteId: Long?,
    onBackClick: () -> Unit = {}
) {
    val viewModel = koinViewModel<NoteDetailViewModel>(
        parameters = { parametersOf(noteId) }
    )

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val existingTags by viewModel.existingTags.collectAsStateWithLifecycle()


    // Dialog States
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showAddTagDialog by remember { mutableStateOf(false) }
    var tagToDelete by remember { mutableStateOf<String?>(null) } // State for tag removal
    var newTagText by remember { mutableStateOf("") }



    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete Note") },
            text = { Text("Are you sure you want to delete this note?") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.deleteNote { showDeleteDialog = false; onBackClick() }
                }) { Text("Delete", color = MaterialTheme.colorScheme.error) }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancel", color = MaterialTheme.colorScheme.onSurface)
                }
            }
        )
    }

    when (val state = uiState) {
        is DetailUiState.Loading -> Box(
            Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
        }

        is DetailUiState.Error -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(state.message, color = MaterialTheme.colorScheme.error)
        }

        is DetailUiState.Success -> {
            var currentNote by remember(state.note) { mutableStateOf(state.note) }
            var contentFieldValue by remember(state.note.content) {
                mutableStateOf(TextFieldValue(state.note.content))
            }

            val activeFormats = remember(contentFieldValue.selection, contentFieldValue.text) {
                val formats = getActiveFormats(contentFieldValue)
                val activeSet = mutableSetOf<FormattingType>()
                if (formats.isBold) activeSet.add(FormattingType.BOLD)
                if (formats.isItalic) activeSet.add(FormattingType.ITALIC)
                if (formats.isList) activeSet.add(FormattingType.LIST)
                activeSet
            }

            // Sync Content
            LaunchedEffect(contentFieldValue.text) {
                if (currentNote.content != contentFieldValue.text) {
                    currentNote = currentNote.copy(content = contentFieldValue.text)
                }
            }

            val saveAndExit = {
                viewModel.saveNote(currentNote)
                onBackClick()
            }

            CommonBackHandler(onBack = saveAndExit)

            if (tagToDelete != null) {
                AlertDialog(
                    onDismissRequest = { tagToDelete = null },
                    title = { Text("Remove Tag") },
                    text = { Text("Remove '${tagToDelete}'?") },
                    confirmButton = {
                        TextButton(onClick = {
                            currentNote = currentNote.copy(tags = currentNote.tags - tagToDelete!!)
                            tagToDelete = null
                        }) { Text("Remove", color = MaterialTheme.colorScheme.error) }
                    },
                    dismissButton = {
                        TextButton(onClick = { tagToDelete = null }) { Text("Cancel") }
                    }
                )
            }

            // 2. Handle Add Tag
            if (showAddTagDialog) {
                AlertDialog(
                    onDismissRequest = { showAddTagDialog = false },
                    title = { Text("Add Tag") },
                    text = {
                        Column {
                            OutlinedTextField(
                                value = newTagText,
                                onValueChange = { if (it.length <= 15) newTagText = it },
                                label = { Text("Tag Name") },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(modifier = Modifier.height(12.dp))

                            // SUGGESTIONS ROW
                            Text("Suggestions:", style = MaterialTheme.typography.labelMedium)
                            Spacer(modifier = Modifier.height(8.dp))
                            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                items(existingTags.filter { it !in currentNote.tags }) { suggestion ->
                                    TagChip(
                                        text = suggestion,
                                        onClick = {
                                            currentNote =
                                                currentNote.copy(tags = currentNote.tags + suggestion)
                                            showAddTagDialog = false
                                        }
                                    )
                                }
                            }
                        }
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                if (newTagText.isNotBlank() && !currentNote.tags.contains(newTagText)) {
                                    currentNote =
                                        currentNote.copy(tags = currentNote.tags + newTagText)
                                }
                                newTagText = ""
                                showAddTagDialog = false
                            },
                            enabled = newTagText.isNotBlank()
                        ) { Text("Add") }
                    },
                    dismissButton = {
                        TextButton(onClick = { showAddTagDialog = false }) { Text("Cancel") }
                    }
                )
            }

            NoteDetailContent(
                noteUi = currentNote,
                contentFieldValue = contentFieldValue,
                activeFormats = activeFormats,
                onBackClick = saveAndExit,
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
                onTagLongClick = { tag -> tagToDelete = tag },
                onFormatClick = { type ->
                    contentFieldValue = applyFormatting(contentFieldValue, type)
                }
            )
        }
    }
}

// STATELESS
@Composable
fun NoteDetailContent(
    noteUi: NoteDetailUi,
    contentFieldValue: TextFieldValue,
    onBackClick: () -> Unit,
    activeFormats: Set<FormattingType>,
    onTitleChange: (String) -> Unit,
    onContentChange: (TextFieldValue) -> Unit,
    onFavoriteToggle: () -> Unit,
    onSaveClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onAddTagClick: () -> Unit,
    onTagLongClick: (String) -> Unit,
    onFormatClick: (FormattingType) -> Unit
) {
    val colors = MaterialTheme.colorScheme
    val isDark = isSystemInDarkTheme()
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
                Modifier.fillMaxWidth().padding(bottom = 24.dp).navigationBarsPadding()
                    .imePadding(),
                contentAlignment = Alignment.Center
            ) {
                EditorToolbar(
                    activeFormats = activeFormats,
                    onSaveClick = onSaveClick,
                    onDeleteClick = onDeleteClick,
                    onFormatClick = onFormatClick
                )
            }
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState())
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 24.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    Modifier.background(colors.primary.copy(alpha = 0.1f), RoundedCornerShape(4.dp))
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text(
                        "Personal",
                        color = colors.primary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
                Text(noteUi.lastEdited, color = colors.onSurfaceVariant, fontSize = 12.sp)
            }
            Spacer(Modifier.height(16.dp))

            BasicTextField(
                value = noteUi.title,
                onValueChange = onTitleChange,
                textStyle = TextStyle(
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    color = colors.onBackground,
                    textDirection = TextDirection.Content,
                    textAlign = TextAlign.Start
                ),
                cursorBrush = SolidColor(colors.primary),
                modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp)
            ) { innerTextField ->
                if (noteUi.title.isEmpty()) {
                    Text(
                        "Untitled Note",
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                        color = colors.onSurfaceVariant.copy(alpha = 0.5f),
                        textAlign = TextAlign.Start
                    )
                }
                innerTextField()
            }
            Spacer(Modifier.height(24.dp))

            LazyRow(
                contentPadding = PaddingValues(horizontal = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(noteUi.tags) { tag ->
                    TagChip(
                        text = tag,
                        onLongClick = { onTagLongClick(tag) }
                    )
                }
                item {
                    AddTagButton(onClick = onAddTagClick)
                }
            }
            Spacer(Modifier.height(24.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth().padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(colors.surface)
                    .border(1.dp, colors.outline.copy(alpha = 0.1f), RoundedCornerShape(16.dp))
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
                            colors.onSurface,
                            colors.primary
                        )
                    },
                    textStyle = TextStyle(
                        fontSize = 16.sp,
                        color = colors.onSurface,
                        lineHeight = 24.sp,
                        textDirection = TextDirection.Content,
                        textAlign = TextAlign.Start
                    ),
                    cursorBrush = SolidColor(colors.primary),
                    modifier = Modifier.fillMaxSize().focusRequester(focusRequester)
                ) { innerTextField ->
                    if (contentFieldValue.text.isEmpty()) {
                        Text(
                            "Start typing...",
                            fontSize = 16.sp,
                            color = colors.onSurfaceVariant.copy(alpha = 0.5f),
                            textAlign = TextAlign.Start
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
            onFormatClick = {},
            onTagLongClick = {},
            activeFormats = emptySet()
        )
    }
}
