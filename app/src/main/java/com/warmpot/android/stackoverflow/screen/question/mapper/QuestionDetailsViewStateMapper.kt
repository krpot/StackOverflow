package com.warmpot.android.stackoverflow.screen.question.mapper

import com.warmpot.android.stackoverflow.R
import com.warmpot.android.stackoverflow.common.AsyncMapper
import com.warmpot.android.stackoverflow.common.OneOf
import com.warmpot.android.stackoverflow.data.schema.answers.AnswersResponse
import com.warmpot.android.stackoverflow.data.schema.qustions.QuestionsResponse
import com.warmpot.android.stackoverflow.screen.common.exception.toUiMessage
import com.warmpot.android.stackoverflow.screen.common.resource.Str
import com.warmpot.android.stackoverflow.screen.question.details.viewmodel.QuestionDetailsUiState

class QuestionDetailsViewStateMapper :
    AsyncMapper<OneOf<Pair<QuestionsResponse, AnswersResponse>>, QuestionDetailsUiState> {

    private val questionMapper by lazy { QuestionMapper() }
    private val answerMapper by lazy { AnswerMapper() }

    override suspend fun convert(src: OneOf<Pair<QuestionsResponse, AnswersResponse>>): QuestionDetailsUiState {
        return when (src) {
            is OneOf.Error -> {
                QuestionDetailsUiState(error = src.e.toUiMessage())
            }
            is OneOf.Success -> {
                val (questionsResponse, answersResponse) = src.data
                if (questionsResponse.items.isEmpty()) {
                    QuestionDetailsUiState(error = Str.from(R.string.error_question_load_failure))
                } else {
                    val question = questionMapper.convert(questionsResponse.items.first())
                    val answers = answersResponse.items.map { answerMapper.convert(it) }
                    QuestionDetailsUiState(question = question.copy(answers = answers))
                }
            }
        }
    }
}
