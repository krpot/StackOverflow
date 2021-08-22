package com.warmpot.android.stackoverflow.screen.user.model

import java.io.Serializable

data class User(
    val aboutMe: String,
    val userId: Int,
    val accountId: Int,
    val displayName: String,
    val location: String,
    val websiteUrl: String,
    val link: String,
    val profileImage: String,
    val userType: String,
    val answerCount: Int,
    val downVoteCount: Int,
    val questionCount: Int,
    val reputation: Int,
    val upVoteCount: Int,
    val viewCount: Int,
    val badgeCounts: BadgeCounts? = null
) : Serializable
