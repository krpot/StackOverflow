package com.warmpot.android.stackoverflow.domain.questions

import com.warmpot.android.stackoverflow.common.OneOf
import com.warmpot.android.stackoverflow.common.tryOneOf
import com.warmpot.android.stackoverflow.data.schema.qustions.QuestionSchema
import com.warmpot.android.stackoverflow.data.schema.qustions.QuestionsResponse
import com.warmpot.android.stackoverflow.network.StackoverflowApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetQuestionsUseCase(
    private val stackOverflowApi: StackoverflowApi
) {

    private val cache = arrayListOf<QuestionSchema>()
    private var page: Int = 1
    private var hasMoreData = false

    private suspend fun execute(page: Int): QuestionsFetchResult =
        withContext(Dispatchers.IO) {
            getQuestionsBy(page)
        }

    suspend fun loadFirstPage() = execute(1)

    suspend fun loadNext(): QuestionsFetchResult {
        return execute(nextPage())
    }

    suspend fun refresh(): QuestionsFetchResult {
        cache.clear()
        return execute(page = this.page)
    }

    suspend fun retry() = execute(page = this.page)

    private suspend fun getQuestionsBy(page: Int): QuestionsFetchResult {
        return when (val result = tryOneOf { stackOverflowApi.getQuestions(page) }) {
            is OneOf.Error -> QuestionsFetchResult.Failure(result.e)
            is OneOf.Success -> {
                handleSuccess(page, result.data)
            }
        }
    }

    private fun handleSuccess(pageNo: Int, response: QuestionsResponse): QuestionsFetchResult {
        this.page = pageNo
        this.hasMoreData = response.hasMore

        if (response.items.isEmpty()) {
            hasMoreData = false
            return if (cache.isEmpty())
                QuestionsFetchResult.Empty(cache)
            else
                QuestionsFetchResult.EndOfData(cache)
        }

        cache.addAll(response.items)
        return QuestionsFetchResult.HasData(cache)
    }

    private fun nextPage() = page.inc()
}
