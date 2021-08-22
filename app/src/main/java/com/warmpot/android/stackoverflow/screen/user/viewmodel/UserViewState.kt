package com.warmpot.android.stackoverflow.screen.user.viewmodel

import com.warmpot.android.stackoverflow.screen.common.resource.Str
import com.warmpot.android.stackoverflow.screen.user.model.User

class UserViewState(
    val isLoading: Boolean = false,
    val error: Str? = null,
    val user: User? = null,
)

