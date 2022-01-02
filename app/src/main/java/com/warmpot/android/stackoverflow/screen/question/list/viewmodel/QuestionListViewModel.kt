package com.warmpot.android.stackoverflow.screen.question.list.viewmodel

import androidx.lifecycle.LiveData
import com.warmpot.android.stackoverflow.data.schema.qustions.QuestionSchema
import com.warmpot.android.stackoverflow.domain.questions.GetQuestionsUseCase
import com.warmpot.android.stackoverflow.domain.questions.QuestionsFetchResult
import com.warmpot.android.stackoverflow.screen.common.viewmodel.BaseViewModel
import com.warmpot.android.stackoverflow.screen.question.model.Question

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
                mapAndPostQuestions(result.data)
            }
        }
    }

    // region post functions
    private suspend fun mapAndPostQuestions(schemas: List<QuestionSchema>) {
        val questions = schemas.map { schema -> Question.from(schema) }
        uiStateLiveData.postQuestions(questions)
    }
    // endregion post functions
}
