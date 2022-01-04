package com.warmpot.android.stackoverflow.common.di

import com.warmpot.android.stackoverflow.domain.questions.GetQuestionDetailsUseCase
import com.warmpot.android.stackoverflow.domain.questions.GetQuestionsUseCase
import com.warmpot.android.stackoverflow.domain.users.GetUserUseCase
import com.warmpot.android.stackoverflow.network.StackoverflowApi

internal object UseCaseModule {

    private val stackOverflowApi: StackoverflowApi by lazy { NetworkModule.stackOverflowApi }

    fun provideGetQuestionUseCase(): GetQuestionsUseCase {
        return GetQuestionsUseCase(
            stackOverflowApi = stackOverflowApi
        )
    }

    fun provideGetQuestionDetailsUseCase(): GetQuestionDetailsUseCase {
        return GetQuestionDetailsUseCase(
            stackOverflowApi = stackOverflowApi
        )
    }

    fun provideGetUserUseCase(): GetUserUseCase {
        return GetUserUseCase(
            stackOverflowApi = stackOverflowApi
        )
    }

}