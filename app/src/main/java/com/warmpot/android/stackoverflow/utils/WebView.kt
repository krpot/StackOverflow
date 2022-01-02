package com.warmpot.android.stackoverflow.utils

import android.webkit.WebView

enum class MimeType(val value: String) {
    HTML("text/html")
}

enum class EncodingType(val value: String) {
    UTF_8("utf-8")
}

private fun WebView.loadData(
    data: String,
    mimeType: MimeType,
    encoding: EncodingType
) {
    loadDataWithBaseURL(null, data, mimeType.value, encoding.value, null)
}

fun WebView.loadHtml(html: String) {
    this.loadData(data = html, mimeType = MimeType.HTML, encoding = EncodingType.UTF_8)
}