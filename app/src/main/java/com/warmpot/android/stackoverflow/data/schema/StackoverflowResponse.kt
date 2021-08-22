package com.warmpot.android.stackoverflow.data.schema


import com.google.gson.annotations.SerializedName

abstract class StackoverflowResponse<T>(
    @SerializedName("has_more")
    val hasMore: Boolean = false,

    @SerializedName("items")
    val items: List<T> = emptyList(),

    @SerializedName("quota_max")
    val quotaMax: Int = 0,

    @SerializedName("quota_remaining")
    val quotaRemaining: Int = 0
)
