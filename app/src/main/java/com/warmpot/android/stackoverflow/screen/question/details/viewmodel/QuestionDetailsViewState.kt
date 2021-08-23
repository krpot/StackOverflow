package com.warmpot.android.stackoverflow.screen.question.details.viewmodel

import com.warmpot.android.stackoverflow.screen.common.resource.Str
import com.warmpot.android.stackoverflow.screen.question.model.Answer
import com.warmpot.android.stackoverflow.screen.question.model.Question

class QuestionDetailsViewState(
    val error: Str? = null,
    val question: Question? = null,
    val answers: List<Answer>? = null
)
