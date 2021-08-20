package com.warmpot.android.stackoverflow.screen.question.list

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.warmpot.android.stackoverflow.R
import com.warmpot.android.stackoverflow.databinding.RowQuestionBinding
import com.warmpot.android.stackoverflow.utils.resStr
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class QuestionViewHolder(
    itemView: View
) : RecyclerView.ViewHolder(itemView) {

    private val binding = RowQuestionBinding.bind(itemView)

    fun bind(item: Question) {
        binding.apply {
            titleTxt.text = item.title
            footerTxt.text =
                itemView.resStr(R.string.question_item_footer_fmt, item.upvoteCount, item.answerCount, item.viewCount)
            creationDateTxt.text = LocalDate.ofEpochDay(item.creationDate).format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))
        }
    }
}
