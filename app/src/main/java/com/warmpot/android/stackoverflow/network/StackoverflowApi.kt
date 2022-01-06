package com.warmpot.android.stackoverflow.network

import com.warmpot.android.stackoverflow.data.answers.AnswersResponse
import com.warmpot.android.stackoverflow.data.qustions.schema.QuestionsResponse
import com.warmpot.android.stackoverflow.data.tags.schema.TagsResponse
import com.warmpot.android.stackoverflow.data.users.UserResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface StackoverflowApi {

    @GET("questions?key=${STACKOVERFLOW_API_KEY}&order=desc&sort=activity&site=stackoverflow")
    suspend fun getQuestions(@Query("page") page: Int): QuestionsResponse

    @GET("search?key=${STACKOVERFLOW_API_KEY}&site=stackoverflow")
    suspend fun searchQuestions(@QueryMap queryMap: Map<String, String>): QuestionsResponse

    @GET("questions/{ids}?key=${STACKOVERFLOW_API_KEY}&order=desc&sort=activity&site=stackoverflow&filter=withbody")
    suspend fun getQuestion(@Path("ids") id: Int): QuestionsResponse

    @GET("questions/{ids}/answers?key=${STACKOVERFLOW_API_KEY}&order=desc&sort=activity&site=stackoverflow&filter=!6VvPDzQn3R(Hu")
    suspend fun getQuestionAnswers(@Path("ids") id: Int): AnswersResponse

    @GET("users/{ids}?key=${STACKOVERFLOW_API_KEY}&order=desc&sort=reputation&site=stackoverflow&filter=!6VvPDzOWTBANI")
    suspend fun getUser(@Path("ids") id: Int): UserResponse

    @GET("tags/?key=${STACKOVERFLOW_API_KEY}&order=desc&sort=popular&site=stackoverflow")
    suspend fun getTags(@Query("inname") query: String): TagsResponse
}
