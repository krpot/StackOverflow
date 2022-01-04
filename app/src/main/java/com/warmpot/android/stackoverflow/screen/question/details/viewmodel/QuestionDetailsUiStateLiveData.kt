package com.warmpot.android.stackoverflow.screen.question.details.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.warmpot.android.stackoverflow.common.OneOf
import com.warmpot.android.stackoverflow.data.answers.AnswersResponse
import com.warmpot.android.stackoverflow.data.qustions.schema.QuestionsResponse
import com.warmpot.android.stackoverflow.screen.question.mapper.QuestionDetailsViewStateMapper

class QuestionDetailsUiStateLiveData(
    private val uiStateLiveData: MutableLiveData<QuestionDetailsUiState> = MutableLiveData()
) {
    val uiState: LiveData<QuestionDetailsUiState> get() = uiStateLiveData

    private val uiStateMapper by lazy { QuestionDetailsViewStateMapper() }

    suspend fun postResult(result: OneOf<Pair<QuestionsResponse, AnswersResponse>>) {
        val state = uiStateMapper.convert(result)
        uiStateLiveData.postValue(state)
    }
}