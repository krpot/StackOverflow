package com.warmpot.android.stackoverflow.utils

import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.style.*
import androidx.annotation.ColorInt
import androidx.core.text.HtmlCompat

fun String?.toHtml(): Spanned {
    return HtmlCompat.fromHtml(this ?: "", HtmlCompat.FROM_HTML_MODE_LEGACY)
}

fun String?.toSpan(
    spanStyle: CharacterStyle,
    start: Int = 0,
    end: Int = this?.length ?: 0,
    flags: Int = Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
): SpannableString? {
    val self = this ?: return null
    return SpannableString(self).apply {
        setSpan(spanStyle, start, end, flags)
    }
}

fun String?.toSpan(
    spanStyle: ParagraphStyle,
    start: Int = 0,
    end: Int = this?.length ?: 0,
    flags: Int = Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
): SpannableString? {
    val self = this ?: return null
    return SpannableString(self).apply {
        setSpan(spanStyle, start, end, flags)
    }
}

fun String?.toRelativeSizeSpan(
    proportion: Float,
    start: Int = 0,
    end: Int = this?.length ?: 0,
    flags: Int = Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
) = this.toSpan(
    spanStyle = RelativeSizeSpan(proportion),
    start = start,
    end = end,
    flags = flags
)

// color - ContextCompat.getColor(context, colorRes)
fun String?.toColorSpan(
    @ColorInt color: Int,
    start: Int = 0,
    end: Int = this?.length ?: 0,
    flags: Int = Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
) = this.toSpan(
    spanStyle = ForegroundColorSpan(color),
    start = start,
    end = end,
    flags = flags
)

fun String?.toBulletSpan(
    gapWidth: Int,
    @ColorInt color: Int,
    start: Int = 0,
    end: Int = this?.length ?: 0,
    flags: Int = Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
) = this.toSpan(
    BulletSpan(gapWidth, color),
    start,
    end,
    flags
)
