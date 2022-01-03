package com.warmpot.android.stackoverflow.screen.user.viewmodel

import androidx.lifecycle.LiveData
import com.warmpot.android.stackoverflow.common.OneOf
import com.warmpot.android.stackoverflow.common.onError
import com.warmpot.android.stackoverflow.common.onSuccess
import com.warmpot.android.stackoverflow.domain.users.GetUserResult
import com.warmpot.android.stackoverflow.domain.users.GetUserUseCase
import com.warmpot.android.stackoverflow.screen.common.viewmodel.BaseViewModel

class UserViewModel(
    private val getUserUseCase: GetUserUseCase,
    private val stateLiveData: UserUiStateLiveData = UserUiStateLiveData()
) : BaseViewModel() {

    val uiState: LiveData<UserUiState> get() = stateLiveData.uiState

    // region public functions
    fun fetchUser(userId: Int) {
        singleLaunch {
            val result = getUserUseCase.getUser(userId)
            handleGetUserResult(result)
        }
    }
    // endregion public functions

    private fun handleGetUserResult(result: OneOf<GetUserResult>) {
        result.onError { throwable ->
            stateLiveData.postError(throwable)
        }
        result.onSuccess { userResponse ->
            stateLiveData.postUser(userResponse.data)
        }
    }
}

