package com.warmpot.android.stackoverflow.network

import com.warmpot.android.stackoverflow.data.schema.QuestionsResponse
import retrofit2.http.GET
import retrofit2.http.Query

const val API_URL_VERSION = "2.3"

interface StackoverflowApi {

    companion object {
        const val STACKOVERFLOW_API_KEY = "EbYV9Y5gGrC1CeVZs7opzA(("
    }

    @GET("/$API_URL_VERSION/questions?key=${STACKOVERFLOW_API_KEY}&order=desc&sort=activity&site=stackoverflow")
    suspend fun getQuestions(@Query("page") page: Int): QuestionsResponse
}
