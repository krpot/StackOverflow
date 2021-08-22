package com.warmpot.android.stackoverflow.screen.question.list.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.warmpot.android.stackoverflow.R
import com.warmpot.android.stackoverflow.data.schema.QuestionSchema
import com.warmpot.android.stackoverflow.domain.usecase.GetQuestionsUseCase
import com.warmpot.android.stackoverflow.domain.usecase.QuestionsFetchResult
import com.warmpot.android.stackoverflow.screen.common.adapter.ListItem
import com.warmpot.android.stackoverflow.screen.common.isActuallyActive
import com.warmpot.android.stackoverflow.screen.common.resource.Str
import com.warmpot.android.stackoverflow.screen.question.list.LoadingState
import com.warmpot.android.stackoverflow.screen.question.mapper.QuestionMapper
import com.warmpot.android.stackoverflow.screen.question.model.Question
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.net.UnknownHostException

class QuestionListViewModel(
    private val getQuestionsUseCase: GetQuestionsUseCase
) : ViewModel() {

    private val listItemsLiveData = MutableLiveData<List<ListItem>>()
    val listItems: LiveData<List<ListItem>> get() = listItemsLiveData
    private val loadingLiveData = MutableLiveData(false)
    val loading: LiveData<Boolean> get() = loadingLiveData

    private val questionMapper by lazy { QuestionMapper() }

    private val questions: List<ListItem>
        get() = listItemsLiveData.value?.filterIsInstance<Question>() ?: emptyList()

    private var job: Job? = null

    // region public functions
    fun loadFirstPageQuestions() {
        throttleApiCall {
            loadingLiveData.postValue(true)
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

        loadingLiveData.postValue(false)
    }

    // region post functions
    private suspend fun mapAndPostQuestions(schemas: List<QuestionSchema>) {
        val questions = schemas.map { schema -> questionMapper.convert(schema) }
        postListItems(questions)
    }

    private fun postLoadMoreLoading() {
        postListItems(questions.plus(LoadingState(isLoading = true)))
    }

    private fun postLoadQuestionsError(th: Throwable) {
        val str = throwableToStr(th)
        postListItems(questions.plus(LoadingState(message = str, isRetry = true)))
    }

    private fun postEmptyDataItem() {
        val loadingState = LoadingState(message = Str.from(R.string.message_empty_items))
        postListItems(questions.plus(loadingState))
    }

    private fun postNoMoreDataItem() {
        val loadingState = LoadingState(message = Str.from(R.string.message_no_more_items))
        postListItems(questions.plus(loadingState))
    }
// endregion post functions

    // region private helper functions
    private fun throttleApiCall(apiCall: suspend () -> Unit) {
        if (job.isActuallyActive()) return
        job = viewModelScope.launch {
            apiCall()
        }
    }

    private fun postListItems(items: List<ListItem>) {
        listItemsLiveData.postValue(items)
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
