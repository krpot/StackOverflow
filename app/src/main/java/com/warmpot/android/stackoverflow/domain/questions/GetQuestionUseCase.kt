package com.warmpot.android.stackoverflow.domain.questions

import com.warmpot.android.stackoverflow.common.OneOf
import com.warmpot.android.stackoverflow.common.switchMap
import com.warmpot.android.stackoverflow.common.tryOneOf
import com.warmpot.android.stackoverflow.data.schema.qustions.QuestionSchema
import com.warmpot.android.stackoverflow.data.schema.qustions.QuestionsResponse
import com.warmpot.android.stackoverflow.network.StackoverflowApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetQuestionUseCase(
    private val stackOverflowApi: StackoverflowApi
) {

    // TODO : Use later
    private var cache: QuestionSchema? = null

    private suspend fun execute(questionId: Int): OneOf<QuestionSchema> =
        withContext(Dispatchers.IO) {
            getQuestionBy(questionId)
        }

    private suspend fun getQuestionBy(questionId: Int): OneOf<QuestionSchema> {
        return tryOneOf {
            stackOverflowApi.getQuestion(questionId)
        }.switchMap { questionsResponse ->
            handleSuccess(questionsResponse)
        }
    }

    private fun handleSuccess(response: QuestionsResponse): OneOf<QuestionSchema> {
        if (response.items.isEmpty()) {
            return OneOf.Error(NoSuchElementException())
        }

        cache = response.items.first()

        return OneOf.Success(response.items.first())
    }
}
