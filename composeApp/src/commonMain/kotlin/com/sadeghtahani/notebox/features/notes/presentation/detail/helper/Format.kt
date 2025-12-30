package com.sadeghtahani.notebox.features.notes.presentation.detail.helper

import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import com.sadeghtahani.notebox.features.notes.presentation.detail.data.FormattingType

fun applyFormatting(value: TextFieldValue, type: FormattingType): TextFieldValue {
    val text = value.text
    val selection = value.selection
    val start = selection.min
    val end = selection.max

    // Safety check
    if (start < 0 || end > text.length) return value

    val selectedText = text.substring(start, end)

    // 1. Special Handling for LIST (Multiline support)
    if (type == FormattingType.LIST) {
        if (selectedText.isNotEmpty()) {
            // Split selected text by newlines and add "- " to each line
            val lines = selectedText.split("\n")
            val newSelectedText = lines.joinToString("\n") { line ->
                // Don't add if already exists to prevent double bullets
                if (line.trimStart().startsWith("-")) line else "- $line"
            }

            val newText = text.replaceRange(start, end, newSelectedText)
            // Select the newly formatted text
            return value.copy(
                text = newText,
                selection = TextRange(start, start + newSelectedText.length)
            )
        } else {
            // If nothing selected, just add a new list item
            val insertion = "\n- "
            val newText = text.replaceRange(start, end, insertion)
            return value.copy(
                text = newText,
                selection = TextRange(start + insertion.length)
            )
        }
    }

    // 2. Standard Wrap Handling for BOLD / ITALIC
    val before = text.substring(0, start)
    val after = text.substring(end, text.length)

    val (prefix, suffix) = when (type) {
        FormattingType.BOLD -> "**" to "**"
        FormattingType.ITALIC -> "_" to "_"
        else -> "" to ""
    }

    val newText = "$before$prefix$selectedText$suffix$after"
    val newCursorPos = start + prefix.length + selectedText.length + suffix.length

    return value.copy(text = newText, selection = TextRange(newCursorPos))
}

fun handleAutoList(oldValue: TextFieldValue, newValue: TextFieldValue): TextFieldValue {
    val newCursor = newValue.selection.end

    // Check if user just typed a character (length increased)
    if (newValue.text.length > oldValue.text.length && newCursor > 0) {

        // Check if the character just typed was a Newline ('\n')
        val charTyped = newValue.text[newCursor - 1]
        if (charTyped == '\n') {

            // Find the content of the line BEFORE the enter press
            val textBeforeNewLine = newValue.text.take(newCursor - 1)
            val lastNewLineIndex = textBeforeNewLine.lastIndexOf('\n')
            val previousLine = textBeforeNewLine.substring(lastNewLineIndex + 1)

            // If previous line started with a bullet, add one to the new line
            if (previousLine.trim().startsWith("-")) {
                val prefix = "- "
                val newText = newValue.text.substring(0, newCursor) + prefix + newValue.text.substring(newCursor)

                return newValue.copy(
                    text = newText,
                    selection = TextRange(newCursor + prefix.length)
                )
            }
        }
    }
    return newValue
}
