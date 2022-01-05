package com.warmpot.android.stackoverflow.screen.question.list.viewmodel

import com.warmpot.android.stackoverflow.common.EventValue
import com.warmpot.android.stackoverflow.screen.common.adapter.LoadingState
import com.warmpot.android.stackoverflow.screen.question.model.Question

data class QuestionListUiState(
    val screenLoading: EventValue<Boolean> = EventValue(false),
    val loading: EventValue<List<LoadingState>> = EventValue(emptyList()),
    val error: EventValue<Throwable>? = null,
    val listItems: List<Question>? = null
)

internal fun stateOf(loading: LoadingState) =
    QuestionListUiState(loading = EventValue(listOf(loading)))

internal fun stateOf(items: List<Question>) = QuestionListUiState(listItems = items)
