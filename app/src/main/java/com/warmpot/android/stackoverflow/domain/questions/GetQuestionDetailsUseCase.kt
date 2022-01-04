package com.warmpot.android.stackoverflow.domain.questions

import com.warmpot.android.stackoverflow.common.OneOf
import com.warmpot.android.stackoverflow.data.answers.AnswersResponse
import com.warmpot.android.stackoverflow.data.qustions.datasource.QuestionDataSource
import com.warmpot.android.stackoverflow.data.qustions.schema.QuestionsResponse
import com.warmpot.android.stackoverflow.domain.model.QuestionId
import kotlinx.coroutines.*

class GetQuestionDetailsUseCase(
    private val dataSource: QuestionDataSource
) {

    suspend fun execute(questionId: QuestionId): OneOf<Pair<QuestionsResponse, AnswersResponse>> =
        withContext(Dispatchers.IO) {
            val deferredGetQuestion: Deferred<OneOf<QuestionsResponse>> =
                async { dataSource.getQuestion(questionId.value) }
            val deferredGetAnswers: Deferred<OneOf<AnswersResponse>> =
                async { dataSource.getQuestionAnswers(questionId.value) }

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
}
