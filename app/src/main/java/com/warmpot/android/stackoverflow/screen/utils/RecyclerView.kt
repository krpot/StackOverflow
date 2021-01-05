package com.warmpot.android.stackoverflow.screen.utils

import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

fun RecyclerView.setup(
    recyclerAdapter: RecyclerView.Adapter<*>,
    layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(context),
    hasFixedSize: Boolean = true,
    clipToPadding: Boolean = false,
    showDivider: Boolean = false
): RecyclerView.Adapter<*> {
    this.adapter = recyclerAdapter
    this.layoutManager = layoutManager
    this.setHasFixedSize(hasFixedSize)
    this.clipToPadding = clipToPadding
    if (showDivider) {
        this.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
    }

    return recyclerAdapter
}