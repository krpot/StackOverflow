package com.warmpot.android.stackoverflow.domain.questions

import com.warmpot.android.stackoverflow.common.OneOf
import com.warmpot.android.stackoverflow.data.qustions.cache.QuestionsResponseCache
import com.warmpot.android.stackoverflow.data.qustions.datasource.QuestionDataSource
import com.warmpot.android.stackoverflow.data.qustions.schema.QuestionsResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class QuestionPagingUseCase(
    private val dataSource: QuestionDataSource,
    private val cache: QuestionsResponseCache
) {
    companion object {
        private const val FIRST_PAGE = 1
    }

    suspend fun execute(pagingType: PagingType): QuestionsFetchResult =
        withContext(Dispatchers.IO) {
            when (pagingType) {
                is PagingType.FirstPage -> getQuestionsBy(page = FIRST_PAGE)
                is PagingType.NextPage -> getQuestionsBy(page = nextPage())
                is PagingType.Refresh -> refreshData()
                is PagingType.Retry -> getQuestionsBy(page = cache.currentPage)
            }
        }

    private suspend fun refreshData(): QuestionsFetchResult {
        cache.clear()
        return getQuestionsBy(page = FIRST_PAGE)
    }

    private suspend fun getQuestionsBy(page: Int): QuestionsFetchResult {
        return when (val result = dataSource.getQuestions(page)) {
            is OneOf.Error -> {
                val cachedData = cache.getData(page)
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

    private suspend fun handleSuccess(
        pageNo: Int,
        response: QuestionsResponse
    ): QuestionsFetchResult {
        when {
            response.items.isEmpty() -> {
                if (!response.hasMore) {
                    QuestionsFetchResult.EndOfData(response.items)
                } else {
                    QuestionsFetchResult.Empty(response.items)
                }
            }
            !response.hasMore -> QuestionsFetchResult.EndOfData(response.items)
        }

        cache.update(page = pageNo, data = response)
        return QuestionsFetchResult.HasData(response.items)
    }

    private fun nextPage() = cache.currentPage.inc()
}

suspend fun QuestionPagingUseCase.fetchFirstPage() = execute(PagingType.FirstPage)

suspend fun QuestionPagingUseCase.fetchNextPage() = execute(PagingType.NextPage)

suspend fun QuestionPagingUseCase.refresh() = execute(PagingType.Refresh)

suspend fun QuestionPagingUseCase.retry() = execute(PagingType.Retry)