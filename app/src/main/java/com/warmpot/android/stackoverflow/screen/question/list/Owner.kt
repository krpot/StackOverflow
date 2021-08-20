package com.warmpot.android.stackoverflow.screen.question.list


import com.google.gson.annotations.SerializedName

data class Owner(
    val userId: Int,
    val acceptRate: Int,
    val accountId: Int,
    val displayName: String,
    val link: String,
    val profileImage: String,
    val reputation: Int,
    val userType: String
)
