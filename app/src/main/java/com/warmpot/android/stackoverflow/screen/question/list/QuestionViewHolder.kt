package com.warmpot.android.stackoverflow.screen.question.list

import androidx.recyclerview.widget.RecyclerView
import com.warmpot.android.stackoverflow.R
import com.warmpot.android.stackoverflow.data.schema.QuestionSchema
import com.warmpot.android.stackoverflow.databinding.RowQuestionBinding
import com.warmpot.android.stackoverflow.utils.resStr
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class QuestionViewHolder(
    private val binding: RowQuestionBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: QuestionSchema) {
        binding.apply {
            titleTxt.text = item.title
            footerTxt.text =
                itemView.resStr(R.string.question_item_footer_fmt, item.upvoteCount, item.answerCount, item.viewCount)
            creationDateTxt.text = LocalDate.ofEpochDay(item.creationDate).format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))
        }
    }
}
