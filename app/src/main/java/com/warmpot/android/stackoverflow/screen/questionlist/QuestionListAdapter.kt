package com.warmpot.android.stackoverflow.screen.questionlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.warmpot.android.stackoverflow.R
import com.warmpot.android.stackoverflow.data.entity.QuestionApiEntity
import com.warmpot.android.stackoverflow.screen.utils.inflate

class QuestionListAdapter(
    list: List<QuestionApiEntity> = emptyList()
) : RecyclerView.Adapter<QuestionListViewHolder>() {

    private val items = ArrayList<QuestionApiEntity>(list)

    fun submitList(questions: List<QuestionApiEntity>) {
        items.clear()
        items.addAll(questions)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionListViewHolder {
        return QuestionListViewHolder(parent.inflate(R.layout.list_item_question))
    }

    override fun onBindViewHolder(holder: QuestionListViewHolder, position: Int) {
        val question = items[position]
        holder.bindQuestion(question)
    }
}