package com.warmpot.android.stackoverflow.screen.question.list

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.warmpot.android.stackoverflow.R
import com.warmpot.android.stackoverflow.common.format
import com.warmpot.android.stackoverflow.databinding.RowQuestionBinding
import com.warmpot.android.stackoverflow.utils.resStr
import java.text.SimpleDateFormat
import java.time.*
import java.time.format.DateTimeFormatter
import java.util.*

class QuestionViewHolder(
    itemView: View
) : RecyclerView.ViewHolder(itemView) {

    private val binding = RowQuestionBinding.bind(itemView)

    fun bind(item: Question) {
        binding.apply {
            titleTxt.text = item.title
            footerTxt.text =
                itemView.resStr(R.string.question_item_footer_fmt, item.upvoteCount, item.answerCount, item.viewCount)
            creationDateTxt.text = item.lastActivityDate.format()
        }
    }

    private fun formatDate(time: Long): String {
        val instant = Instant.ofEpochSecond(time)
        val ofInstant = ZonedDateTime.ofInstant(instant, ZoneOffset.UTC)
        return ofInstant.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))
    }
}

