package com.warmpot.android.stackoverflow.screen.user.viewmodel

import com.warmpot.android.stackoverflow.common.EventValue
import com.warmpot.android.stackoverflow.screen.common.resource.Str
import com.warmpot.android.stackoverflow.screen.user.model.User

data class UserUiState(
    val loading: EventValue<Boolean> = EventValue(false),
    val error: EventValue<Str>? = null,
    val user: User? = null,
)

