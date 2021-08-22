package com.warmpot.android.stackoverflow.screen.common.adapter

import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.warmpot.android.stackoverflow.databinding.RowLoadingStateBinding
import com.warmpot.android.stackoverflow.screen.common.resource.text

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

            messageTxt.text = item.message.text(itemView.context)
            retryBtn.isVisible = item.isRetry
        }
    }
}

