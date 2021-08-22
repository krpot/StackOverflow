package com.warmpot.android.stackoverflow.screen.common.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.warmpot.android.stackoverflow.utils.inflate

class LoadingStateAdapter : ListAdapter<LoadingState, LoadingStateViewHolder>(DIFF_CALLBACK) {

    private var retryClicked: (() -> Unit)? = null

    fun onRetryClicked(callback: () -> Unit) {
        this.retryClicked = callback
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).viewType
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LoadingStateViewHolder {
        val itemView = parent.inflate(viewType)
        return LoadingStateViewHolder(itemView, retryClicked)
    }

    override fun onBindViewHolder(holder: LoadingStateViewHolder, position: Int) {
        val item = getItem(position) ?: return
        holder.bind(item)
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<LoadingState>() {
            override fun areItemsTheSame(oldItem: LoadingState, newItem: LoadingState): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: LoadingState, newItem: LoadingState): Boolean {
                return oldItem == newItem
            }
        }
    }
}
