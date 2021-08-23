package com.warmpot.android.stackoverflow.data.schema.answers


import com.google.gson.annotations.SerializedName
import com.warmpot.android.stackoverflow.data.schema.users.UserSchema

data class AnswerSchema(
    @SerializedName("answer_id")
    val answerId: Int,

    @SerializedName("body")
    val body: String?,

    @SerializedName("content_license")
    val contentLicense: String?,

    @SerializedName("creation_date")
    val creationDate: Long,

    @SerializedName("is_accepted")
    val isAccepted: Boolean?,

    @SerializedName("last_activity_date")
    val lastActivityDate: Long,

    @SerializedName("last_edit_date")
    val lastEditDate: Long,

    @SerializedName("owner")
    val owner: UserSchema,

    @SerializedName("question_id")
    val questionId: Int,

    @SerializedName("score")
    val score: Int
)
