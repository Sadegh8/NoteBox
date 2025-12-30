package com.sadeghtahani.notebox.features.notes.presentation.list

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sadeghtahani.notebox.core.theme.*
import com.sadeghtahani.notebox.features.notes.presentation.list.components.*
import com.sadeghtahani.notebox.features.notes.presentation.list.data.NoteListThemeColors
import com.sadeghtahani.notebox.features.notes.presentation.list.data.NoteListUiState
import com.sadeghtahani.notebox.features.notes.presentation.list.data.NoteUi
import com.sadeghtahani.notebox.features.notes.presentation.util.getDummyNotes
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

// STATEFUL
@Composable
fun NoteListScreen(
    onNoteClick: (Long) -> Unit,
) {
    // 1. Inject ViewModel via Koin
    val viewModel = koinViewModel<NoteListViewModel>()

    // 2. Collect State safely (Aware of Lifecycle)
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var isGridView by remember { mutableStateOf(false) }

    var selectedFilter by remember { mutableStateOf("All") }

    // Theme Logic
    val isDark = isSystemInDarkTheme()
    val themeColors = if (isDark) {
        NoteListThemeColors(
            background = DarkBackground,
            surface = DarkSurface,
            textPrimary = Color.White,
            textSecondary = TextGray,
            accent = NeonGreen
        )
    } else {
        NoteListThemeColors(
            background = LightBackground,
            surface = LightSurface,
            textPrimary = TextDark,
            textSecondary = Color.Gray,
            accent = NeonGreen
        )
    }

    val renderContent: @Composable (List<NoteUi>) -> Unit = { notes ->
        // OPTIONAL: Filter the list based on selection locally (or do this in ViewModel)
        val filteredNotes = if (selectedFilter == "All") notes else notes.filter {
            if (selectedFilter == "Favorites") false /* Add isFavorite to NoteUi first */ else it.tag.equals(
                selectedFilter,
                ignoreCase = true
            )
        }

        NoteListContent(
            notes = filteredNotes, // Pass filtered notes
            isDark = isDark,
            isGridView = isGridView,
            themeColors = themeColors,
            selectedFilter = selectedFilter,
            onNoteClick = onNoteClick,
            onAddClick = { onNoteClick(0) },
            onToggleView = { isGridView = !isGridView },
            onFilterClick = { selectedFilter = it }
        )
    }

    when (val state = uiState) {
        is NoteListUiState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = androidx.compose.ui.Alignment.Center
            ) {
                CircularProgressIndicator(color = themeColors.accent)
            }
        }

        is NoteListUiState.Empty -> renderContent(emptyList())
        is NoteListUiState.Success -> renderContent(state.notes)
    }
}

// STATELESS
@Composable
fun NoteListContent(
    notes: List<NoteUi>,
    isDark: Boolean,
    isGridView: Boolean,
    themeColors: NoteListThemeColors,
    selectedFilter: String,
    onNoteClick: (Long) -> Unit,
    onAddClick: () -> Unit,
    onToggleView: () -> Unit,
    onFilterClick: (String) -> Unit,
) {
    val pinnedNotes = remember(notes) { notes.filter { it.isPinned } }
    val recentNotes = remember(notes) { notes.filter { !it.isPinned } }

    Scaffold(
        containerColor = themeColors.background,
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddClick,
                containerColor = themeColors.accent,
                contentColor = Color.Black,
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.size(72.dp)
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "Add Note",
                    modifier = Modifier.size(32.dp)
                )
            }
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize()) {
            if (isDark) {
                Box(
                    modifier = Modifier
                        .offset(x = (-100).dp, y = (-100).dp)
                        .size(300.dp)
                        .background(
                            Brush.radialGradient(
                                listOf(themeColors.accent.copy(alpha = 0.1f), Color.Transparent)
                            )
                        )
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 24.dp)
            ) {

                LazyVerticalStaggeredGrid(
                    columns = StaggeredGridCells.Fixed(if (isGridView) 2 else 1),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(top = 24.dp, bottom = 100.dp),
                    verticalItemSpacing = 16.dp,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {

                    // --- Header --- (Spans Full Width)
                    item(span = StaggeredGridItemSpan.FullLine) {
                        HeaderSection(
                            textColor = themeColors.textPrimary,
                            surfaceColor = themeColors.surface,
                            primaryColor = themeColors.accent,
                            isGridView = isGridView,
                            onToggleView = onToggleView
                        )
                    }

                    item(span = StaggeredGridItemSpan.FullLine) {
                        Spacer(modifier = Modifier.height(8.dp))
                        SearchBar(isDark = isDark)
                    }

                    item(span = StaggeredGridItemSpan.FullLine) {
                        Spacer(modifier = Modifier.height(8.dp))

                        FilterSection(
                            isDark = isDark,
                            primaryColor = themeColors.accent,
                            selectedFilter = selectedFilter,
                            onFilterClick = onFilterClick
                        )

                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    if (pinnedNotes.isNotEmpty()) {
                        item(span = StaggeredGridItemSpan.FullLine) {
                            SectionLabel("PINNED NOTES", themeColors.accent)
                        }

                        items(pinnedNotes) { note ->
                            NoteCard(
                                note = note,
                                isDark = isDark,
                                primaryColor = themeColors.accent,
                                onClick = onNoteClick
                            )
                        }

                        item(span = StaggeredGridItemSpan.FullLine) {
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }

                    item(span = StaggeredGridItemSpan.FullLine) {
                        SectionLabel("RECENT NOTES", themeColors.textSecondary)
                    }

                    items(recentNotes) { note ->
                        NoteCard(
                            note = note,
                            isDark = isDark,
                            primaryColor = themeColors.accent,
                            onClick = onNoteClick
                        )
                    }
                }
            }
        }
    }
}


/**
 * PREVIEWS
 */
@Preview
@Composable
private fun PreviewNoteListScreenDark() {
    val dummyColors = NoteListThemeColors(
        background = DarkBackground,
        surface = DarkSurface,
        textPrimary = Color.White,
        textSecondary = TextGray,
        accent = NeonGreen
    )

    MaterialTheme {
        NoteListContent(
            notes = getDummyNotes(),
            isDark = true,
            themeColors = dummyColors,
            onNoteClick = {},
            onAddClick = {},
            onToggleView = {},
            isGridView = true,
            selectedFilter = "Work",
            onFilterClick = {}
        )
    }
}

@Preview
@Composable
private fun PreviewNoteListScreenLight() {
    val dummyColors = NoteListThemeColors(
        background = LightBackground,
        surface = LightSurface,
        textPrimary = TextDark,
        textSecondary = Color.Gray,
        accent = NeonGreen
    )

    MaterialTheme {
        NoteListContent(
            notes = getDummyNotes(),
            isDark = false,
            themeColors = dummyColors,
            onNoteClick = {},
            onAddClick = {},
            onToggleView = {},
            isGridView = false,
            selectedFilter = "Work",
            onFilterClick = {}
        )
    }
}
