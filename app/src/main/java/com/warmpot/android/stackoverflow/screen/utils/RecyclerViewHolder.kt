package com.warmpot.android.stackoverflow.screen.utils

import android.view.View
import androidx.annotation.IdRes
import androidx.recyclerview.widget.RecyclerView

fun <T : View> RecyclerView.ViewHolder.findViewById(@IdRes viewId: Int): T {
    return itemView.findViewById(viewId)
}