package com.warmpot.android.stackoverflow.network

import com.warmpot.android.stackoverflow.data.schema.QuestionsResponse
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap
import retrofit2.http.QueryName

const val API_URL_VERSION = "2.3"

interface StackoverflowApi {

    @GET("/$API_URL_VERSION/questions?order=desc&sort=activity&site=stackoverflow")
    suspend fun getQuestions(@QueryName options: PageOptions): QuestionsResponse
}
