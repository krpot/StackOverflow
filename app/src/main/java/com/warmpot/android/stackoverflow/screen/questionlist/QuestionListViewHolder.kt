package com.warmpot.android.stackoverflow.screen.questionlist

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.warmpot.android.stackoverflow.R
import com.warmpot.android.stackoverflow.data.entity.QuestionApiEntity
import com.warmpot.android.stackoverflow.screen.utils.findViewById
import com.warmpot.android.stackoverflow.screen.utils.nonSyncLazy
import com.warmpot.android.stackoverflow.screen.utils.toHtml

class QuestionListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val titleTxt: TextView by nonSyncLazy { findViewById(R.id.titleTxt) }

    fun bindQuestion(question: QuestionApiEntity) {
        titleTxt.text = question.title.toHtml()
    }

}
