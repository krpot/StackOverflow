package com.warmpot.android.stackoverflow.screen.common.adapter

import com.warmpot.android.stackoverflow.R
import com.warmpot.android.stackoverflow.screen.common.resource.Str

data class LoadingState(
    val isLoading: Boolean = false,
    val message: Str? = null,
    val isRetry: Boolean = false,
    override val viewType: Int = VIEW_TYPE
) : ListItem {
    companion object {
        const val VIEW_TYPE = R.layout.row_loading_state
    }
}
