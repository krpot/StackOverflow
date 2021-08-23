package com.warmpot.android.stackoverflow.screen.question.model


import com.warmpot.android.stackoverflow.screen.user.model.User

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
    val score: Int
)
