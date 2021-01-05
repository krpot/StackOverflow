package com.warmpot.android.stackoverflow.screen.utils

import androidx.core.text.HtmlCompat

fun String?.toHtml(): CharSequence {
    return this?.let { HtmlCompat.fromHtml(it, HtmlCompat.FROM_HTML_MODE_LEGACY) } ?: ""
}