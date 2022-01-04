package com.warmpot.android.stackoverflow.screen.user.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.warmpot.android.stackoverflow.R
import com.warmpot.android.stackoverflow.common.EventValue
import com.warmpot.android.stackoverflow.data.users.UserSchema
import com.warmpot.android.stackoverflow.screen.common.resource.Str
import com.warmpot.android.stackoverflow.screen.user.mapper.UserMapper
import com.warmpot.android.stackoverflow.screen.user.model.User
import retrofit2.HttpException
import java.net.UnknownHostException

class UserUiStateLiveData(
    private val liveData: MutableLiveData<UserUiState> = MutableLiveData()
) {
    val uiState: LiveData<UserUiState> get() = liveData

    private val userMapper by lazy { UserMapper() }

    fun postLoading(isLoading: Boolean = true) {
        emitState(isLoading = isLoading)
    }

    fun postError(error: Str?) {
        emitState(error = error)
    }

    fun postError(th: Throwable) {
        emitState(error = th.toStr())
    }
    // endregion post functions

    // region private helper functions
    fun postUser(schema: UserSchema) {
        val user = userMapper.convert(schema)
        liveData.postValue(UserUiState(user = user))
        emitState(user = user)
    }

    private fun emitState(
        isLoading: Boolean = false,
        error: Str? = null,
        user: User? = liveData.value?.user,
    ) {
        val state = uiState.value ?: UserUiState()
        liveData.postValue(
            state.copy(
                loading = EventValue(isLoading),
                error = error?.let { EventValue(it) },
                user = user
            )
        )
    }
}

private fun Throwable.toStr(): Str {
    return when (this) {
        is UnknownHostException -> Str.from(R.string.error_no_connectivity)
        is HttpException -> Str.from("${message()} (${code()})")
        else -> Str.from(R.string.error_network_unavailable)
    }
}