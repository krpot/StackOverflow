package com.warmpot.android.stackoverflow.screen.question.list

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import com.warmpot.android.stackoverflow.data.schema.QuestionsResponse
import com.warmpot.android.stackoverflow.databinding.ActivityQuestionListBinding
import com.warmpot.android.stackoverflow.network.StackoverflowApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.util.*

class QuestionListActivity : AppCompatActivity() {

    private val binding by lazy { ActivityQuestionListBinding.inflate(layoutInflater) }

    private val questionAdapter by lazy { QuestionAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupViews()
        loadQuestions()
    }

    private fun setupViews() {
        binding.apply {
            questionRcv.adapter = questionAdapter
            questionRcv.addItemDecoration(DividerItemDecoration(this@QuestionListActivity, DividerItemDecoration.VERTICAL))
        }
    }

    private lateinit var stackOverflowApi: StackoverflowApi

    private fun loadQuestions() {
        binding.loadingBar.isVisible = true

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.stackexchange.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        stackOverflowApi = retrofit.create()

        lifecycleScope.launch {
            val response: QuestionsResponse = getQuestions()
            questionAdapter.submitList(response.items)

            binding.loadingBar.isVisible = false
        }
    }

    private suspend fun getQuestions(): QuestionsResponse = withContext(Dispatchers.IO) {
        stackOverflowApi.getQuestions()
    }
}

