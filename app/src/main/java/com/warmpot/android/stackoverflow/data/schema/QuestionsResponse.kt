package com.warmpot.android.stackoverflow.data.schema


import com.google.gson.annotations.SerializedName

data class QuestionsResponse(
    @SerializedName("backoff")
    val backoff: Int,

    @SerializedName("has_more")
    val hasMore: Boolean,

    @SerializedName("items")
    val items: List<QuestionSchema>,

    @SerializedName("quota_max")
    val quotaMax: Int,

    @SerializedName("quota_remaining")
    val quotaRemaining: Int
)
