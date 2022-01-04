package com.warmpot.android.stackoverflow.domain.users

import com.warmpot.android.stackoverflow.common.OneOf
import com.warmpot.android.stackoverflow.common.map
import com.warmpot.android.stackoverflow.data.users.UserDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetUserUseCase(
    private val dataSource: UserDataSource
) {

    suspend fun getUser(userId: Int): OneOf<GetUserResult> =
        withContext(Dispatchers.IO) {
            dataSource.getUser(userId)
                .map { userResponse ->
                    GetUserResult(
                        hasMore = userResponse.hasMore,
                        data = userResponse.items.first(),
                        quotaMax = userResponse.quotaMax,
                        quotaRemaining = userResponse.quotaRemaining
                    )
                }
        }
}
