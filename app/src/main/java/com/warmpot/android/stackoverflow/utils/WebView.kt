package com.warmpot.android.stackoverflow.utils

import android.webkit.WebView

fun WebView.loadData(
    data: String,
    mimeType: String = "text/html",
    encoding: String = "utf-8"
) {
    loadDataWithBaseURL(null, data, mimeType, encoding, null)
}
