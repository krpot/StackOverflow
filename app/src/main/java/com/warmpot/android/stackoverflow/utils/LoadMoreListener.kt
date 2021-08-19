package com.warmpot.android.stackoverflow.utils

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.concurrent.atomic.AtomicBoolean

class LoadMoreListener(
    recyclerView: RecyclerView,
    onLoadMore: () -> Unit
) {
    private var hasMoreData = AtomicBoolean(true)
    private var isLoadMoreInProgress = AtomicBoolean(false)

    init {
        requireCheck(recyclerView)

        val layoutManager = recyclerView.layoutManager as LinearLayoutManager

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (!hasMoreData.get() || isLoadMoreInProgress.get()) return

                val reachedAtBottom = layoutManager.findLastVisibleItemPosition() == recyclerView.adapter!!.itemCount - 1
                if (reachedAtBottom) {
                    isLoadMoreInProgress.set(true)
                    onLoadMore()
                }
            }
        })
    }

    private fun requireCheck(recyclerView: RecyclerView) {
        require(recyclerView.layoutManager is LinearLayoutManager) {
            "LoadMoreListener can be used only with LinearLayoutManager"
        }

        requireNotNull(recyclerView.adapter)
    }

    fun setHasMoreData(value: Boolean) {
        hasMoreData.set(value)
    }

    fun setLoadMoreInProgress(value: Boolean) {
        isLoadMoreInProgress.set(value)
    }
}

fun RecyclerView.onLoadMore(
    onLoadMore: () -> Unit
) = LoadMoreListener(this, onLoadMore)
