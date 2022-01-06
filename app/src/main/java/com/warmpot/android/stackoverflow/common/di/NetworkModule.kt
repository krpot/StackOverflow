package com.warmpot.android.stackoverflow.common.di

import com.warmpot.android.stackoverflow.data.qustions.datasource.QuestionDataSource
import com.warmpot.android.stackoverflow.data.qustions.datasource.QuestionRemoteDataSource
import com.warmpot.android.stackoverflow.data.tags.datasource.TagDataSource
import com.warmpot.android.stackoverflow.data.tags.datasource.TagRemoteDataSource
import com.warmpot.android.stackoverflow.data.users.UserDataSource
import com.warmpot.android.stackoverflow.data.users.UserRemoteDataSource
import com.warmpot.android.stackoverflow.network.STACKOVERFLOW_BASE_URL
import com.warmpot.android.stackoverflow.network.StackoverflowApi
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

internal object NetworkModule {

    val questionDataSource: QuestionDataSource by lazy { QuestionRemoteDataSource(stackOverflowApi) }

    val tagDataSource: TagDataSource by lazy { TagRemoteDataSource(stackOverflowApi) }

    val userDataSource: UserDataSource by lazy { UserRemoteDataSource(stackOverflowApi) }

    private val stackOverflowApi: StackoverflowApi by lazy { retrofit.create() }

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(STACKOVERFLOW_BASE_URL)
            .client(provideOkHttpClient())
            .addConverterFactory(provideGsonFactory())
            .build()
    }

    private fun provideGsonFactory(): GsonConverterFactory {
        return GsonConverterFactory.create()
    }

    private fun provideHttpLoginInterceptor(): Interceptor {
        return HttpLoggingInterceptor().also {
            it.level = HttpLoggingInterceptor.Level.BODY
        }
    }

    private fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(provideHttpLoginInterceptor())
            .build()
    }
}
