package com.warmpot.android.stackoverflow.screen.common.recyclerview

import androidx.recyclerview.widget.RecyclerView
import com.warmpot.android.stackoverflow.utils.RecyclerViewDivider
import com.warmpot.android.stackoverflow.utils.setup

class RecyclerViewHelper(
    recyclerView: RecyclerView,
    adapter: RecyclerView.Adapter<*>,
    divider: RecyclerViewDivider? = null,
    onLoadMore: (() -> Unit)? = null
) {
    var loadMoreListener: LoadMoreListener? = null
        private set

    init {
        recyclerView.setup(
            adapter = adapter,
            divider = divider
        )

        onLoadMore?.also { callback ->
            loadMoreListener = recyclerView.onLoadMore {
                callback()
            }
        }
    }

}
