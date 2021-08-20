package com.warmpot.android.stackoverflow.screen.question.mapper

import com.warmpot.android.stackoverflow.common.EpochSecond
import com.warmpot.android.stackoverflow.common.Mapper
import com.warmpot.android.stackoverflow.data.schema.QuestionSchema
import com.warmpot.android.stackoverflow.screen.question.model.Question
import com.warmpot.android.stackoverflow.screen.user.mapper.OwnerMapper

class QuestionMapper : Mapper<QuestionSchema, Question> {

    private val ownerMapper by lazy { OwnerMapper() }

    override fun convert(src: QuestionSchema): Question {
        return src.run {
            Question(
                questionId = questionId,
                title = title,
                creationDate = EpochSecond(creationDate),
                lastActivityDate = EpochSecond(lastActivityDate),
                lastEditDate = EpochSecond(lastEditDate),
                link = link,
                owner = src.owner?.let { ownerMapper.convert(it) },
                answerCount = answerCount,
                score = score,
                upvoteCount = upvoteCount,
                viewCount = viewCount,
                tags = src.tags.take(3)
            )
        }
    }
}
