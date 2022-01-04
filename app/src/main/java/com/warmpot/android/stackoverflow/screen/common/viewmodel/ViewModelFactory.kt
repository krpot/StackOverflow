package com.warmpot.android.stackoverflow.screen.common.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.warmpot.android.stackoverflow.domain.questions.GetQuestionDetailsUseCase
import com.warmpot.android.stackoverflow.domain.questions.GetQuestionsUseCase
import com.warmpot.android.stackoverflow.domain.users.GetUserUseCase
import com.warmpot.android.stackoverflow.common.di.NetworkModule
import com.warmpot.android.stackoverflow.screen.question.details.viewmodel.QuestionDetailsViewModel
import com.warmpot.android.stackoverflow.screen.question.list.viewmodel.QuestionListViewModel
import com.warmpot.android.stackoverflow.screen.user.viewmodel.UserViewModel

class ViewModelFactory(
    private val viewModelProvider: Map<Class<*>, ViewModel>
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        val vm: ViewModel = viewModelProvider[modelClass] ?: throw IllegalArgumentException("Cannot find ViewModel for $modelClass")
        return vm as T

//        return when (modelClass) {
//            QuestionListViewModel::class.java -> QuestionListViewModel(
//                getQuestionsUseCase = GetQuestionsUseCase(
//                    stackOverflowApi = NetworkModule.stackOverflowApi
//                )
//            )
//            QuestionDetailsViewModel::class.java -> QuestionDetailsViewModel(
//                getQuestionDetailsUseCase = GetQuestionDetailsUseCase(
//                    stackOverflowApi = NetworkModule.stackOverflowApi
//                )
//            )
//            UserViewModel::class.java -> UserViewModel(
//                getUserUseCase = GetUserUseCase(
//                    stackOverflowApi = NetworkModule.stackOverflowApi
//                )
//            )
//            else -> throw IllegalArgumentException("Cannot find ViewModel for $modelClass")
//        } as T
    }
}
