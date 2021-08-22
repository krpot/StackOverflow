package com.warmpot.android.stackoverflow.screen.user.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.warmpot.android.stackoverflow.R
import com.warmpot.android.stackoverflow.common.OneOf
import com.warmpot.android.stackoverflow.common.onError
import com.warmpot.android.stackoverflow.common.onSuccess
import com.warmpot.android.stackoverflow.data.schema.UserSchema
import com.warmpot.android.stackoverflow.domain.usecase.GetUserResult
import com.warmpot.android.stackoverflow.domain.usecase.GetUserUseCase
import com.warmpot.android.stackoverflow.screen.common.isActuallyActive
import com.warmpot.android.stackoverflow.screen.common.resource.Str
import com.warmpot.android.stackoverflow.screen.user.mapper.UserMapper
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.net.UnknownHostException

class UserViewModel(
    private val getUserUseCase: GetUserUseCase
) : ViewModel() {

    private val viewStateLiveData = MutableLiveData<UserViewState>()
    val viewState: LiveData<UserViewState> get() = viewStateLiveData

    private val userMapper by lazy { UserMapper() }

    private var job: Job? = null

    // region public functions
    fun fetchUser(userId: Int) {
        throttleApiCall {
            val result = getUserUseCase.getUser(userId)
            handleGetUserResult(result)
        }
    }
    // endregion public functions

    private fun handleGetUserResult(result: OneOf<GetUserResult>) {
        result.onError { throwable ->
            postError(throwable)
        }
        result.onSuccess { userResponse ->
            mapAndPostUser(userResponse.data)
        }
    }

    // region post functions
    private fun mapAndPostUser(schema: UserSchema) {
        val user = userMapper.convert(schema)
        viewStateLiveData.postValue(UserViewState(user = user))
    }

    private fun postError(th: Throwable) {
        val str = throwableToStr(th)
        viewStateLiveData.postValue(UserViewState(error = str))
    }
    // endregion post functions

    // region private helper functions
    private fun throttleApiCall(apiCall: suspend () -> Unit) {
        if (job.isActuallyActive()) return
        job = viewModelScope.launch {
            apiCall()
        }
    }

    private fun throwableToStr(th: Throwable): Str {
        return when (th) {
            is UnknownHostException -> Str.from(R.string.error_no_connectivity)
            is HttpException -> Str.from("${th.message()} (${th.code()})")
            else -> Str.from(R.string.error_network_unavailable)
        }
    }
// endregion private helper functions
}
