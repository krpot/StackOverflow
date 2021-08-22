package com.warmpot.android.stackoverflow.screen.question.list.viewmodel

import com.warmpot.android.stackoverflow.screen.common.adapter.LoadingState
import com.warmpot.android.stackoverflow.screen.question.model.Question

sealed class QuestionListViewState {
    data class Loading(val data: List<LoadingState>? = null) : QuestionListViewState()
    data class ListItem(val data: List<Question>? = null) : QuestionListViewState()
}

internal fun stateOf(state: LoadingState) = QuestionListViewState.Loading(listOf(state))
internal fun stateOf(state: List<Question>) = QuestionListViewState.ListItem(state)
