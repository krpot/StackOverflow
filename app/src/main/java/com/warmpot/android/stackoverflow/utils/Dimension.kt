package com.warmpot.android.stackoverflow.utils

import android.content.Context
import android.util.TypedValue

fun Int?.dpToPx(context: Context) = toPx(context, TypedValue.COMPLEX_UNIT_DIP)

fun Int?.spToPx(context: Context) = toPx(context, TypedValue.COMPLEX_UNIT_SP)

fun Int?.toPx(context: Context, unit: Int): Float {
    val value = this ?: return 0f
    return TypedValue.applyDimension(unit, value.toFloat(), context.resources.displayMetrics)
}


