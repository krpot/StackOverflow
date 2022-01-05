package com.warmpot.android.stackoverflow.domain.questions

import com.warmpot.android.stackoverflow.common.OneOf
import com.warmpot.android.stackoverflow.data.qustions.datasource.QuestionDataSource
import com.warmpot.android.stackoverflow.data.qustions.schema.QuestionsResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetQuestionsUseCase(
    private val dataSource: QuestionDataSource
) {
    companion object {
        private const val FIRST_PAGE = 1
    }

    private val cache = hashMapOf<Int, QuestionsResponse>()
    private var currentPage: Int = 1
    private var hasMoreData = false

    suspend fun execute(fetchingType: FetchingType): QuestionsFetchResult =
        withContext(Dispatchers.IO) {
            when (fetchingType) {
                is FetchingType.FirstPage -> getQuestionsBy(page = FIRST_PAGE)
                is FetchingType.NextPage -> getQuestionsBy(page = nextPage())
                is FetchingType.Refresh -> refreshData()
                is FetchingType.Retry -> getQuestionsBy(page = currentPage)
            }
        }

    private suspend fun refreshData(): QuestionsFetchResult {
        cache.clear()
        return getQuestionsBy(page = FIRST_PAGE)
    }

    private suspend fun getQuestionsBy(page: Int): QuestionsFetchResult {
        return when (val result = dataSource.getQuestions(page)) {
            is OneOf.Error -> {
                val cachedData = cache[page]
                if (cachedData != null) {
                    return handleSuccess(pageNo = page, response = cachedData)
                }
                QuestionsFetchResult.Failure(result.e)
            }
            is OneOf.Success -> {
                handleSuccess(page, result.data)
            }
        }
    }

    private fun handleSuccess(pageNo: Int, response: QuestionsResponse): QuestionsFetchResult {
        this.currentPage = pageNo
        this.hasMoreData = response.hasMore

        if (response.items.isEmpty()) {
            hasMoreData = false
            return if (cache.isEmpty())
                QuestionsFetchResult.Empty(response.items)
            else
                QuestionsFetchResult.EndOfData(response.items)
        }

        cache[currentPage] = response
        return QuestionsFetchResult.HasData(response.items)
    }

    private fun nextPage() = currentPage.inc()
}

sealed class FetchingType {
    object FirstPage : FetchingType()
    object NextPage : FetchingType()
    object Refresh : FetchingType()
    object Retry : FetchingType()
}

suspend fun GetQuestionsUseCase.fetchFirstPage() = execute(FetchingType.FirstPage)

suspend fun GetQuestionsUseCase.fetchNextPage() = execute(FetchingType.NextPage)

suspend fun GetQuestionsUseCase.refresh() = execute(FetchingType.Refresh)

suspend fun GetQuestionsUseCase.retry() = execute(FetchingType.Retry)