package com.warmpot.android.stackoverflow.screen.question.details.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.warmpot.android.stackoverflow.common.OneOf
import com.warmpot.android.stackoverflow.data.schema.answers.AnswersResponse
import com.warmpot.android.stackoverflow.data.schema.qustions.QuestionsResponse
import com.warmpot.android.stackoverflow.domain.questions.GetQuestionDetailsUseCase
import com.warmpot.android.stackoverflow.screen.common.isActuallyActive
import com.warmpot.android.stackoverflow.screen.question.mapper.QuestionDetailsViewStateMapper
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class QuestionDetailsViewModel(
    private val getQuestionDetailsUseCase: GetQuestionDetailsUseCase
) : ViewModel() {

    private val viewStateLiveData = MutableLiveData<QuestionDetailsViewState>()
    val viewState: LiveData<QuestionDetailsViewState> get() = viewStateLiveData

    private val viewStateMapper by lazy { QuestionDetailsViewStateMapper() }

    private var job: Job? = null

    // region public functions
    fun fetch(questionId: Int) {
        throttleApiCall {
            val result = getQuestionDetailsUseCase.execute(questionId)
            handleQuestionResult(result)
        }
    }
    // endregion public functions

    private suspend fun handleQuestionResult(result: OneOf<Pair<QuestionsResponse, AnswersResponse>>) {
        val viewState = viewStateMapper.convert(result)
        viewStateLiveData.postValue(viewState)
    }
    // endregion post functions

    // region private helper functions
    private fun throttleApiCall(apiCall: suspend () -> Unit) {
        if (job.isActuallyActive()) return
        job = viewModelScope.launch {
            apiCall()
        }
    }
    // endregion private helper functions
}
