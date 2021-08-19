package com.warmpot.android.stackoverflow.utils

import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView

fun RecyclerView.addHorizontalDivider() = this.addDivider(RecyclerViewDivider.Horizontal)

fun RecyclerView.addVerticalDivider() = this.addDivider(RecyclerViewDivider.Vertical)

fun RecyclerView.addDivider(divider: RecyclerViewDivider) {
    this.addItemDecoration(DividerItemDecoration(this.context, divider.direction))
}

fun RecyclerView.setup(
    adapter: RecyclerView.Adapter<*>,
    divider: RecyclerViewDivider? = null
) {
    this.adapter = adapter
    divider?.also(::addDivider)
}

sealed class RecyclerViewDivider(val direction: Int) {
    object Horizontal : RecyclerViewDivider(DividerItemDecoration.HORIZONTAL)
    object Vertical : RecyclerViewDivider(DividerItemDecoration.VERTICAL)
}
