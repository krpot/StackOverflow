package com.warmpot.android.stackoverflow.domain.questions

import com.warmpot.android.stackoverflow.common.OneOf
import com.warmpot.android.stackoverflow.common.tryOneOf
import com.warmpot.android.stackoverflow.data.schema.answers.AnswersResponse
import com.warmpot.android.stackoverflow.data.schema.qustions.QuestionsResponse
import com.warmpot.android.stackoverflow.network.StackoverflowApi
import kotlinx.coroutines.*

class GetQuestionDetailsUseCase(
    private val stackOverflowApi: StackoverflowApi
) {

    suspend fun execute(questionId: Int): OneOf<Pair<QuestionsResponse, AnswersResponse>> = withContext(Dispatchers.IO) {
        val deferredGetQuestion: Deferred<OneOf<QuestionsResponse>> = async { getQuestionAsync(questionId) }
        val deferredGetAnswers: Deferred<OneOf<AnswersResponse>> = async { getAnswersAsync(questionId) }

        val oneOfGetQuestion: OneOf<QuestionsResponse> = deferredGetQuestion.await()
        val oneOfGetAnswers: OneOf<AnswersResponse> = deferredGetAnswers.await()

        val questionResponse: QuestionsResponse = when (oneOfGetQuestion) {
            is OneOf.Error -> return@withContext oneOfGetQuestion
            is OneOf.Success -> oneOfGetQuestion.data
        }

        val answerResponse: AnswersResponse = when (oneOfGetAnswers) {
            is OneOf.Error -> return@withContext oneOfGetAnswers
            is OneOf.Success -> oneOfGetAnswers.data
        }


        OneOf.Success(questionResponse to answerResponse)
    }

    private suspend fun getQuestionAsync(id: Int): OneOf<QuestionsResponse> = tryOneOf {
        stackOverflowApi.getQuestion(id)
    }

    private suspend fun getAnswersAsync(questionId: Int): OneOf<AnswersResponse> = tryOneOf {
        stackOverflowApi.getQuestionAnswers(questionId)
    }
}
