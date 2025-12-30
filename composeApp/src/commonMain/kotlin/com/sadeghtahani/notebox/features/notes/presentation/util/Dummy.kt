package com.sadeghtahani.notebox.features.notes.presentation.util

import androidx.compose.ui.graphics.Color
import com.sadeghtahani.notebox.features.notes.presentation.list.data.NoteUi


// --- DUMMY DATA ---
fun getDummyNotes() = listOf(
    NoteUi(
        1,
        "Grocery List",
        "Milk, Eggs, Bread, Spinach, Almonds, Dark Chocolate, Coffee beans (light roast), Avocados...",
        "Today",
        true,
        "Personal",
        Color(0xFFc4c7c5),
        Color(0xFF2c302c)
    ),
    NoteUi(
        2,
        "Q3 Goals Meeting",
        "Discuss privacy updates, new encryption standards, and the rollout plan for the new mobile interface.",
        "Yesterday",
        false,
        "Work",
        Color(0xFFe4a4ff),
        Color(0xFF3e2b46)
    ),
    NoteUi(
        3,
        "Book Ideas",
        "The protagonist finds a key to a hidden digital library that contains the lost memories of humanity...",
        "Oct 24",
        false,
        "Creative",
        Color(0xFFffdca4),
        Color(0xFF463d2b)
    ),
    NoteUi(
        4,
        "Fitness Plan",
        "Monday: 5k run + core. Tuesday: Upper body strength. Wednesday: Rest or light yoga.",
        "Oct 20",
        false,
        "Health",
        Color(0xFF86d678),
        Color(0xFF1e2f1b)
    )
)