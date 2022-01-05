package com.warmpot.android.stackoverflow.data.qustions.datasource

import com.warmpot.android.stackoverflow.common.OneOf
import com.warmpot.android.stackoverflow.common.tryCatchOf
import com.warmpot.android.stackoverflow.data.answers.AnswersResponse
import com.warmpot.android.stackoverflow.data.qustions.schema.QuestionsResponse
import com.warmpot.android.stackoverflow.data.qustions.schema.SearchParam
import com.warmpot.android.stackoverflow.network.StackoverflowApi

class QuestionRemoteDataSource(
    private val api: StackoverflowApi
) : QuestionDataSource {

    override suspend fun getQuestions(page: Int): OneOf<QuestionsResponse> {
        return tryCatchOf {
            api.getQuestions(page)
        }
    }

    override suspend fun searchQuestions(param: SearchParam): OneOf<QuestionsResponse> {
        return tryCatchOf {
            api.searchQuestions(param.toQueryMap())
        }
    }

    override suspend fun getQuestion(id: Int): OneOf<QuestionsResponse> {
        return tryCatchOf {
            api.getQuestion(id)
        }
    }

    override suspend fun getQuestionAnswers(id: Int): OneOf<AnswersResponse> {
        return tryCatchOf {
            api.getQuestionAnswers(id)
        }
    }
}