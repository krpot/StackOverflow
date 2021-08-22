package com.warmpot.android.stackoverflow.screen.question.list.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.warmpot.android.stackoverflow.common.agoText
import com.warmpot.android.stackoverflow.databinding.RowQuestionBinding
import com.warmpot.android.stackoverflow.screen.question.model.Question
import com.warmpot.android.stackoverflow.screen.user.model.User
import com.warmpot.android.stackoverflow.utils.toHtml

class QuestionViewHolder(
    itemView: View,
    private val itemClicked: ((Question) -> Unit)?,
    private val ownerClicked: ((User) -> Unit)?
) : RecyclerView.ViewHolder(itemView) {

    private val binding = RowQuestionBinding.bind(itemView)

    fun bind(item: Question) {
        binding.apply {
            upvoteCountTxt.text = item.upvoteCount.toString()
            answerCountTxt.text = item.answerCount.toString()
            titleTxt.text = item.title.toHtml()
            lastActivityDateTxt.text = item.lastActivityDate.agoText()
            ownerTxt.text = item.owner.displayName.toHtml()
            ownerTxt.setOnClickListener {
                ownerClicked?.invoke(item.owner)
            }

            bindTags(item.tags)
        }

        itemView.setOnClickListener {
            itemClicked?.invoke(item)
        }
    }

    private fun bindTags(tags: List<String>) {
        binding.tagGroup.apply {
            for (tag in tags) {
                val chip = Chip(context)
                chip.text = tag
                addView(chip)
            }
        }
    }
}

