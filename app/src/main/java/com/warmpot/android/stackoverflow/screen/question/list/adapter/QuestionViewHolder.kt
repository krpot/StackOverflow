package com.warmpot.android.stackoverflow.screen.question.list.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.warmpot.android.stackoverflow.common.agoText
import com.warmpot.android.stackoverflow.databinding.RowQuestionBinding
import com.warmpot.android.stackoverflow.screen.question.model.Question
import com.warmpot.android.stackoverflow.utils.toHtml

class QuestionViewHolder(
    itemView: View,
    private val itemClicked: ((Question) -> Unit)?
) : RecyclerView.ViewHolder(itemView) {

    private val binding = RowQuestionBinding.bind(itemView)

    fun bind(item: Question) {
        binding.apply {
            upvoteCountTxt.text = item.upvoteCount.toString()
            answerCountTxt.text = item.answerCount.toString()
            titleTxt.text = item.title.toHtml()
            tagTxt.text = item.tags.joinToString(" ᐧ ")
            lastActivityDateTxt.text = item.lastActivityDate.agoText()
            ownerTxt.text = item.owner?.displayName.toHtml()
        }

        itemView.setOnClickListener {
            itemClicked?.invoke(item)
        }
    }
}

