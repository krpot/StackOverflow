package com.warmpot.android.stackoverflow.data

import retrofit2.http.GET

interface QuestionsApi {

    @GET("/questions?filter=withbody&site=stackoverflow&sort=activity")
    suspend fun getLastActiveQuestions(): QuestionListResponse

}