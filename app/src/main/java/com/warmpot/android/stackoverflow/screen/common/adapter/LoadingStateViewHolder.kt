package com.warmpot.android.stackoverflow.screen.question.list

import android.view.View
import androidx.annotation.StringRes
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.warmpot.android.stackoverflow.R
import com.warmpot.android.stackoverflow.databinding.RowLoadingStateBinding
import com.warmpot.android.stackoverflow.screen.common.adapter.ListItem

class LoadingStateViewHolder(
    itemView: View,
    retryClicked: (() -> Unit)? = null
) : RecyclerView.ViewHolder(itemView) {

    private val binding = RowLoadingStateBinding.bind(itemView)

    init {
        binding.retryBtn.setOnClickListener {
            retryClicked?.invoke()
        }
    }

    fun bind(item: LoadingState) {
        binding.apply {
            loadingBar.isVisible = item.isLoading
            errorGroup.isVisible = !item.isLoading
            if (item.isLoading) return@apply

            messageTxt.text = item.message
            retryBtn.isVisible = item.isRetry
        }
    }
}

data class LoadingState(
    val isLoading: Boolean = false,
    val message: String? = null,
    @StringRes val messageId: Int? = null,
    val isRetry: Boolean = false,
    override val viewType: Int = VIEW_TYPE
) : ListItem {
    companion object {
        const val VIEW_TYPE = R.layout.row_loading_state
    }
}
