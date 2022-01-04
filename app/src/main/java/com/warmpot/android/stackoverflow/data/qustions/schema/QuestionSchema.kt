package com.warmpot.android.stackoverflow.data.qustions.schema


import com.google.gson.annotations.SerializedName
import com.warmpot.android.stackoverflow.data.users.UserSchema

data class QuestionSchema(
    @SerializedName("question_id")
    val questionId: Int = 0,

    @SerializedName("title")
    val title: String = "",

    @SerializedName("body")
    val body: String? = null,

    @SerializedName("creation_date")
    val creationDate: Long = 0L,

    @SerializedName("last_activity_date")
    val lastActivityDate: Long = 0L,

    @SerializedName("last_edit_date")
    val lastEditDate: Long = 0L,

    @SerializedName("link")
    val link: String = "",

    @SerializedName("owner")
    val owner: UserSchema,

    @SerializedName("answer_count")
    val answerCount: Int = 0,

    @SerializedName("comment_count")
    val commentCount: Int = 0,

    @SerializedName("score")
    val score: Int = 0,

    @SerializedName("up_vote_count")
    val upvoteCount: Int = 0,

    @SerializedName("view_count")
    val viewCount: Int = 0,

    @SerializedName("tags")
    val tags: List<String> = emptyList()
)
