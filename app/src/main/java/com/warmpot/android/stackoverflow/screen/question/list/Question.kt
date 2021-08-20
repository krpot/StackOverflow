package com.warmpot.android.stackoverflow.screen.question.list

import com.warmpot.android.stackoverflow.R
import com.warmpot.android.stackoverflow.screen.common.adapter.ListItem


data class Question(
    val questionId: Int = 0,
    val title: String = "",
    val creationDate: Long = 0L,
    val lastActivityDate: Long = 0L,
    val lastEditDate: Long = 0L,
    val link: String = "",
    val owner: Owner? = null,
    val answerCount: Int = 0,
    val score: Int = 0,
    val upvoteCount: Int = 0,
    val viewCount: Int = 0,
    override val viewType: Int = VIEW_TYPE
): ListItem {
    companion object {
        const val VIEW_TYPE = R.layout.row_question
    }
}
