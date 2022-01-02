package com.warmpot.android.stackoverflow.screen.question.details.viewmodel

import androidx.lifecycle.LiveData
import com.warmpot.android.stackoverflow.common.OneOf
import com.warmpot.android.stackoverflow.data.schema.answers.AnswersResponse
import com.warmpot.android.stackoverflow.data.schema.qustions.QuestionsResponse
import com.warmpot.android.stackoverflow.domain.questions.GetQuestionDetailsUseCase
import com.warmpot.android.stackoverflow.screen.common.viewmodel.BaseViewModel

class QuestionDetailsViewModel(
    private val getQuestionDetailsUseCase: GetQuestionDetailsUseCase,
    private val uiStateLiveData: QuestionDetailsUiStateLiveData = QuestionDetailsUiStateLiveData()
) : BaseViewModel() {

    val uiState: LiveData<QuestionDetailsUiState> get() = uiStateLiveData.uiState

    // region public functions
    fun fetch(questionId: Int) {
        singleLaunch {
            val result = getQuestionDetailsUseCase.execute(questionId)
            handleQuestionResult(result)
        }
    }
    // endregion public functions

    private suspend fun handleQuestionResult(result: OneOf<Pair<QuestionsResponse, AnswersResponse>>) {
        uiStateLiveData.postResult(result)
    }
}
