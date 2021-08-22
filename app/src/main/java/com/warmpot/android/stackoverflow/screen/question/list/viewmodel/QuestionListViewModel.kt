package com.warmpot.android.stackoverflow.screen.question.list.viewmodel

import androidx.lifecycle.*
import com.warmpot.android.stackoverflow.R
import com.warmpot.android.stackoverflow.data.schema.QuestionSchema
import com.warmpot.android.stackoverflow.domain.usecase.GetQuestionsUseCase
import com.warmpot.android.stackoverflow.domain.usecase.QuestionsFetchResult
import com.warmpot.android.stackoverflow.screen.common.adapter.LoadingState
import com.warmpot.android.stackoverflow.screen.common.isActuallyActive
import com.warmpot.android.stackoverflow.screen.common.resource.Str
import com.warmpot.android.stackoverflow.screen.question.mapper.QuestionMapper
import com.warmpot.android.stackoverflow.screen.question.model.Question
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.net.UnknownHostException

class QuestionListViewModel(
    private val getQuestionsUseCase: GetQuestionsUseCase
) : ViewModel() {

    private val questionsLiveData = MutableLiveData<QuestionListViewState.ListItem>()
    private val loadingLiveData = MutableLiveData<QuestionListViewState.Loading>()

    private val questionMapper by lazy { QuestionMapper() }

    private var job: Job? = null

    fun observe(owner: LifecycleOwner, onChange: (QuestionListViewState) -> Unit) {
        questionsLiveData.observe(owner, Observer(onChange))
        loadingLiveData.observe(owner, Observer(onChange))
    }

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
        val str = throwableToStr(th)
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
        loadingLiveData.postValue(stateOf(LoadingState(isLoading = isLoading, message = message, isRetry = isRetry)))
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
        questionsLiveData.postValue(stateOf(items))
    }

    private fun throwableToStr(th: Throwable): Str {
        return when (th) {
            is UnknownHostException -> Str.from(R.string.error_no_connectivity)
            is HttpException -> Str.from("${th.message()} (${th.code()})")
            else -> Str.from(R.string.error_network_unavailable)
        }
    }
// endregion private helper functions
}
