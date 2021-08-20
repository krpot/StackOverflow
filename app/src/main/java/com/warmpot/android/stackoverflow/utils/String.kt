package com.warmpot.android.stackoverflow.utils

import android.text.Spanned
import androidx.core.text.HtmlCompat

fun String?.toHtml(): Spanned {
    return HtmlCompat.fromHtml(this ?: "", HtmlCompat.FROM_HTML_MODE_LEGACY)
}
