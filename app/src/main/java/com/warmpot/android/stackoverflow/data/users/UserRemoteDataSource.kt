package com.warmpot.android.stackoverflow.data.users

import com.warmpot.android.stackoverflow.common.OneOf
import com.warmpot.android.stackoverflow.common.tryCatchOf
import com.warmpot.android.stackoverflow.network.StackoverflowApi

class UserRemoteDataSource(
    private val api: StackoverflowApi
) : UserDataSource {

    override suspend fun getUser(id: Int): OneOf<UserResponse> {
        return tryCatchOf {
            api.getUser(id)
        }
    }
}