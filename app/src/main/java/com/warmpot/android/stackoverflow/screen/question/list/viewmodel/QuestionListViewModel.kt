package com.warmpot.android.stackoverflow.screen.question.list.viewmodel

import androidx.lifecycle.LiveData
import com.warmpot.android.stackoverflow.domain.questions.*
import com.warmpot.android.stackoverflow.screen.common.viewmodel.BaseViewModel

class QuestionListViewModel(
    private val getQuestionsUseCase: QuestionPagingUseCase,
    private val stateLiveData: QuestionListUiStateLiveData = QuestionListUiStateLiveData()
) : BaseViewModel() {

    val uiState: LiveData<QuestionListUiState> get() = stateLiveData.uiState

    private val loadMoreFailureHandler: (Throwable) -> Unit = { exception ->
        stateLiveData.postLoadQuestionsError(exception)
    }

    // region public functions
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
