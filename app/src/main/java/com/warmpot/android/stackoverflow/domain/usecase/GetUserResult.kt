package com.warmpot.android.stackoverflow.domain.usecase

import com.warmpot.android.stackoverflow.data.schema.UserSchema

class GetUserResult(
    override val hasMore: Boolean,
    override val data: UserSchema,
    override val quotaMax: Int,
    override val quotaRemaining: Int
) : UseCaseResult<UserSchema>()
