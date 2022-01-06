package com.warmpot.android.stackoverflow.common.di

import com.warmpot.android.stackoverflow.data.qustions.cache.QuestionsResponseCache
import com.warmpot.android.stackoverflow.data.tags.cache.TagsResponseCache
import com.warmpot.android.stackoverflow.domain.questions.GetQuestionDetailsUseCase
import com.warmpot.android.stackoverflow.domain.questions.QuestionPagingUseCase
import com.warmpot.android.stackoverflow.domain.questions.QuestionSearchUseCase
import com.warmpot.android.stackoverflow.domain.tags.GetTagsUseCase
import com.warmpot.android.stackoverflow.domain.users.GetUserUseCase

internal object UseCaseModule {

    fun provideQuestionPagingUseCase(): QuestionPagingUseCase {
        return QuestionPagingUseCase(
            dataSource = NetworkModule.questionDataSource,
            cache = provideQuestionsResponseCache()
        )
    }

    fun provideQuestionSearchUseCase(): QuestionSearchUseCase {
        return QuestionSearchUseCase(
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

    private fun provideQuestionsResponseCache(): QuestionsResponseCache {
        return QuestionsResponseCache()
    }

    private val tagResponseCache by lazy { TagsResponseCache() }

    fun provideGetTagsUseCase(): GetTagsUseCase {
        return GetTagsUseCase(
            dataSource = NetworkModule.tagDataSource,
            cache = tagResponseCache
        )
    }
}