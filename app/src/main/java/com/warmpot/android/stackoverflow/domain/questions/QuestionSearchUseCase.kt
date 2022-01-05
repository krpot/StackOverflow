package com.warmpot.android.stackoverflow.domain.questions

import com.warmpot.android.stackoverflow.common.OneOf
import com.warmpot.android.stackoverflow.data.qustions.datasource.QuestionDataSource
import com.warmpot.android.stackoverflow.data.qustions.schema.QuestionsResponse
import com.warmpot.android.stackoverflow.data.qustions.schema.SearchParam
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class QuestionSearchUseCase(
    private val dataSource: QuestionDataSource
) {

    private var searchParam: SearchParam? = null

    suspend fun execute(
        param: SearchParam = requireNotNull(searchParam),
        pagingType: PagingType
    ): QuestionsFetchResult =
        withContext(Dispatchers.IO) {
            when (pagingType) {
                is PagingType.FirstPage -> searchQuestionsBy(param.copy(page = 1))
                is PagingType.NextPage -> searchQuestionsBy(param.nextPage())
                is PagingType.Refresh -> searchQuestionsBy(param)
                is PagingType.Retry -> searchQuestionsBy(param)
            }
        }

    private suspend fun searchQuestionsBy(param: SearchParam): QuestionsFetchResult {
        searchParam = param
        return when (val result = dataSource.searchQuestions(param)) {
            is OneOf.Error -> {
                QuestionsFetchResult.Failure(result.e)
            }
            is OneOf.Success -> {
                handleSuccess(result.data)
            }
        }
    }

    private suspend fun handleSuccess(
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

        return QuestionsFetchResult.HasData(response.items)
    }
}

suspend fun QuestionSearchUseCase.fetchNextPage() = execute(pagingType = PagingType.NextPage)

suspend fun QuestionSearchUseCase.refresh() = execute(pagingType = PagingType.Refresh)

suspend fun QuestionSearchUseCase.retry() = execute(pagingType = PagingType.Retry)