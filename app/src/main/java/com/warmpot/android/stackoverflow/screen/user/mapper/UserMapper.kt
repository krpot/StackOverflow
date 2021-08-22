package com.warmpot.android.stackoverflow.screen.user.mapper

import com.warmpot.android.stackoverflow.common.Mapper
import com.warmpot.android.stackoverflow.data.schema.UserSchema
import com.warmpot.android.stackoverflow.screen.user.model.User

class UserMapper : Mapper<UserSchema, User> {

    private val badgeCountsMapper by lazy { BadgeCountsMapper() }

    override fun convert(src: UserSchema): User {
        return src.run {
            User(
                aboutMe = aboutMe ?: "",
                userId = userId,
                accountId = accountId,
                displayName = displayName ?: "",
                link = link ?: "",
                location = location ?: "",
                websiteUrl = websiteUrl ?: "",
                profileImage = profileImage ?: "",
                userType = userType,
                answerCount = answerCount ?: 0,
                downVoteCount = downVoteCount ?: 0,
                questionCount = questionCount,
                reputation = reputation ?: 0,
                upVoteCount = upVoteCount ?: 0,
                viewCount = viewCount ?: 0,
                badgeCounts = badgeCounts?.let { badgeCountsMapper.convert(it) }
            )
        }
    }
}
