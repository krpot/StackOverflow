package com.warmpot.android.stackoverflow.data.schema.users


import com.google.gson.annotations.SerializedName

data class UserSchema(

    @SerializedName("user_id")
    val userId: Int,

    @SerializedName("about_me")
    val aboutMe: String? = null,

    @SerializedName("account_id")
    val accountId: Int,

    @SerializedName("answer_count")
    val answerCount: Int?? = null,

    @SerializedName("badge_counts")
    val badgeCounts: BadgeCountsSchema? = null,

    @SerializedName("creation_date")
    val creationDate: Int,

    @SerializedName("display_name")
    val displayName: String,

    @SerializedName("down_vote_count")
    val downVoteCount: Int?,

    @SerializedName("is_employee")
    val isEmployee: Boolean,

    @SerializedName("last_access_date")
    val lastAccessDate: Int,

    @SerializedName("last_modified_date")
    val lastModifiedDate: Int,

    @SerializedName("link")
    val link: String,

    @SerializedName("location")
    val location: String? = null,

    @SerializedName("profile_image")
    val profileImage: String,

    @SerializedName("question_count")
    val questionCount: Int,

    @SerializedName("reputation")
    val reputation: Int?,

    @SerializedName("reputation_change_day")
    val reputationChangeDay: Int,

    @SerializedName("reputation_change_month")
    val reputationChangeMonth: Int,

    @SerializedName("reputation_change_quarter")
    val reputationChangeQuarter: Int,

    @SerializedName("reputation_change_week")
    val reputationChangeWeek: Int,

    @SerializedName("reputation_change_year")
    val reputationChangeYear: Int,

    @SerializedName("up_vote_count")
    val upVoteCount: Int,

    @SerializedName("user_type")
    val userType: String,

    @SerializedName("view_count")
    val viewCount: Int,

    @SerializedName("website_url")
    val websiteUrl: String? = null
)
