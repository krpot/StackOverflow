package com.warmpot.android.stackoverflow.common.di

import com.warmpot.android.stackoverflow.domain.questions.GetQuestionDetailsUseCase
import com.warmpot.android.stackoverflow.domain.questions.GetQuestionsUseCase
import com.warmpot.android.stackoverflow.domain.users.GetUserUseCase

internal object UseCaseModule {

    fun provideGetQuestionUseCase(): GetQuestionsUseCase {
        return GetQuestionsUseCase(
            dataSource = NetworkModule.questionDataSource
        )
    }

    fun provideGetQuestionDetailsUseCase(): GetQuestionDetailsUseCase {
        return GetQuestionDetailsUseCase(
            dataSource = NetworkModule.questionDataSource
        )
    }

    fun provideGetUserUseCase(): GetUserUseCase {
        return GetUserUseCase(
            dataSource = NetworkModule.userDataSource
        )
    }

}