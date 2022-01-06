package com.warmpot.android.stackoverflow.screen.question.search.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.warmpot.android.stackoverflow.R
import com.warmpot.android.stackoverflow.common.EventValue
import com.warmpot.android.stackoverflow.data.qustions.schema.QuestionSchema
import com.warmpot.android.stackoverflow.data.tags.schema.TagSchema
import com.warmpot.android.stackoverflow.screen.common.adapter.LoadingState
import com.warmpot.android.stackoverflow.screen.common.exception.toUiMessage
import com.warmpot.android.stackoverflow.screen.common.resource.Str
import com.warmpot.android.stackoverflow.screen.question.mapper.QuestionMapper
import com.warmpot.android.stackoverflow.domain.mapper.TagMapper
import com.warmpot.android.stackoverflow.screen.question.model.Question
import com.warmpot.android.stackoverflow.domain.model.TagEntity

class SearchUiStateLiveData(
    private val uiStateLiveData: MutableLiveData<SearchUiState> = MutableLiveData()
) {
    val uiState: LiveData<SearchUiState> get() = uiStateLiveData

    private val questionMapper by lazy { QuestionMapper() }
    private val tagMapper by lazy { TagMapper() }

    suspend fun postQuestions(schemas: List<QuestionSchema>) {
        val items = schemas.map { questionMapper.convert(it) }
        uiStateLiveData.postValue(
            forwardState(questions = items)
        )
    }

    suspend fun postTags(schemas: List<TagSchema>) {
        val items = schemas.map { tagMapper.convert(it) }
        uiStateLiveData.postValue(
            forwardState(tags = items)
        )
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

    fun postScreenError(th: Throwable) {
        uiStateLiveData.postValue(
            forwardState(error = th)
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
        questions: List<Question>? = uiState.value?.questions,
        tags: List<TagEntity>? = uiState.value?.tags,
    ): SearchUiState {
        val state = uiState.value ?: SearchUiState()
        val loadingStates: List<LoadingState> = loading?.let { listOf(it) } ?: emptyList()

        return state.copy(
            screenLoading = EventValue(isScreenLoading),
            loading = EventValue(loadingStates),
            error = error?.let { EventValue(it) },
            questions = questions,
            tags = tags
        )
    }
}