package com.warmpot.android.stackoverflow.screen.question.list

import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.warmpot.android.stackoverflow.databinding.RowLoadingStateBinding

class LoadingStateViewHolder(
    private val binding: RowLoadingStateBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: LoadingState) {
        binding.apply {
            loadingBar.isVisible = item.isLoading

            messageTxt.isVisible = !item.isLoading
            if (!item.isLoading) {
                messageTxt.text = item.message
            }

            retryBtn.isVisible = item.isRetry
        }
    }
}

data class LoadingState(
    val isLoading: Boolean = false,
    val message: String? = null,
    val isRetry: Boolean = false
)
