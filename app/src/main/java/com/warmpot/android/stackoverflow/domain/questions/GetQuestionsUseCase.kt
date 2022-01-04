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

    suspend fun execute(fetchingType: FetchingType): QuestionsFetchResult =
        withContext(Dispatchers.IO) {
            when (fetchingType) {
                is FetchingType.FirstPage -> getQuestionsBy(1)
                is FetchingType.NextPage -> getQuestionsBy(nextPage())
                is FetchingType.Refresh -> refreshData()
                is FetchingType.Retry -> getQuestionsBy(page = page)
            }
        }

    private suspend fun refreshData(): QuestionsFetchResult {
        cache.clear()
        return getQuestionsBy(page = this.page)
    }

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

sealed class FetchingType {
    object FirstPage: FetchingType()
    object NextPage: FetchingType()
    object Refresh: FetchingType()
    object Retry: FetchingType()
}

suspend fun GetQuestionsUseCase.fetchFirstPage() = execute(FetchingType.FirstPage)

suspend fun GetQuestionsUseCase.fetchNextPage() = execute(FetchingType.NextPage)

suspend fun GetQuestionsUseCase.refresh() = execute(FetchingType.Refresh)

suspend fun GetQuestionsUseCase.retry() = execute(FetchingType.Retry)