package com.warmpot.android.stackoverflow.data.api

import com.warmpot.android.stackoverflow.data.entity.QuestionListResponse
import retrofit2.http.GET

interface QuestionsApi {

    @GET("/questions?filter=withbody&site=stackoverflow&sort=activity")
    suspend fun getLastActiveQuestions(): QuestionListResponse

}