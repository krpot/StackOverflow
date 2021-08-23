package com.warmpot.android.stackoverflow.data.schema.users


import com.google.gson.annotations.SerializedName

data class BadgeCountsSchema(
    @SerializedName("bronze")
    val bronze: Int,

    @SerializedName("gold")
    val gold: Int,

    @SerializedName("silver")
    val silver: Int
)
