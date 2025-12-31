package com.sadeghtahani.notebox.features.notes.presentation.detail.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.sadeghtahani.notebox.features.notes.presentation.detail.NoteDetailContent
import com.sadeghtahani.notebox.features.notes.presentation.detail.data.DetailUiAction
import com.sadeghtahani.notebox.features.notes.presentation.detail.data.FormattingType
import com.sadeghtahani.notebox.features.notes.presentation.detail.data.NoteDetailUi
import com.sadeghtahani.notebox.features.notes.presentation.detail.helper.applyFormatting
import com.sadeghtahani.notebox.features.notes.presentation.detail.helper.getActiveFormats
import com.sadeghtahani.notebox.features.notes.presentation.detail.helper.handleAutoList

@Composable
fun NoteDetailSuccessContent(
    note: NoteDetailUi,
    availableTags: List<String>,
    snackbarHostState: SnackbarHostState,
    onAction: (DetailUiAction) -> Unit,
    onExportRequest: () -> Unit
) {
    var contentFieldValue by remember { mutableStateOf(TextFieldValue(note.content)) }

    LaunchedEffect(note.content) {
        if (contentFieldValue.text != note.content) {
            contentFieldValue = contentFieldValue.copy(text = note.content)
        }
    }

    val activeFormats = remember(contentFieldValue.selection, contentFieldValue.text) {
        val formats = getActiveFormats(contentFieldValue)
        val activeSet = mutableSetOf<FormattingType>()
        if (formats.isBold) activeSet.add(FormattingType.BOLD)
        if (formats.isItalic) activeSet.add(FormattingType.ITALIC)
        if (formats.isList) activeSet.add(FormattingType.LIST)
        activeSet
    }

    // Dialog States
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showAddTagDialog by remember { mutableStateOf(false) }
    var tagToDelete by remember { mutableStateOf<String?>(null) }
    var newTagText by remember { mutableStateOf("") }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete Note") },
            text = { Text("Are you sure you want to delete this note?") },
            confirmButton = {
                TextButton(onClick = {
                    showDeleteDialog = false
                    onAction(DetailUiAction.DeleteNote)
                }) { Text("Delete", color = MaterialTheme.colorScheme.error) }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancel", color = MaterialTheme.colorScheme.onSurface)
                }
            }
        )
    }

    if (tagToDelete != null) {
        AlertDialog(
            onDismissRequest = { tagToDelete = null },
            title = { Text("Remove Tag") },
            text = { Text("Remove '${tagToDelete}'?") },
            confirmButton = {
                TextButton(onClick = {
                    val updatedTags = note.tags - tagToDelete!!
                    onAction(DetailUiAction.UpdateTags(updatedTags))
                    tagToDelete = null
                }) { Text("Remove", color = MaterialTheme.colorScheme.error) }
            },
            dismissButton = {
                TextButton(onClick = { tagToDelete = null }) { Text("Cancel") }
            }
        )
    }

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

                    Text("Suggestions:", style = MaterialTheme.typography.labelMedium)
                    Spacer(modifier = Modifier.height(8.dp))
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(availableTags.filter { it !in note.tags }) { suggestion ->
                            TagChip(
                                text = suggestion,
                                onClick = {
                                    onAction(DetailUiAction.UpdateTags(note.tags + suggestion))
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
                        if (newTagText.isNotBlank() && !note.tags.contains(newTagText)) {
                            onAction(DetailUiAction.UpdateTags(note.tags + newTagText))
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

    // Main Content
    NoteDetailContent(
        noteUi = note,
        contentFieldValue = contentFieldValue,
        snackbarHostState = snackbarHostState,
        activeFormats = activeFormats,
        onBackClick = { onAction(DetailUiAction.NavigateBack) },
        onTitleChange = { onAction(DetailUiAction.UpdateTitle(it)) },
        onContentChange = { newValue ->
            val autoListValue = handleAutoList(contentFieldValue, newValue)
            contentFieldValue = autoListValue
            onAction(DetailUiAction.UpdateContent(autoListValue.text))
        },
        onFavoriteToggle = { onAction(DetailUiAction.ToggleFavorite) },
        onSaveClick = { onAction(DetailUiAction.SaveNote) },
        onDeleteClick = { showDeleteDialog = true },
        onAddTagClick = { showAddTagDialog = true },
        onTagLongClick = { tag -> tagToDelete = tag },
        onFormatClick = { type ->
            contentFieldValue = applyFormatting(contentFieldValue, type)
            onAction(DetailUiAction.UpdateContent(contentFieldValue.text))
        },
        onExport = onExportRequest
    )
}
