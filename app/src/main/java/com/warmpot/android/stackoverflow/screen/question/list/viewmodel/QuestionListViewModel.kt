package com.warmpot.android.stackoverflow.screen.question.list.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.warmpot.android.stackoverflow.domain.questions.*
import com.warmpot.android.stackoverflow.domain.tags.GetTagsUseCase
import com.warmpot.android.stackoverflow.domain.tags.TagFetchResult
import com.warmpot.android.stackoverflow.screen.common.viewmodel.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

class QuestionListViewModel(
    private val getQuestionsUseCase: QuestionPagingUseCase,
    private val getTagsUseCase: GetTagsUseCase,
    private val stateLiveData: QuestionListUiStateLiveData = QuestionListUiStateLiveData()
) : BaseViewModel() {

    val uiState: LiveData<QuestionListUiState> get() = stateLiveData.uiState

    private val loadMoreFailureHandler: (Throwable) -> Unit = { exception ->
        stateLiveData.postLoadQuestionsError(exception)
    }

    private val tagFlow = MutableStateFlow<String?>(null)

    init {
        viewModelScope.launch {
            tagFlow
                .filterNotNull()
                .debounce(500L)
                .collectLatest { query ->
                    fetchSuggestions(query)
                }
        }
    }

    // region public functions
    fun searchQueryChanged(query: String) {
        tagFlow.value = query
    }

    private fun fetchSuggestions(query: String) {
        if (query.isBlank()) return

        singleLaunch {
            handleTagListResult(query = query, result = getTagsUseCase.execute(query))
        }
    }

    private suspend fun handleTagListResult(
        query: String,
        result: TagFetchResult
    ) {
        when (result) {
            is TagFetchResult.Failure -> {
                // Ignore
            }
            is TagFetchResult.Empty -> stateLiveData.postEmptyDataItem()
            is TagFetchResult.HasData -> stateLiveData.postTags(
                query = query,
                entities = result.data
            )
        }
    }

    fun loadFirstPageQuestions() {
        singleLaunch {
            val result = getQuestionsUseCase.fetchFirstPage()
            handleQuestionListResult(result, loadMoreFailureHandler)
        }
    }

    fun triggerLoadMore() {
        singleLaunch {
            stateLiveData.postPageLoading()
            handleQuestionListResult(
                getQuestionsUseCase.fetchNextPage(),
                loadMoreFailureHandler
            )
        }
    }

    fun loadMoreRetryClicked() {
        singleLaunch {
            stateLiveData.postPageLoading()
            handleQuestionListResult(getQuestionsUseCase.retry(), loadMoreFailureHandler)
        }
    }

    fun pullToRefresh() {
        singleLaunch {
            handleQuestionListResult(getQuestionsUseCase.refresh()) { exception ->
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
}
