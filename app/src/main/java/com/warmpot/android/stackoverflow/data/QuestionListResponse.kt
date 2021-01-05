package com.warmpot.android.stackoverflow.data


import com.google.gson.annotations.SerializedName

data class QuestionListResponse(
    @SerializedName("has_more")
    val hasMore: Boolean,

    @SerializedName("items")
    val questions: List<QuestionApiEntity>,

    @SerializedName("quota_max")
    val quotaMax: Int,

    @SerializedName("quota_remaining")
    val quotaRemaining: Int
)