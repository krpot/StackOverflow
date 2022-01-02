package com.warmpot.android.stackoverflow.screen.question.list.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.warmpot.android.stackoverflow.R
import com.warmpot.android.stackoverflow.data.schema.qustions.QuestionSchema
import com.warmpot.android.stackoverflow.screen.common.adapter.LoadingState
import com.warmpot.android.stackoverflow.screen.common.exception.toUiMessage
import com.warmpot.android.stackoverflow.screen.common.resource.Str
import com.warmpot.android.stackoverflow.screen.question.mapper.QuestionMapper

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

    fun postLoadMoreLoading() {
        postLoadMore(isLoading = true)
    }

    fun postLoadQuestionsError(th: Throwable) {
        val str = th.toUiMessage()
        postLoadMore(message = str, isRetry = true)
    }

    fun postEmptyDataItem() {
        postLoadMore(message = Str.from(R.string.message_empty_items))
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
}