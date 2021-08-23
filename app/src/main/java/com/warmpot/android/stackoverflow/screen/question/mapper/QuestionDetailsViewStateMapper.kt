package com.warmpot.android.stackoverflow.screen.question.mapper

import com.warmpot.android.stackoverflow.R
import com.warmpot.android.stackoverflow.common.AsyncMapper
import com.warmpot.android.stackoverflow.common.OneOf
import com.warmpot.android.stackoverflow.data.schema.answers.AnswersResponse
import com.warmpot.android.stackoverflow.data.schema.qustions.QuestionsResponse
import com.warmpot.android.stackoverflow.screen.common.exception.toUiMessage
import com.warmpot.android.stackoverflow.screen.common.resource.Str
import com.warmpot.android.stackoverflow.screen.question.details.viewmodel.QuestionDetailsViewState

class QuestionDetailsViewStateMapper :
    AsyncMapper<OneOf<Pair<QuestionsResponse, AnswersResponse>>, QuestionDetailsViewState> {

    private val questionMapper by lazy { QuestionMapper() }
    private val answerMapper by lazy { AnswerMapper() }

    override suspend fun convert(src: OneOf<Pair<QuestionsResponse, AnswersResponse>>): QuestionDetailsViewState {
        return when (src) {
            is OneOf.Error -> {
                QuestionDetailsViewState(error = src.e.toUiMessage())
            }
            is OneOf.Success -> {
                val (questionsResponse, answersResponse) = src.data
                if (questionsResponse.items.isEmpty()) {
                    QuestionDetailsViewState(error = Str.from(R.string.error_question_load_failure))
                } else {
                    val question = questionMapper.convert(questionsResponse.items.first())
                    val answers = answersResponse.items.map { answerMapper.convert(it) }
                    QuestionDetailsViewState(question = question, answers = answers)
                }
            }
        }
    }
}
