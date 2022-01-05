package com.warmpot.android.stackoverflow.screen.question.search.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.warmpot.android.stackoverflow.data.qustions.schema.SearchParam
import com.warmpot.android.stackoverflow.domain.questions.*
import com.warmpot.android.stackoverflow.screen.common.viewmodel.BaseViewModel
import com.warmpot.android.stackoverflow.screen.question.list.viewmodel.QuestionListUiState
import com.warmpot.android.stackoverflow.screen.question.list.viewmodel.QuestionListUiStateLiveData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

class SearchViewModel(
    private val searchUseCase: QuestionSearchUseCase,
    private val stateLiveData: QuestionListUiStateLiveData = QuestionListUiStateLiveData()
) : BaseViewModel() {

    val uiState: LiveData<QuestionListUiState> get() = stateLiveData.uiState

    private val loadMoreFailureHandler: (Throwable) -> Unit = { exception ->
        stateLiveData.postLoadQuestionsError(exception)
    }

    private val searchFlow = MutableStateFlow<String?>(null)

    init {
        viewModelScope.launch {
            searchFlow
                .filterNotNull()
                .debounce(500L)
                .collectLatest { query ->
                    searchByQuery(query)
                }
        }
    }

    // region public functions
    fun triggerLoadMore() {
        singleLaunch {
            stateLiveData.postPageLoading()
            handleQuestionListResult(
                searchUseCase.fetchNextPage(),
                loadMoreFailureHandler
            )
        }
    }

    fun loadMoreRetryClicked() {
        singleLaunch {
            stateLiveData.postPageLoading()
            handleQuestionListResult(
                searchUseCase.retry(),
                loadMoreFailureHandler
            )
        }
    }

    fun pullToRefresh() {
        singleLaunch {
            handleQuestionListResult(searchUseCase.refresh()) { exception ->
                stateLiveData.postPullToRefreshError(exception)
            }
        }
    }
    // endregion public functions

    private suspend fun handleQuestionListResult(
        result: QuestionsFetchResult,
        onFetchFailure: (Throwable) -> Unit
    ) {
        when (result) {
            is QuestionsFetchResult.Failure -> onFetchFailure(result.e)
            is QuestionsFetchResult.Empty -> stateLiveData.postEmptyDataItem()
            is QuestionsFetchResult.EndOfData -> stateLiveData.postNoMoreDataItem()
            is QuestionsFetchResult.HasData -> stateLiveData.postQuestions(result.data)
        }
    }

    fun searchQueryChanged(query: String?) {
        searchFlow.value = query
    }

    private fun searchByQuery(query: String) {
        singleLaunch {
            stateLiveData.postScreenLoading()

            val searchResult = searchUseCase.execute(
                param = SearchParam(
                    page = 1,
                    inTitle = query
                ),
                pagingType = PagingType.FirstPage
            )

            handleQuestionListResult(searchResult, loadMoreFailureHandler)
        }
    }
}
