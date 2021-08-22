package com.warmpot.android.stackoverflow.screen.question.mapper

import com.warmpot.android.stackoverflow.common.AsyncMapper
import com.warmpot.android.stackoverflow.common.EpochSecond
import com.warmpot.android.stackoverflow.data.schema.QuestionSchema
import com.warmpot.android.stackoverflow.domain.utils.StackoverflowHtmlParser
import com.warmpot.android.stackoverflow.screen.question.model.Question
import com.warmpot.android.stackoverflow.screen.user.mapper.UserMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class QuestionMapper : AsyncMapper<QuestionSchema, Question> {

    private val ownerMapper by lazy { UserMapper() }

    override suspend fun convert(src: QuestionSchema): Question = withContext(Dispatchers.IO) {
        src.run {
            Question(
                questionId = questionId,
                title = title,
                body = getHtmlContent(src.body),
                creationDate = EpochSecond(creationDate),
                lastActivityDate = EpochSecond(lastActivityDate),
                lastEditDate = EpochSecond(lastEditDate),
                link = link,
                owner = ownerMapper.convert(src.owner),
                answerCount = answerCount,
                commentCount = commentCount,
                score = score,
                upvoteCount = upvoteCount,
                viewCount = viewCount,
                tags = src.tags
            )
        }
    }

    private fun getHtmlContent(body: String?): String {
        return body?.let { s ->
            StackoverflowHtmlParser.parseQuestionBodyHtml(s)
        } ?: ""
    }
}
