package com.warmpot.android.stackoverflow.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
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

fun View.resStr(@StringRes resId: Int, vararg formatArgs: Any?): String {
    return this.resources.getString(resId, *formatArgs)
}

fun ViewGroup.inflate(@LayoutRes layoutId: Int, attachToParent: Boolean = false): View =
    LayoutInflater.from(this.context).inflate(layoutId, this, attachToParent)
