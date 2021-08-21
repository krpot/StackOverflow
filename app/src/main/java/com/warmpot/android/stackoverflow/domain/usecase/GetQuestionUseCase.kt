package com.warmpot.android.stackoverflow.domain.usecase

import com.warmpot.android.stackoverflow.common.OneOf
import com.warmpot.android.stackoverflow.common.tryOneOf
import com.warmpot.android.stackoverflow.data.schema.QuestionSchema
import com.warmpot.android.stackoverflow.data.schema.QuestionsResponse
import com.warmpot.android.stackoverflow.network.StackoverflowApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetQuestionUseCase(
    private val stackOverflowApi: StackoverflowApi
) {

    private var cache: QuestionSchema? = null
    private var id: Int = 1

    suspend fun fetch(questionId: Int): QuestionFetchResult =
        withContext(Dispatchers.IO) {
            id = questionId
            getQuestionBy(questionId)
        }

    private suspend fun getQuestionBy(id: Int): QuestionFetchResult {
        return when (val result = tryOneOf { stackOverflowApi.getQuestion(id) }) {
            is OneOf.Error -> QuestionFetchResult.Failure(result.e)
            is OneOf.Success -> handleSuccess(result.data)
        }
    }

    private fun handleSuccess(response: QuestionsResponse): QuestionFetchResult {
        cache = null
        if (response.items.isEmpty()) {
            return QuestionFetchResult.Empty
        }

        response.items.first().also { schema ->
            cache = schema
            return QuestionFetchResult.HasData(response.items.first())
        }
    }

}
