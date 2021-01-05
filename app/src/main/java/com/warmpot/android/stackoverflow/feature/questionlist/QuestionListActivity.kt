package com.warmpot.android.stackoverflow.feature.questionlist

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.warmpot.android.stackoverflow.R
import com.warmpot.android.stackoverflow.data.QuestionsApi
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class QuestionListActivity : AppCompatActivity() {

    companion object {
        private const val BASE_URL = "http://api.stackexchange.com/2.2/"
    }

    private val retrofit: Retrofit by lazy(LazyThreadSafetyMode.NONE) {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private val questionsApi: QuestionsApi by lazy(LazyThreadSafetyMode.NONE) {
        retrofit.create(QuestionsApi::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question_list)

        setupTitle()
    }

    private fun setupTitle() {
        title = getString(R.string.question_list_title, 0)
    }

    override fun onStart() {
        super.onStart()
        fetchQuestionList()
    }

    private fun fetchQuestionList() {
        lifecycleScope.launch {
            val questionListResponse = questionsApi.getLastActiveQuestions()
            println("====== Question fetched: $questionListResponse")
        }
    }
}