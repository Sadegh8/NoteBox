package com.sadeghtahani.notebox.features.notes.presentation.list

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sadeghtahani.notebox.features.notes.presentation.list.components.*
import com.sadeghtahani.notebox.features.notes.presentation.list.data.NoteListUiState
import com.sadeghtahani.notebox.features.notes.presentation.util.animateEnter
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
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val selectedFilter by viewModel.selectedFilter.collectAsStateWithLifecycle()
    val isGridView by viewModel.isGridView.collectAsStateWithLifecycle()
    val tags by viewModel.tags.collectAsStateWithLifecycle()

    NoteListContent(
        uiState = uiState,
        tags = tags,
        searchQuery = searchQuery,
        isGridView = isGridView,
        selectedFilter = selectedFilter,
        onNoteClick = onNoteClick,
        onAddClick = { onNoteClick(0) },
        onToggleView = viewModel::toggleView,
        onFilterClick = viewModel::onFilterChange,
        onSearchQueryChange = viewModel::onSearchQueryChange,
        onPinClick = viewModel::togglePin
    )
}

// STATELESS
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NoteListContent(
    uiState: NoteListUiState,
    tags: List<String>,
    searchQuery: String,
    isGridView: Boolean,
    selectedFilter: String,
    onNoteClick: (Long) -> Unit,
    onAddClick: () -> Unit,
    onToggleView: () -> Unit,
    onFilterClick: (String) -> Unit,
    onSearchQueryChange: (String) -> Unit,
    onPinClick: (Long) -> Unit
) {
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
                        SearchBar(query = searchQuery, onQueryChange = onSearchQueryChange)
                    }

                    item(span = StaggeredGridItemSpan.FullLine) {
                        Spacer(modifier = Modifier.height(8.dp))
                        FilterSection(
                            selectedFilter = selectedFilter,
                            onFilterClick = onFilterClick,
                            tags = tags
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    when (uiState) {
                        is NoteListUiState.Loading -> {
                            item(span = StaggeredGridItemSpan.FullLine) {
                                Box(
                                    modifier = Modifier.fillMaxWidth().height(200.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator(color = colors.primary)
                                }
                            }
                        }

                        is NoteListUiState.Empty -> {
                            item(span = StaggeredGridItemSpan.FullLine) {
                                EmptyStateView("Create your first note!")
                            }
                        }

                        is NoteListUiState.Success -> {
                            // No Search Results logic
                            if (uiState.notes.isEmpty()) {
                                item(span = StaggeredGridItemSpan.FullLine) {
                                    EmptyStateView("No notes found for \"$searchQuery\"")
                                }
                            }

                            if (uiState.pinnedNotes.isNotEmpty()) {
                                item(span = StaggeredGridItemSpan.FullLine) {
                                    SectionLabel("PINNED NOTES", colors.primary)
                                }
                                items(items = uiState.pinnedNotes, key = { it.id }) { note ->
                                    NoteCard(
                                        note = note,
                                        onClick = onNoteClick,
                                        onPinClick = onPinClick,
                                        modifier = Modifier.animateItem()
                                            .animateEnter(uiState.pinnedNotes.indexOf(note))
                                    )
                                }
                                item(span = StaggeredGridItemSpan.FullLine) {
                                    Spacer(modifier = Modifier.height(8.dp))
                                }
                            }

                            if (uiState.otherNotes.isNotEmpty()) {
                                item(span = StaggeredGridItemSpan.FullLine) {
                                    SectionLabel("RECENT NOTES", colors.secondary)
                                }
                                items(items = uiState.otherNotes, key = { it.id }) { note ->
                                    NoteCard(
                                        note = note,
                                        onClick = onNoteClick,
                                        onPinClick = onPinClick,
                                        modifier = Modifier.animateItem()
                                            .animateEnter(uiState.otherNotes.indexOf(note))
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun EmptyStateView(message: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 100.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.EditNote,
            contentDescription = null,
            modifier = Modifier.size(100.dp),
            tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = message,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium
        )
    }
}


/**
 * PREVIEWS
 */
@Preview
@Composable
private fun PreviewNoteListScreenDark() {
    MaterialTheme(
        colorScheme = darkColorScheme(
            primary = Color(0xFF86d678),
            background = Color(0xFF121412),
            onBackground = Color.White,
            surface = Color(0xFF1e211e),
            secondary = Color(0xFFc4c7c5)
        )
    ) {
        NoteListContent(
            isGridView = true,
            selectedFilter = "Work",
            onNoteClick = {},
            onAddClick = {},
            onToggleView = {},
            onFilterClick = {},
            searchQuery = "",
            onSearchQueryChange = {},
            onPinClick = {},
            tags = listOf("All", "Work", "Favorites"),
            uiState = NoteListUiState.Success(
                notes = getDummyNotes(),
                pinnedNotes = getDummyNotes().take(2),
                otherNotes = getDummyNotes().drop(2)
            )
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
            isGridView = false,
            selectedFilter = "Work",
            onNoteClick = {},
            onAddClick = {},
            onToggleView = {},
            onFilterClick = {},
            searchQuery = "",
            onSearchQueryChange = {},
            onPinClick = {},
            tags = listOf("All", "Work", "Favorites"),
            uiState = NoteListUiState.Success(
                notes = getDummyNotes(),
                pinnedNotes = getDummyNotes().take(2),
                otherNotes = getDummyNotes().drop(2)
            )
        )
    }
}
