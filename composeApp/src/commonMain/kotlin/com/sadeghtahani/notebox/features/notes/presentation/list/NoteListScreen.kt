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
import com.sadeghtahani.notebox.features.notes.presentation.list.components.*
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
    val viewModel = koinViewModel<NoteListViewModel>()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var isGridView by remember { mutableStateOf(false) }
    var selectedFilter by remember { mutableStateOf("All") }

    val renderContent: @Composable (List<NoteUi>) -> Unit = { notes ->
        val filteredNotes = if (selectedFilter == "All") notes else notes.filter {
            if (selectedFilter == "Favorites") false /* Add isFavorite to NoteUi first */
            else it.tag.equals(selectedFilter, ignoreCase = true)
        }

        NoteListContent(
            notes = filteredNotes,
            isGridView = isGridView,
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
                // Uses the Primary color defined in your AppTheme
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
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
    isGridView: Boolean,
    selectedFilter: String,
    onNoteClick: (Long) -> Unit,
    onAddClick: () -> Unit,
    onToggleView: () -> Unit,
    onFilterClick: (String) -> Unit,
) {
    val pinnedNotes = remember(notes) { notes.filter { it.isPinned } }
    val recentNotes = remember(notes) { notes.filter { !it.isPinned } }
    val colors = MaterialTheme.colorScheme

    Scaffold(
        containerColor = colors.background,
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddClick,
                containerColor = colors.primary,
                contentColor = colors.onPrimary,
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
            Box(
                modifier = Modifier
                    .offset(x = (-100).dp, y = (-100).dp)
                    .size(300.dp)
                    .background(
                        Brush.radialGradient(
                            listOf(colors.primary.copy(alpha = 0.1f), Color.Transparent)
                        )
                    )
            )

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

                    item(span = StaggeredGridItemSpan.FullLine) {
                        HeaderSection(
                            textColor = colors.onBackground,
                            surfaceColor = colors.surface,
                            primaryColor = colors.primary,
                            isGridView = isGridView,
                            onToggleView = onToggleView
                        )
                    }

                    item(span = StaggeredGridItemSpan.FullLine) {
                        Spacer(modifier = Modifier.height(8.dp))
                        SearchBar()
                    }

                    item(span = StaggeredGridItemSpan.FullLine) {
                        Spacer(modifier = Modifier.height(8.dp))

                        FilterSection(
                            selectedFilter = selectedFilter,
                            onFilterClick = onFilterClick
                        )

                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    if (pinnedNotes.isNotEmpty()) {
                        item(span = StaggeredGridItemSpan.FullLine) {
                            SectionLabel("PINNED NOTES", colors.primary)
                        }

                        items(pinnedNotes) { note ->
                            NoteCard(
                                note = note,
                                onClick = onNoteClick
                            )
                        }

                        item(span = StaggeredGridItemSpan.FullLine) {
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }

                    item(span = StaggeredGridItemSpan.FullLine) {
                        SectionLabel("RECENT NOTES", colors.secondary)
                    }

                    items(recentNotes) { note ->
                        NoteCard(
                            note = note,
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
    // Simulating Dark Theme Setup
    MaterialTheme(
        colorScheme = darkColorScheme(
            primary = Color(0xFF86d678), // NeonGreen
            background = Color(0xFF121412),
            onBackground = Color.White,
            surface = Color(0xFF1e211e),
            secondary = Color(0xFFc4c7c5)
        )
    ) {
        NoteListContent(
            notes = getDummyNotes(),
            isGridView = true,
            selectedFilter = "Work",
            onNoteClick = {},
            onAddClick = {},
            onToggleView = {},
            onFilterClick = {}
        )
    }
}

@Preview
@Composable
private fun PreviewNoteListScreenLight() {
    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = Color(0xFF2e7d32),
            background = Color(0xFFF5F7F5),
            onBackground = Color(0xFF1A1C19),
            surface = Color(0xFFFFFFFF),
            secondary = Color.Gray
        )
    ) {
        NoteListContent(
            notes = getDummyNotes(),
            isGridView = false,
            selectedFilter = "Work",
            onNoteClick = {},
            onAddClick = {},
            onToggleView = {},
            onFilterClick = {}
        )
    }
}
