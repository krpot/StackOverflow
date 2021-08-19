package com.warmpot.android.stackoverflow.utils

import android.view.View
import androidx.core.view.isGone
import androidx.core.view.isInvisible
import androidx.core.view.isVisible

fun View.show() {
    this.isVisible = true
}

fun View.hide() {
    this.isGone = true
}

fun View.invisible() {
    this.isInvisible = true
}
