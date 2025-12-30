package com.sadeghtahani.notebox.features.notes.presentation.detail.helper

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.sp

/**
 * Minimal markdown visual transformer for the note editor.
 *
 * Supported syntax (render-only, underlying text stays unchanged):
 *  - **bold**          → bold span, asterisks hidden
 *  - _italic_         → italic span, underscores hidden
 *  - "- Item" at BOL  → "• Item" with colored bullet
 */
class MarkdownVisualTransformation(
    private val textColor: Color,
    private val primaryColor: Color
) : VisualTransformation {

    private val hiddenSyntaxStyle = SpanStyle(
        color = Color.Transparent,
        fontSize = 0.sp
    )

    override fun filter(text: AnnotatedString): TransformedText {
        val rawText = text.text
        val styleBuilder = AnnotatedString.Builder(rawText)

        applyBold(rawText, styleBuilder)
        applyItalic(rawText, styleBuilder)

        // Lists need a separate backing string because we visually replace '-' with '•'.
        val listTransformedText = applyLists(rawText, styleBuilder)

        return TransformedText(
            listTransformedText,
            OffsetMapping.Identity
        )
    }

    /**
     * **text** → bold span; asterisks are rendered invisible.
     */
    private fun applyBold(rawText: String, builder: AnnotatedString.Builder) {
        val boldRegex = Regex("\\*\\*(.*?)\\*\\*")

        boldRegex.findAll(rawText).forEach { result ->
            // Bold for the inner segment (without the surrounding ** markers).
            builder.addStyle(
                style = SpanStyle(
                    fontWeight = FontWeight.Bold,
                    color = textColor
                ),
                start = result.range.first + 2,
                end = result.range.last - 1
            )

            // Hide opening and closing markers.
            builder.addStyle(hiddenSyntaxStyle, result.range.first, result.range.first + 2)
            builder.addStyle(hiddenSyntaxStyle, result.range.last - 1, result.range.last + 1)
        }
    }

    /**
     * _text_ → italic span; underscores are rendered invisible.
     */
    private fun applyItalic(rawText: String, builder: AnnotatedString.Builder) {
        val italicRegex = Regex("_(.*?)_")

        italicRegex.findAll(rawText).forEach { result ->
            // Italic for the inner segment (without the surrounding _ markers).
            builder.addStyle(
                style = SpanStyle(
                    fontStyle = FontStyle.Italic,
                    color = textColor
                ),
                start = result.range.first + 1,
                end = result.range.last
            )

            // Hide opening and closing markers.
            builder.addStyle(hiddenSyntaxStyle, result.range.first, result.range.first + 1)
            builder.addStyle(hiddenSyntaxStyle, result.range.last, result.range.last + 1)
        }
    }

    /**
     * "- Item" at the beginning of a line → "• Item" with a primary-colored bullet.
     * Only the first character is replaced; spans still operate on the original indices.
     */
    private fun applyLists(
        rawText: String,
        builder: AnnotatedString.Builder
    ): AnnotatedString {
        val listRegex = Regex("(?m)^-\\s")
        val output = StringBuilder(rawText)

        listRegex.findAll(rawText).forEach { result ->
            // Swap '-' with a visual bullet while keeping string length unchanged.
            output[result.range.first] = '•'

            // Highlight the bullet itself.
            builder.addStyle(
                style = SpanStyle(
                    color = primaryColor,
                    fontWeight = FontWeight.ExtraBold
                ),
                start = result.range.first,
                end = result.range.first + 1
            )
        }

        return AnnotatedString(
            text = output.toString(),
            spanStyles = builder.toAnnotatedString().spanStyles
        )
    }
}
