package com.warmpot.android.stackoverflow.screen.question.list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.warmpot.android.stackoverflow.screen.common.adapter.ListItem
import com.warmpot.android.stackoverflow.screen.question.list.LoadingState
import com.warmpot.android.stackoverflow.screen.question.list.LoadingStateViewHolder
import com.warmpot.android.stackoverflow.screen.question.model.Question

class QuestionAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var retryClicked: (() -> Unit)? = null
    private var items = emptyList<ListItem>()

    fun onRetryClicked(callback: () -> Unit) {
        this.retryClicked = callback
    }

    fun submitList(items: List<ListItem>) {
        this.items = items
        notifyDataSetChanged()
    }

    fun getItem(position: Int): ListItem = items[position]

    override fun getItemCount(): Int = this.items.size

    override fun getItemViewType(position: Int): Int {
        return getItem(position).viewType
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return when (viewType) {
            LoadingState.VIEW_TYPE -> LoadingStateViewHolder(itemView, retryClicked)
            Question.VIEW_TYPE -> QuestionViewHolder(itemView)
            else -> throw IllegalArgumentException("Invalid view type : $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is LoadingStateViewHolder -> holder.bind(getItem(position) as LoadingState)
            is QuestionViewHolder -> holder.bind(getItem(position) as Question)
            else -> throw IllegalArgumentException("Invalid ViewHolder type : $holder")
        }
    }
}
