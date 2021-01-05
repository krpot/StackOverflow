package com.warmpot.android.stackoverflow.screen.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes

fun ViewGroup.inflate(@LayoutRes layoutResId: Int, attachToParent: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutResId, this, attachToParent)
}