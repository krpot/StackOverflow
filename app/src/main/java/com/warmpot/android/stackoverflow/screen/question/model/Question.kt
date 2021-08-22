package com.warmpot.android.stackoverflow.screen.question.model

import com.warmpot.android.stackoverflow.R
import com.warmpot.android.stackoverflow.common.EpochSecond
import com.warmpot.android.stackoverflow.screen.common.adapter.ListItem
import com.warmpot.android.stackoverflow.screen.user.model.User
import java.io.Serializable


data class Question(
    val questionId: Int = 0,
    val title: String = "",
    val body: String = "",
    val creationDate: EpochSecond,
    val lastActivityDate: EpochSecond,
    val lastEditDate: EpochSecond,
    val link: String = "",
    val owner: User,
    val answerCount: Int = 0,
    val commentCount: Int = 0,
    val score: Int = 0,
    val upvoteCount: Int = 0,
    val viewCount: Int = 0,
    val tags: List<String> = emptyList(),
    override val viewType: Int = VIEW_TYPE
) : ListItem, Serializable {
    companion object {
        const val VIEW_TYPE = R.layout.row_question
    }
}
