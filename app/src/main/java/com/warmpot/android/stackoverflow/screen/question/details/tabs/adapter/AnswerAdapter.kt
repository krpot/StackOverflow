package com.warmpot.android.stackoverflow.screen.question.details.tabs.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.warmpot.android.stackoverflow.databinding.RowAnswerBinding
import com.warmpot.android.stackoverflow.screen.question.model.Answer
import com.warmpot.android.stackoverflow.utils.inflate
import com.warmpot.android.stackoverflow.utils.loadHtml

class AnswerAdapter : ListAdapter<Answer, AnswerViewHolder>(DIFF_CALLBACK) {

    override fun getItemViewType(position: Int): Int {
        return getItem(position).viewType
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnswerViewHolder {
        return AnswerViewHolder(parent.inflate(viewType))
    }

    override fun onBindViewHolder(holder: AnswerViewHolder, position: Int) {
        val item = getItem(position) ?: return
        holder.bind(item)
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Answer>() {
            override fun areItemsTheSame(oldItem: Answer, newItem: Answer): Boolean {
                return oldItem.answerId == newItem.answerId
            }

            override fun areContentsTheSame(oldItem: Answer, newItem: Answer): Boolean {
                return oldItem == newItem
            }
        }
    }
}


class AnswerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val binding = RowAnswerBinding.bind(itemView)

    fun bind(item: Answer) {
        binding.apply {
            profileView.bindUser(item.owner)
            webView.loadHtml(item.body)
        }
    }

}
