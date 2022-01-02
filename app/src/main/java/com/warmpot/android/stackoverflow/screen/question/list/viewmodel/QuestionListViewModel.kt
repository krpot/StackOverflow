package com.warmpot.android.stackoverflow.screen.question.list.viewmodel

import androidx.lifecycle.*
import com.warmpot.android.stackoverflow.R
import com.warmpot.android.stackoverflow.data.schema.qustions.QuestionSchema
import com.warmpot.android.stackoverflow.domain.questions.GetQuestionsUseCase
import com.warmpot.android.stackoverflow.domain.questions.QuestionsFetchResult
import com.warmpot.android.stackoverflow.screen.common.adapter.LoadingState
import com.warmpot.android.stackoverflow.screen.common.exception.toUiMessage
import com.warmpot.android.stackoverflow.screen.common.isActuallyActive
import com.warmpot.android.stackoverflow.screen.common.resource.Str
import com.warmpot.android.stackoverflow.screen.question.mapper.QuestionMapper
import com.warmpot.android.stackoverflow.screen.question.model.Question
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class QuestionListViewModel(
    private val getQuestionsUseCase: GetQuestionsUseCase
) : ViewModel() {

    private val uiStateLiveData = MutableLiveData<QuestionListUiState>()
    val uiState: LiveData<QuestionListUiState> get() = uiStateLiveData

    private val questionMapper by lazy { QuestionMapper() }

    private var job: Job? = null

    // region public functions
    fun loadFirstPageQuestions() {
        throttleApiCall {
            val result = getQuestionsUseCase.loadFirstPage()
            handleQuestionListResult(result)
        }
    }

    fun triggerLoadMore() {
        throttleApiCall {
            postLoadMoreLoading()
            handleQuestionListResult(getQuestionsUseCase.loadNext())
        }
    }

    fun pullToRefresh() {
        throttleApiCall {
            handleQuestionListResult(getQuestionsUseCase.refresh())
        }
    }

    fun loadMoreRetryClicked() {
        throttleApiCall {
            postLoadMoreLoading()
            handleQuestionListResult(getQuestionsUseCase.retry())
        }
    }
    // endregion public functions

    private suspend fun handleQuestionListResult(result: QuestionsFetchResult) {
        when (result) {
            is QuestionsFetchResult.Failure -> {
                postLoadQuestionsError(result.e)
            }
            is QuestionsFetchResult.Empty -> {
                postEmptyDataItem()
            }
            is QuestionsFetchResult.EndOfData -> {
                postNoMoreDataItem()
            }
            is QuestionsFetchResult.HasData -> {
                mapAndPostQuestions(result.data)
            }
        }
    }

    // region post functions
    private suspend fun mapAndPostQuestions(schemas: List<QuestionSchema>) {
        val questions = schemas.map { schema -> questionMapper.convert(schema) }
        postQuestions(questions)
    }

    private fun postLoadMoreLoading() {
        postLoadMore(isLoading = true)
    }

    private fun postLoadQuestionsError(th: Throwable) {
        val str = th.toUiMessage()
        postLoadMore(message = str, isRetry = true)
    }

    private fun postEmptyDataItem() {
        postLoadMore(message = Str.from(R.string.message_empty_items))
    }

    private fun postNoMoreDataItem() {
        postLoadMore(message = Str.from(R.string.message_no_more_items))
    }

    private fun postLoadMore(
        isLoading: Boolean = false,
        message: Str? = null,
        isRetry: Boolean = false
    ) {
        uiStateLiveData.postValue(
            stateOf(
                LoadingState(
                    isLoading = isLoading,
                    message = message,
                    isRetry = isRetry
                )
            )
        )
    }
    // endregion post functions

    // region private helper functions
    private fun throttleApiCall(apiCall: suspend () -> Unit) {
        if (job.isActuallyActive()) return
        job = viewModelScope.launch {
            apiCall()
        }
    }

    private fun postQuestions(items: List<Question>) {
        uiStateLiveData.postValue(stateOf(items))
    }
    // endregion private helper functions
}
