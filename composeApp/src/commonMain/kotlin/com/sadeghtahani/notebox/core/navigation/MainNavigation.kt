package com.sadeghtahani.notebox.core.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.layout.PaneAdaptedValue
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sadeghtahani.notebox.features.notes.presentation.detail.NoteDetailScreen
import com.sadeghtahani.notebox.features.notes.presentation.list.NoteListScreen
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun MainNavigation() {
    val navigator = rememberListDetailPaneScaffoldNavigator<Long>()
    val scope = rememberCoroutineScope()

    ListDetailPaneScaffold(
        modifier = Modifier.background(MaterialTheme.colorScheme.background),
        directive = navigator.scaffoldDirective,
        value = navigator.scaffoldValue,
        listPane = {
            AnimatedPane {
                NoteListScreen(onNoteClick = { id ->
                    scope.launch {
                        navigator.navigateTo(ListDetailPaneScaffoldRole.Detail, id)
                    }
                })
            }
            HorizontalDivider(
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.35f),
                modifier = Modifier
                    .fillMaxHeight()
                    .width(1.dp)
            )
        },
        detailPane = {
            AnimatedPane {
                val noteId = navigator.currentDestination?.contentKey

                val isListVisible =
                    navigator.scaffoldValue[ListDetailPaneScaffoldRole.List] ==
                            PaneAdaptedValue.Expanded
                val showBackButton = !isListVisible

                NoteDetailScreen(
                    noteId = noteId,
                    showBackButton = showBackButton,
                    onBackClick = {
                        scope.launch { navigator.navigateBack() }
                    }
                )
            }
        }
    )
}
