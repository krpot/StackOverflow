package com.warmpot.android.stackoverflow.domain.users

import com.warmpot.android.stackoverflow.common.OneOf
import com.warmpot.android.stackoverflow.common.map
import com.warmpot.android.stackoverflow.common.tryOneOf
import com.warmpot.android.stackoverflow.data.schema.users.UserResponse
import com.warmpot.android.stackoverflow.network.StackoverflowApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetUserUseCase(
    private val stackOverflowApi: StackoverflowApi
) {

    suspend fun getUser(userId: Int): OneOf<GetUserResult> =
        withContext(Dispatchers.IO) {
            getUserBy(userId)
                .map { userResponse ->
                    GetUserResult(
                        hasMore = userResponse.hasMore,
                        data = userResponse.items.first(),
                        quotaMax = userResponse.quotaMax,
                        quotaRemaining = userResponse.quotaRemaining
                    )
                }
        }

    private suspend fun getUserBy(userId: Int): OneOf<UserResponse> =
        tryOneOf {
            stackOverflowApi.getUser(userId)
        }
}
