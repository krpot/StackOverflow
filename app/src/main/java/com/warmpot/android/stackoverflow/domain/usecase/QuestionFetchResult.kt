package com.warmpot.android.stackoverflow.domain.usecase

import com.warmpot.android.stackoverflow.data.schema.QuestionSchema

sealed class QuestionFetchResult {
    data class Failure(val e: Throwable) : QuestionFetchResult()

    object Empty : QuestionFetchResult()

    data class HasData(val data: QuestionSchema) : QuestionFetchResult()
}
