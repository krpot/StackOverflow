package com.warmpot.android.stackoverflow.screen.question.list.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.warmpot.android.stackoverflow.R
import com.warmpot.android.stackoverflow.common.EventValue
import com.warmpot.android.stackoverflow.data.qustions.schema.QuestionSchema
import com.warmpot.android.stackoverflow.screen.common.adapter.LoadingState
import com.warmpot.android.stackoverflow.screen.common.exception.toUiMessage
import com.warmpot.android.stackoverflow.screen.common.resource.Str
import com.warmpot.android.stackoverflow.screen.question.mapper.QuestionMapper
import com.warmpot.android.stackoverflow.screen.question.model.Question

class QuestionListUiStateLiveData(
    private val uiStateLiveData: MutableLiveData<QuestionListUiState> = MutableLiveData()
) {
    val uiState: LiveData<QuestionListUiState> get() = uiStateLiveData

    private val questionMapper by lazy { QuestionMapper() }

    suspend fun postQuestions(schemas: List<QuestionSchema>) {
        val items = schemas.map { questionMapper.convert(it) }
        uiStateLiveData.postValue(stateOf(items))
    }

    fun postNoMoreDataItem() {
        postLoadMore(message = Str.from(R.string.message_no_more_items))
    }

    fun postScreenLoading() {
        uiStateLiveData.postValue(forwardState(isScreenLoading = true))
    }

    fun postPageLoading() {
        postLoadMore(isLoading = true)
    }

    fun postLoadQuestionsError(th: Throwable) {
        val str = th.toUiMessage()
        postLoadMore(message = str, isRetry = true)
    }

    fun postPullToRefreshError(th: Throwable) {
        val state = uiState.value ?: QuestionListUiState()
        uiStateLiveData.postValue(
            state.copy(loading = EventValue(emptyList()), error = EventValue(th))
        )
    }

    fun postEmptyDataItem() {
        postLoadMore(message = Str.from(R.string.message_empty_items))
    }

    private fun postLoadMore(
        isLoading: Boolean = false,
        message: Str? = null,
        isRetry: Boolean = false
    ) {
        val loadingState = LoadingState(
            isLoading = isLoading,
            message = message,
            isRetry = isRetry
        )

        uiStateLiveData.postValue(forwardState(loading = loadingState))
    }

    private fun forwardState(
        isScreenLoading: Boolean = false,
        loading: LoadingState? = null,
        error: Throwable? = null,
        listItems: List<Question>? = uiState.value?.listItems
    ): QuestionListUiState {
        val state = uiState.value ?: QuestionListUiState()
        val loadingStates: List<LoadingState> = loading?.let { listOf(it) } ?: emptyList()

        return state.copy(
            screenLoading = EventValue(isScreenLoading),
            loading = EventValue(loadingStates),
            error = error?.let { EventValue(it) },
            listItems = listItems
        )
    }
}