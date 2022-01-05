package com.warmpot.android.stackoverflow.utils

import android.view.View
import androidx.annotation.AttrRes
import com.google.android.material.color.MaterialColors
import com.warmpot.android.stackoverflow.R

fun View.themeColorOf(@AttrRes colorAttributeResId: Int) =
    MaterialColors.getColor(this, colorAttributeResId)

val View.themeOnPrimaryColor get() = themeColorOf(R.attr.colorOnPrimary)