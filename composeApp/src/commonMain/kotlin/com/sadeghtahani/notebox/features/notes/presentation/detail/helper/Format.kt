package com.sadeghtahani.notebox.features.notes.presentation.detail.helper

import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import com.sadeghtahani.notebox.features.notes.presentation.detail.data.ActiveFormats
import com.sadeghtahani.notebox.features.notes.presentation.detail.data.FormattingType

fun applyFormatting(value: TextFieldValue, type: FormattingType): TextFieldValue {
    val text = value.text
    val selection = value.selection
    val start = selection.min
    val end = selection.max

    if (type == FormattingType.LIST) {
        val lineStart = text.lastIndexOf('\n', startIndex = (start - 1).coerceAtLeast(0)) + 1
        val lineEnd = text.indexOf('\n', startIndex = end).takeIf { it != -1 } ?: text.length
        val currentLineContent = text.substring(lineStart, lineEnd)

        return if (currentLineContent.trimStart().startsWith("- ")) {
            val validPrefixRegex = Regex("^\\s*-\\s")
            val match = validPrefixRegex.find(currentLineContent)
            val prefixLength = match?.range?.last?.plus(1) ?: 0

            val newText = text.removeRange(lineStart, lineStart + prefixLength)
            // Adjust cursor backwards
            val cursorOffset = if (selection.collapsed) -prefixLength else 0
            value.copy(
                text = newText,
                selection = TextRange((start + cursorOffset).coerceAtLeast(0))
            )
        } else {
            val newText = text.replaceRange(lineStart, lineStart, "- ")
            val cursorOffset = if (selection.collapsed) 2 else 0 // Move cursor after bullet
            value.copy(
                text = newText,
                selection = TextRange(start + cursorOffset, end + 2)
            )
        }
    }

    val (prefix, suffix) = when (type) {
        FormattingType.BOLD -> "**" to "**"
        FormattingType.ITALIC -> "_" to "_"
        else -> "" to ""
    }

    return if (selection.collapsed) {
        val newText = text.replaceRange(start, end, "$prefix$suffix")
        value.copy(
            text = newText,
            selection = TextRange(start + prefix.length) // Place cursor inside
        )
    } else {
        val selectedText = text.substring(start, end)
        val newText = text.replaceRange(start, end, "$prefix$selectedText$suffix")
        value.copy(
            text = newText,
            selection = TextRange(start + prefix.length + selectedText.length + suffix.length)
        )
    }
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

            if (previousLine.trim().startsWith("-")) {
                val prefix = "- "
                val newText =
                    newValue.text.substring(0, newCursor) + prefix + newValue.text.substring(
                        newCursor
                    )

                return newValue.copy(
                    text = newText,
                    selection = TextRange(newCursor + prefix.length)
                )
            }
        }
    }
    return newValue
}

fun getActiveFormats(value: TextFieldValue): ActiveFormats {
    val text = value.text
    val cursor = value.selection.start

    val lineStart = text.lastIndexOf('\n', startIndex = (cursor - 1).coerceAtLeast(0)) + 1
    val lineEnd = text.indexOf('\n', startIndex = cursor).takeIf { it != -1 } ?: text.length
    val currentLine = text.substring(lineStart, lineEnd)
    val isList = currentLine.trimStart().startsWith("- ")

    val boldMatches = Regex("\\*\\*(.*?)\\*\\*").findAll(text)
    val isBold = boldMatches.any { match ->
        cursor > match.range.first && cursor < match.range.last + 1
    }

    val italicMatches = Regex("_(.*?)_").findAll(text)
    val isItalic = italicMatches.any { match ->
        cursor > match.range.first && cursor < match.range.last + 1
    }

    return ActiveFormats(isBold, isItalic, isList)
}
