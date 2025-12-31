package com.sadeghtahani.notebox.core.navigation

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.sadeghtahani.notebox.features.notes.presentation.detail.NoteDetailScreen
import com.sadeghtahani.notebox.features.notes.presentation.list.NoteListScreen

@Composable
fun MainNavigation() {
    val navController = rememberNavController()

    var selectedNoteId by rememberSaveable { mutableStateOf<Long?>(null) }

    BoxWithConstraints {
        val isExpanded = maxWidth >= 840.dp

        if (isExpanded) {
            TwoPaneLayout(
                selectedNoteId = selectedNoteId,
                onNoteClick = { id -> selectedNoteId = id },
                onCloseDetail = { selectedNoteId = null }
            )
        } else {
            NavHost(
                navController = navController,
                startDestination = Route.Home
            ) {
                composable<Route.Home> {
                    NoteListScreen(
                        onNoteClick = { id ->
                            navController.navigate(Route.Detail(id))
                        }
                    )
                }

                composable<Route.Detail> { backStackEntry ->
                    val route: Route.Detail = backStackEntry.toRoute()
                    NoteDetailScreen(
                        noteId = route.noteId,
                        onBackClick = { navController.popBackStack() }
                    )
                }
            }
        }
    }
}

@Composable
private fun TwoPaneLayout(
    selectedNoteId: Long?,
    onNoteClick: (Long) -> Unit,
    onCloseDetail: () -> Unit
) {
    Row(modifier = Modifier.fillMaxSize()) {
        BoxWithConstraints(modifier = Modifier.width(350.dp)) {
            NoteListScreen(onNoteClick = onNoteClick)
        }

        VerticalDivider()

        BoxWithConstraints(modifier = Modifier.weight(1f)) {
            NoteDetailScreen(
                noteId = selectedNoteId,
                onBackClick = onCloseDetail
            )
        }
    }
}