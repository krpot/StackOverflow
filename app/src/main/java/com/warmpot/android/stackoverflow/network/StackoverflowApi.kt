package com.warmpot.android.stackoverflow.network

import com.warmpot.android.stackoverflow.data.schema.QuestionsResponse
import retrofit2.http.GET

interface StackoverflowApi {

    @GET("questions")
    suspend fun getQuestions(): QuestionsResponse

}
