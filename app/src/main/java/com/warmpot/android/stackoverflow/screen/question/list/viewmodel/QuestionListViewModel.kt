package com.warmpot.android.stackoverflow.screen.question.list.viewmodel

import androidx.lifecycle.LiveData
import com.warmpot.android.stackoverflow.domain.questions.GetQuestionsUseCase
import com.warmpot.android.stackoverflow.domain.questions.QuestionsFetchResult
import com.warmpot.android.stackoverflow.screen.common.viewmodel.BaseViewModel

class QuestionListViewModel(
    private val getQuestionsUseCase: GetQuestionsUseCase,
    private val uiStateLiveData: QuestionListUiStateLiveData = QuestionListUiStateLiveData()
) : BaseViewModel() {

    val uiState: LiveData<QuestionListUiState> get() = uiStateLiveData.uiState

    // region public functions
    fun loadFirstPageQuestions() {
        singleLaunch {
            val result = getQuestionsUseCase.loadFirstPage()
            handleQuestionListResult(result)
        }
    }

    fun triggerLoadMore() {
        singleLaunch {
            uiStateLiveData.postLoadMoreLoading()
            handleQuestionListResult(getQuestionsUseCase.loadNext())
        }
    }

    fun pullToRefresh() {
        singleLaunch {
            handleQuestionListResult(getQuestionsUseCase.refresh())
        }
    }

    fun loadMoreRetryClicked() {
        singleLaunch {
            uiStateLiveData.postLoadMoreLoading()
            handleQuestionListResult(getQuestionsUseCase.retry())
        }
    }
    // endregion public functions

    private suspend fun handleQuestionListResult(result: QuestionsFetchResult) {
        when (result) {
            is QuestionsFetchResult.Failure -> {
                uiStateLiveData.postLoadQuestionsError(result.e)
            }
            is QuestionsFetchResult.Empty -> {
                uiStateLiveData.postEmptyDataItem()
            }
            is QuestionsFetchResult.EndOfData -> {
                uiStateLiveData.postNoMoreDataItem()
            }
            is QuestionsFetchResult.HasData -> {
                uiStateLiveData.postQuestions(result.data)
            }
        }
    }
}
