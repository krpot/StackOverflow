package com.warmpot.android.stackoverflow.data.qustions.datasource

import com.warmpot.android.stackoverflow.common.OneOf
import com.warmpot.android.stackoverflow.data.answers.AnswersResponse
import com.warmpot.android.stackoverflow.data.qustions.schema.QuestionsResponse
import com.warmpot.android.stackoverflow.data.qustions.schema.SearchParam

interface QuestionDataSource {
    suspend fun getQuestions(page: Int): OneOf<QuestionsResponse>

    suspend fun searchQuestions(param: SearchParam): OneOf<QuestionsResponse>

    suspend fun getQuestion(id: Int): OneOf<QuestionsResponse>

    suspend fun getQuestionAnswers(id: Int): OneOf<AnswersResponse>
}