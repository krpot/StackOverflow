package com.warmpot.android.stackoverflow.screen.question.details.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.warmpot.android.stackoverflow.R
import com.warmpot.android.stackoverflow.data.schema.QuestionSchema
import com.warmpot.android.stackoverflow.domain.usecase.GetQuestionUseCase
import com.warmpot.android.stackoverflow.domain.usecase.QuestionFetchResult
import com.warmpot.android.stackoverflow.screen.common.adapter.LoadingState
import com.warmpot.android.stackoverflow.screen.common.isActuallyActive
import com.warmpot.android.stackoverflow.screen.common.resource.Str
import com.warmpot.android.stackoverflow.screen.question.mapper.QuestionMapper
import com.warmpot.android.stackoverflow.screen.question.model.Question
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.net.UnknownHostException

class QuestionDetailsViewModel(
    private val getQuestionUseCase: GetQuestionUseCase
) : ViewModel() {

    private val questionLiveData = MutableLiveData<Question>()
    val question: LiveData<Question> get() = questionLiveData

    private val questionMapper by lazy { QuestionMapper() }

    private var job: Job? = null

    // region public functions
    fun fetch(questionId: Int) {
        throttleApiCall {
            val result = getQuestionUseCase.fetch(questionId)
            handleQuestionResult(result)
        }
    }
    // endregion public functions

    private suspend fun handleQuestionResult(result: QuestionFetchResult) {
        when (result) {
            is QuestionFetchResult.Failure -> {
                postLoadQuestionsError(result.e)
            }
            is QuestionFetchResult.Empty -> {
                postEmptyDataItem()
            }
            is QuestionFetchResult.HasData -> {
                mapAndPostQuestions(result.data)
            }
        }
    }

    // region post functions
    private suspend fun mapAndPostQuestions(schema: QuestionSchema) {
        val question = questionMapper.convert(schema)
        questionLiveData.postValue(question)
    }

    private fun postLoadQuestionsError(th: Throwable) {
        val str = throwableToStr(th)
        //postListItems(questions.plus(LoadingState(message = str, isRetry = true)))
    }

    private fun postEmptyDataItem() {
        val loadingState = LoadingState(message = Str.from(R.string.message_empty_items))
        //postListItems(questions.plus(loadingState))
    }
    // endregion post functions

    // region private helper functions
    private fun throttleApiCall(apiCall: suspend () -> Unit) {
        if (job.isActuallyActive()) return
        job = viewModelScope.launch {
            apiCall()
        }
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
