package com.warmpot.android.stackoverflow.domain.questions

import com.warmpot.android.stackoverflow.common.OneOf
import com.warmpot.android.stackoverflow.common.tryCatchOf
import com.warmpot.android.stackoverflow.data.answers.AnswersResponse
import com.warmpot.android.stackoverflow.data.qustions.schema.QuestionsResponse
import com.warmpot.android.stackoverflow.domain.model.QuestionId
import com.warmpot.android.stackoverflow.network.StackoverflowApi
import kotlinx.coroutines.*

class GetQuestionDetailsUseCase(
    private val stackOverflowApi: StackoverflowApi
) {

    suspend fun execute(questionId: QuestionId): OneOf<Pair<QuestionsResponse, AnswersResponse>> =
        withContext(Dispatchers.IO) {
            val deferredGetQuestion: Deferred<OneOf<QuestionsResponse>> =
                async { getQuestionAsync(questionId.value) }
            val deferredGetAnswers: Deferred<OneOf<AnswersResponse>> =
                async { getAnswersAsync(questionId.value) }

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

    private suspend fun getQuestionAsync(id: Int): OneOf<QuestionsResponse> = tryCatchOf {
        stackOverflowApi.getQuestion(id)
    }

    private suspend fun getAnswersAsync(questionId: Int): OneOf<AnswersResponse> = tryCatchOf {
        stackOverflowApi.getQuestionAnswers(questionId)
    }
}
