package com.warmpot.android.stackoverflow.screen.common.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.warmpot.android.stackoverflow.domain.usecase.GetQuestionUseCase
import com.warmpot.android.stackoverflow.domain.usecase.GetQuestionsUseCase
import com.warmpot.android.stackoverflow.domain.usecase.GetUserUseCase
import com.warmpot.android.stackoverflow.network.NetworkModule
import com.warmpot.android.stackoverflow.screen.question.details.viewmodel.QuestionDetailsViewModel
import com.warmpot.android.stackoverflow.screen.question.list.viewmodel.QuestionListViewModel
import com.warmpot.android.stackoverflow.screen.user.viewmodel.UserViewModel

object ViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when (modelClass) {
            QuestionListViewModel::class.java -> QuestionListViewModel(
                getQuestionsUseCase = GetQuestionsUseCase(
                    stackOverflowApi = NetworkModule.stackOverflowApi
                )
            )
            QuestionDetailsViewModel::class.java -> QuestionDetailsViewModel(
                getQuestionUseCase = GetQuestionUseCase(
                    stackOverflowApi = NetworkModule.stackOverflowApi
                )
            )
            UserViewModel::class.java -> UserViewModel(
                getUserUseCase = GetUserUseCase(
                    stackOverflowApi = NetworkModule.stackOverflowApi
                )
            )
            else -> throw IllegalArgumentException("Cannot find ViewModel for $modelClass")
        } as T
    }
}
