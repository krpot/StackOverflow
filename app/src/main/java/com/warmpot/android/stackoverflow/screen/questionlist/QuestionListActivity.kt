package com.warmpot.android.stackoverflow.screen.questionlist

import android.os.Bundle
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.warmpot.android.stackoverflow.R
import com.warmpot.android.stackoverflow.data.api.QuestionsApi
import com.warmpot.android.stackoverflow.screen.utils.hide
import com.warmpot.android.stackoverflow.screen.utils.nonSyncLazy
import com.warmpot.android.stackoverflow.screen.utils.setup
import com.warmpot.android.stackoverflow.screen.utils.show
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class QuestionListActivity : AppCompatActivity() {

    companion object {
        private const val BASE_URL = "http://api.stackexchange.com/2.2/"
    }

    private val retrofit: Retrofit by nonSyncLazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private val questionsApi: QuestionsApi by nonSyncLazy {
        retrofit.create(QuestionsApi::class.java)
    }

    private val questionListRcv: RecyclerView by nonSyncLazy { findViewById(R.id.questionListRcv) }
    private val progressBar: ProgressBar by nonSyncLazy { findViewById(R.id.progressBar) }

    private val questionListAdapter = QuestionListAdapter()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question_list)

        updateTitle()
        setupQuestionList()
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        fetchQuestionList()
    }

    private fun setupQuestionList() {
        questionListRcv.setup(
            recyclerAdapter = questionListAdapter,
            showDivider = true
        )
    }

    private fun updateTitle(questionSize: Int = 0) {
        title = getString(R.string.question_list_title, questionSize)
    }

    private fun fetchQuestionList() {
        progressBar.show()
        lifecycleScope.launchFetchQuestionList()
    }

    private fun CoroutineScope.launchFetchQuestionList() = launch {
        val questionListResponse = questionsApi.getLastActiveQuestions()
        questionListAdapter.submitList(questionListResponse.questions)
        updateTitle(questionListResponse.questions.size)

        progressBar.hide()
    }
}
