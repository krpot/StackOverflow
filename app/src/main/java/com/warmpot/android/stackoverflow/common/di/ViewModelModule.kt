package com.warmpot.android.stackoverflow.common.di

import androidx.lifecycle.ViewModel
import com.warmpot.android.stackoverflow.screen.common.viewmodel.ViewModelFactory
import com.warmpot.android.stackoverflow.screen.question.details.viewmodel.QuestionDetailsViewModel
import com.warmpot.android.stackoverflow.screen.question.list.viewmodel.QuestionListViewModel
import com.warmpot.android.stackoverflow.screen.user.viewmodel.UserViewModel

internal object ViewModelModule {

    val viewModelFactory: ViewModelFactory by lazy { ViewModelFactory(viewModelMap) }

    private val viewModelMap = hashMapOf<Class<*>, ViewModel>(
        QuestionListViewModel::class.java to provideQuestionListViewModel(),
        QuestionDetailsViewModel::class.java to provideQuestionDetailsViewModel(),
        UserViewModel::class.java to provideUserViewModel()
    )

    private fun provideQuestionListViewModel(): QuestionListViewModel {
        return QuestionListViewModel(
            getQuestionsUseCase = UseCaseModule.provideGetQuestionUseCase()
        )
    }

    private fun provideQuestionDetailsViewModel(): QuestionDetailsViewModel {
        return QuestionDetailsViewModel(
            getQuestionDetailsUseCase = UseCaseModule.provideGetQuestionDetailsUseCase()
        )
    }

    private fun provideUserViewModel(): UserViewModel {
        return UserViewModel(
            getUserUseCase = UseCaseModule.provideGetUserUseCase()
        )
    }

}