package com.warmpot.android.stackoverflow.screen.question.model


import com.warmpot.android.stackoverflow.R
import com.warmpot.android.stackoverflow.screen.common.adapter.ListItem
import com.warmpot.android.stackoverflow.screen.user.model.User
import java.io.Serializable

data class Answer(
    val answerId: Int,
    val body: String,
    val contentLicense: String,
    val isAccepted: Boolean,
    val creationDate: Long,
    val lastActivityDate: Long,
    val lastEditDate: Long,
    val owner: User,
    val questionId: Int,
    val score: Int,
    override val viewType: Int = VIEW_TYPE
) : ListItem, Serializable {
    companion object {
        const val VIEW_TYPE = R.layout.row_answer
    }
}
