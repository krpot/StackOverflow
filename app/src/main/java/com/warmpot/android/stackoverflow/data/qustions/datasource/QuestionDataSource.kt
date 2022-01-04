package com.warmpot.android.stackoverflow.data.qustions.datasource

import com.warmpot.android.stackoverflow.common.OneOf
import com.warmpot.android.stackoverflow.data.qustions.schema.QuestionsResponse

interface QuestionDataSource {
    suspend fun getQuestions(page: Int): OneOf<QuestionsResponse>
}