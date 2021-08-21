package com.warmpot.android.stackoverflow.domain.usecase

import com.warmpot.android.stackoverflow.data.schema.QuestionSchema

sealed class QuestionFetchResult {
    data class Failure(val e: Throwable) : QuestionFetchResult()

    data class Empty(val data: List<QuestionSchema>) : QuestionFetchResult()

    data class EndOfData(val data: List<QuestionSchema>) : QuestionFetchResult()

    data class HasData(
        val data: List<QuestionSchema>
    ) : QuestionFetchResult()
}
