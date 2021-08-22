package com.warmpot.android.stackoverflow.screen.question.list.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.warmpot.android.stackoverflow.screen.common.adapter.ListItem
import com.warmpot.android.stackoverflow.screen.question.model.Question
import com.warmpot.android.stackoverflow.screen.user.model.User
import com.warmpot.android.stackoverflow.utils.inflate

class QuestionAdapter : ListAdapter<Question, QuestionViewHolder>(DIFF_CALLBACK) {

    private var itemClicked: ((Question) -> Unit)? = null
    private var userClicked: ((User) -> Unit)? = null
    private var items = emptyList<ListItem>()

    fun onItemClicked(callback: (Question) -> Unit) {
        this.itemClicked = callback
    }

    fun onUserClicked(callback: (User) -> Unit) {
        this.userClicked = callback
    }

    override fun getItemCount(): Int = this.items.size

    override fun getItemViewType(position: Int): Int {
        return getItem(position).viewType
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionViewHolder {
        val itemView = parent.inflate(viewType)
        return QuestionViewHolder(itemView, itemClicked, userClicked)
    }

    override fun onBindViewHolder(holder: QuestionViewHolder, position: Int) {
        val item = getItem(position) ?: return
        holder.bind(item)
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Question>() {
            override fun areItemsTheSame(oldItem: Question, newItem: Question): Boolean {
                return oldItem.questionId == newItem.questionId
            }

            override fun areContentsTheSame(oldItem: Question, newItem: Question): Boolean {
                return oldItem == newItem
            }
        }
    }
}
