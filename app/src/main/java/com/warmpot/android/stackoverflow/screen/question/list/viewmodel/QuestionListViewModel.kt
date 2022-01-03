package com.warmpot.android.stackoverflow.screen.question.list.viewmodel

import androidx.lifecycle.LiveData
import com.warmpot.android.stackoverflow.domain.questions.GetQuestionsUseCase
import com.warmpot.android.stackoverflow.domain.questions.QuestionsFetchResult
import com.warmpot.android.stackoverflow.screen.common.viewmodel.BaseViewModel

class QuestionListViewModel(
    private val getQuestionsUseCase: GetQuestionsUseCase,
    private val stateLiveData: QuestionListUiStateLiveData = QuestionListUiStateLiveData()
) : BaseViewModel() {

    val uiState: LiveData<QuestionListUiState> get() = stateLiveData.uiState

    // region public functions
    fun loadFirstPageQuestions() {
        singleLaunch {
            val result = getQuestionsUseCase.loadFirstPage()
            handleQuestionListResult(result)
        }
    }

    fun triggerLoadMore() {
        singleLaunch {
            stateLiveData.postLoadMoreLoading()
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
            stateLiveData.postLoadMoreLoading()
            handleQuestionListResult(getQuestionsUseCase.retry())
        }
    }
    // endregion public functions

    private suspend fun handleQuestionListResult(result: QuestionsFetchResult) {
        when (result) {
            is QuestionsFetchResult.Failure -> {
                stateLiveData.postLoadQuestionsError(result.e)
            }
            is QuestionsFetchResult.Empty -> {
                stateLiveData.postEmptyDataItem()
            }
            is QuestionsFetchResult.EndOfData -> {
                stateLiveData.postNoMoreDataItem()
            }
            is QuestionsFetchResult.HasData -> {
                stateLiveData.postQuestions(result.data)
            }
        }
    }
}
