package com.warmpot.android.stackoverflow.screen.question.mapper

import com.warmpot.android.stackoverflow.common.AsyncMapper
import com.warmpot.android.stackoverflow.data.answers.AnswerSchema
import com.warmpot.android.stackoverflow.domain.utils.StackoverflowHtmlParser
import com.warmpot.android.stackoverflow.screen.question.model.Answer
import com.warmpot.android.stackoverflow.screen.user.mapper.UserMapper

class AnswerMapper : AsyncMapper<AnswerSchema, Answer> {

    private val userMapper by lazy { UserMapper() }

    override suspend fun convert(src: AnswerSchema): Answer {
        return Answer(
            answerId = src.answerId,
            body = getHtmlContent(src.body),
            contentLicense = src.contentLicense ?: "",
            isAccepted = src.isAccepted ?: false,
            creationDate = src.creationDate,
            lastActivityDate = src.lastActivityDate,
            lastEditDate = src.lastEditDate,
            owner = userMapper.convert(src.owner),
            questionId = src.questionId,
            score = src.score,
        )
    }

    private fun getHtmlContent(body: String?): String {
        return body?.let { s ->
            StackoverflowHtmlParser.parseQuestionBodyHtml(s)
        } ?: ""
    }
}
