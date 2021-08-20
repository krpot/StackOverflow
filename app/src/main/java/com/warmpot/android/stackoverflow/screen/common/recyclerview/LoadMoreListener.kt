package com.warmpot.android.stackoverflow.screen.common.recyclerview

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.concurrent.atomic.AtomicBoolean

class LoadMoreListener(
    private val recyclerView: RecyclerView,
    onLoadMore: () -> Unit
) {

    private val layoutManager: LinearLayoutManager = requireNotNull(recyclerView.layoutManager as? LinearLayoutManager) {
        "Only LinearLayoutManager can be use for load more."
    }

    private val adapter = requireNotNull(recyclerView.adapter) { "RecyclerView adapter must be set first." }

    private val isInLoading = AtomicBoolean(false)

    init {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (reachedAtMostBottom() && !isInLoading.get()) {
                    isInLoading.set(true)
                    onLoadMore()
                }
            }
        })
    }

    private fun reachedAtMostBottom(): Boolean = layoutManager.findLastVisibleItemPosition() == adapter.itemCount - 1

    fun loadMoreDone() {
        isInLoading.set(false)
    }
}

fun RecyclerView.onLoadMore(action: () -> Unit): LoadMoreListener {
    return LoadMoreListener(this, action)
}
