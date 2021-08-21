package com.warmpot.android.stackoverflow.network

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object NetworkModule {

    val stackOverflowApi: StackoverflowApi by lazy { retrofit.create() }

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
        return HttpLoggingInterceptor()
    }

    private fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(provideHttpLoginInterceptor())
            .build()
    }
}
