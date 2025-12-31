package com.sadeghtahani.notebox.features.notes.presentation.detail.components

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

/**
 * Minimal markdown visual transformer for the note editor.
 */
class MarkdownVisualTransformation(
    private val textColor: Color,
    private val primaryColor: Color
) : VisualTransformation {

    private val hiddenSyntaxStyle = SpanStyle(
        color = Color.Transparent
    )

    override fun filter(text: AnnotatedString): TransformedText {
        val rawText = text.text
        val styleBuilder = AnnotatedString.Builder(rawText)

        applyBold(rawText, styleBuilder)
        applyItalic(rawText, styleBuilder)

        val listTransformedText = applyLists(rawText, styleBuilder)

        return TransformedText(
            listTransformedText,
            OffsetMapping.Identity
        )
    }

    /**
     * **text** → bold span.
     */
    private fun applyBold(rawText: String, builder: AnnotatedString.Builder) {
        val boldRegex = Regex("\\*\\*([\\s\\S]*?)\\*\\*")

        boldRegex.findAll(rawText).forEach { matchResult ->
            val startIndex = matchResult.range.first
            val endIndex = matchResult.range.last + 1

            builder.addStyle(
                style = SpanStyle(
                    fontWeight = FontWeight.Bold,
                    color = textColor
                ),
                start = startIndex + 2,
                end = endIndex - 2
            )

            builder.addStyle(hiddenSyntaxStyle, startIndex, startIndex + 2)
            builder.addStyle(hiddenSyntaxStyle, endIndex - 2, endIndex)
        }
    }

    /**
     * _text_ → italic span.
     */
    private fun applyItalic(rawText: String, builder: AnnotatedString.Builder) {
        val italicRegex = Regex("_([\\s\\S]*?)_")

        italicRegex.findAll(rawText).forEach { matchResult ->
            val startIndex = matchResult.range.first
            val endIndex = matchResult.range.last + 1

            builder.addStyle(
                style = SpanStyle(
                    fontStyle = FontStyle.Italic,
                    color = textColor
                ),
                start = startIndex + 1,
                end = endIndex - 1
            )

            builder.addStyle(hiddenSyntaxStyle, startIndex, startIndex + 1)
            builder.addStyle(hiddenSyntaxStyle, endIndex - 1, endIndex)
        }
    }

    private fun applyLists(
        rawText: String,
        builder: AnnotatedString.Builder
    ): AnnotatedString {
        val listRegex = Regex("(?m)^-\\s")
        val output = StringBuilder(rawText)

        listRegex.findAll(rawText).forEach { result ->
            output[result.range.first] = '•'

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
