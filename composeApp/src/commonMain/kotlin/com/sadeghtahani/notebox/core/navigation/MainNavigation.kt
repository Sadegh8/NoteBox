package com.sadeghtahani.notebox.core.navigation

import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import com.sadeghtahani.notebox.features.notes.presentation.common.CommonBackHandler
import com.sadeghtahani.notebox.features.notes.presentation.detail.NoteDetailScreen
import com.sadeghtahani.notebox.features.notes.presentation.list.NoteListScreen
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun MainNavigation() {
    val navigator = rememberListDetailPaneScaffoldNavigator<Long>()
    val scope = rememberCoroutineScope()

    CommonBackHandler(enabled = navigator.canNavigateBack()) {
        scope.launch {
            navigator.navigateBack()
        }
    }

    ListDetailPaneScaffold(
        directive = navigator.scaffoldDirective,
        value = navigator.scaffoldValue,
        listPane = {
            AnimatedPane {
                NoteListScreen(
                    onNoteClick = { noteId ->
                        scope.launch {
                            navigator.navigateTo(ListDetailPaneScaffoldRole.Detail, noteId)
                        }
                    }
                )
            }
        },
        detailPane = {
            AnimatedPane {
                val noteId = navigator.currentDestination?.contentKey

                NoteDetailScreen(
                    noteId = noteId,
                    onBackClick = {
                        scope.launch {
                            navigator.navigateBack()
                        }
                    }
                )
            }
        }
    )
}