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
import com.sadeghtahani.notebox.core.util.rememberStoragePermissionLauncher
import com.sadeghtahani.notebox.features.notes.presentation.common.CommonBackHandler
import com.sadeghtahani.notebox.features.notes.presentation.detail.components.*
import com.sadeghtahani.notebox.features.notes.presentation.detail.data.DetailUiAction
import com.sadeghtahani.notebox.features.notes.presentation.detail.data.DetailUiEvent
import com.sadeghtahani.notebox.features.notes.presentation.detail.data.DetailUiState
import com.sadeghtahani.notebox.features.notes.presentation.detail.data.FormattingType
import com.sadeghtahani.notebox.features.notes.presentation.detail.data.NoteDetailUi
import com.sadeghtahani.notebox.features.notes.presentation.detail.components.MarkdownVisualTransformation
import com.sadeghtahani.notebox.features.notes.presentation.detail.helper.formatTimestamp
import com.sadeghtahani.notebox.openFile
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun NoteDetailScreen(
    noteId: Long?,
    showBackButton: Boolean = true,
    onBackClick: () -> Unit = {}
) {
    val viewModel = koinViewModel<NoteDetailViewModel>(
        key = noteId?.toString(),
        parameters = { parametersOf(noteId) }
    )

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val permissionLauncher = rememberStoragePermissionLauncher { isGranted ->
        if (isGranted) {
            viewModel.onAction(DetailUiAction.ExportNote)
        } else {
            scope.launch {
                snackbarHostState.showSnackbar("Permission denied. Cannot export.")
            }
        }
    }

    LaunchedEffect(noteId) {
        if (noteId == null || noteId == 0L) {
            viewModel.resetState()
        }
    }

    CommonBackHandler { viewModel.onAction(DetailUiAction.NavigateBack) }

    LaunchedEffect(viewModel) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is DetailUiEvent.NavigateBack -> onBackClick()
                is DetailUiEvent.ShowMessage -> {
                    launch {
                        snackbarHostState.showSnackbar(event.message)
                    }
                }
                is DetailUiEvent.ExportSuccess -> {
                    launch {
                        val result = snackbarHostState.showSnackbar(
                            message = "Note exported successfully",
                            actionLabel = "Open",
                            duration = SnackbarDuration.Short
                        )
                        if (result == SnackbarResult.ActionPerformed) {
                            openFile(event.filePath)
                        }
                    }
                }
            }
        }
    }

    when (val state = uiState) {
        is DetailUiState.Loading -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            }
        }

        is DetailUiState.Error -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(state.message, color = MaterialTheme.colorScheme.error)
            }
        }

        is DetailUiState.Success -> {
            NoteDetailSuccessContent(
                note = state.note,
                availableTags = state.availableTags,
                snackbarHostState = snackbarHostState,
                showBackButton = showBackButton,
                onAction = viewModel::onAction,
                onExportRequest = { permissionLauncher.launch() }
            )
        }
    }
}

// STATELESS
@Composable
fun NoteDetailContent(
    noteUi: NoteDetailUi,
    contentFieldValue: TextFieldValue,
    snackbarHostState: SnackbarHostState,
    activeFormats: Set<FormattingType>,
    onBackClick: () -> Unit,
    showBackButton: Boolean = true,
    onTitleChange: (String) -> Unit,
    onContentChange: (TextFieldValue) -> Unit,
    onFavoriteToggle: () -> Unit,
    onSaveClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onAddTagClick: () -> Unit,
    onExport: () -> Unit,
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
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            Box(modifier = Modifier.statusBarsPadding()) {
                DetailTopBar(
                    isDark = isDark,
                    showBackButton = showBackButton,
                    onBackClick = onBackClick,
                    onFavoriteClick = onFavoriteToggle,
                    isFavorite = noteUi.isFavorite,
                    onExport = onExport
                )
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
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
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
                        text = "Personal",
                        color = colors.primary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                val dateText = remember(noteUi.lastEditedTimestamp) {
                    formatTimestamp(noteUi.lastEditedTimestamp)
                }
                Text(
                    text = dateText,
                    color = colors.onSurfaceVariant,
                    fontSize = 12.sp
                )
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
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
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
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
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
                    modifier = Modifier
                        .fillMaxSize()
                        .focusRequester(focusRequester)
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
            activeFormats = emptySet(),
            onExport = {},
            snackbarHostState = remember { SnackbarHostState() }
        )
    }
}
