package com.warmpot.android.stackoverflow.data.qustions.datasource

import com.warmpot.android.stackoverflow.common.OneOf
import com.warmpot.android.stackoverflow.common.tryCatchOf
import com.warmpot.android.stackoverflow.data.qustions.schema.QuestionsResponse
import com.warmpot.android.stackoverflow.network.StackoverflowApi

class QuestionRemoteDataSource(
    private val api: StackoverflowApi
) : QuestionDataSource {

    override suspend fun getQuestions(page: Int): OneOf<QuestionsResponse> {
        return tryCatchOf {
            api.getQuestions(page)
        }
    }
}