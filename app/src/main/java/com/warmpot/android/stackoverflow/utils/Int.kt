package com.warmpot.android.stackoverflow.utils

import java.text.NumberFormat
import java.util.*

fun Int.formatted(
    formatter: NumberFormat = NumberFormat.getNumberInstance(Locale.US)
): String {
    return formatter.format(this.toLong())
}