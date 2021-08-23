package com.warmpot.android.stackoverflow.domain.questions

import com.warmpot.android.stackoverflow.data.schema.qustions.QuestionSchema

sealed class QuestionsFetchResult {
    data class Failure(val e: Throwable) : QuestionsFetchResult()

    data class Empty(val data: List<QuestionSchema>) : QuestionsFetchResult()

    data class EndOfData(val data: List<QuestionSchema>) : QuestionsFetchResult()

    data class HasData(
        val data: List<QuestionSchema>
    ) : QuestionsFetchResult()
}
