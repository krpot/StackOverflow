package com.warmpot.android.stackoverflow.utils

import android.content.Context
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat

fun Context.colorRes(@ColorRes resId: Int) = ContextCompat.getColor(this, resId)
