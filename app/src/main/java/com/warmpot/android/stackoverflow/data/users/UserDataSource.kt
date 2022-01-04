package com.warmpot.android.stackoverflow.data.users

import com.warmpot.android.stackoverflow.common.OneOf

interface UserDataSource {
    suspend fun getUser(id: Int): OneOf<UserResponse>
}