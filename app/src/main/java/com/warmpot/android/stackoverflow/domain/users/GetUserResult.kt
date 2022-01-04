package com.warmpot.android.stackoverflow.domain.users

import com.warmpot.android.stackoverflow.data.users.UserSchema
import com.warmpot.android.stackoverflow.domain.usecase.UseCaseResult

class GetUserResult(
    override val hasMore: Boolean,
    override val data: UserSchema,
    override val quotaMax: Int,
    override val quotaRemaining: Int
) : UseCaseResult<UserSchema>()
