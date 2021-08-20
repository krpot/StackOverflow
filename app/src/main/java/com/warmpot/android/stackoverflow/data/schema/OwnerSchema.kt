package com.warmpot.android.stackoverflow.data.schema


import com.google.gson.annotations.SerializedName

data class OwnerSchema(
    @SerializedName("user_id")
    val userId: Int,

    @SerializedName("accept_rate")
    val acceptRate: Int? = null,

    @SerializedName("account_id")
    val accountId: Int,

    @SerializedName("display_name")
    val displayName: String? = null,

    @SerializedName("link")
    val link: String? = null,

    @SerializedName("profile_image")
    val profileImage: String? = null,

    @SerializedName("reputation")
    val reputation: Int? = null,

    @SerializedName("user_type")
    val userType: String
)
