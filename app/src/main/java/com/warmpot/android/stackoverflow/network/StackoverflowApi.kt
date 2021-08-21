package com.warmpot.android.stackoverflow.network

import com.warmpot.android.stackoverflow.data.schema.QuestionsResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface StackoverflowApi {

    @GET("questions?key=${STACKOVERFLOW_API_KEY}&order=desc&sort=activity&site=stackoverflow")
    suspend fun getQuestions(@Query("page") page: Int): QuestionsResponse

    @GET("questions/{ids}?key=${STACKOVERFLOW_API_KEY}&order=desc&sort=activity&site=stackoverflow&filter=withbody")
    suspend fun getQuestion(@Path("ids") id: Int): QuestionsResponse

}
