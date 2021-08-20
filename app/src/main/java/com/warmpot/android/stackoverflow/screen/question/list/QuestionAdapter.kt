package com.warmpot.android.stackoverflow.screen.question.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.recyclerview.widget.RecyclerView
import com.warmpot.android.stackoverflow.R
import com.warmpot.android.stackoverflow.data.schema.QuestionSchema
import com.warmpot.android.stackoverflow.databinding.RowQuestionBinding
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class QuestionAdapter : RecyclerView.Adapter<QuestionViewHolder>() {

    private var items = emptyList<QuestionSchema>()
    fun submitList(items: List<QuestionSchema>) {
        this.items = items
        notifyDataSetChanged()
    }

    fun getItem(position: Int): QuestionSchema = items[position]

    override fun getItemCount(): Int = this.items.size

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionViewHolder {
        return QuestionViewHolder(RowQuestionBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: QuestionViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}
