package com.warmpot.android.stackoverflow.utils

import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView

sealed class RecyclerViewDivider(val direction: Int) {
    object Horizontal : RecyclerViewDivider(DividerItemDecoration.HORIZONTAL)
    object Vertical : RecyclerViewDivider(DividerItemDecoration.VERTICAL)
}

fun RecyclerView.addDivider(divider: RecyclerViewDivider) {
    this.addItemDecoration(DividerItemDecoration(this.context, divider.direction))
}

fun RecyclerView.setup(
    adapter: RecyclerView.Adapter<*>,
    divider: RecyclerViewDivider? = null
) {
    this.adapter = adapter
    divider?.also { this.addDivider(it) }
}
