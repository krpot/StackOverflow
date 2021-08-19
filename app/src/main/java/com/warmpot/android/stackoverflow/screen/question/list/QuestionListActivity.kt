package com.warmpot.android.stackoverflow.screen.question.list

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.warmpot.android.stackoverflow.data.schema.QuestionSchema
import com.warmpot.android.stackoverflow.data.schema.QuestionsResponse
import com.warmpot.android.stackoverflow.databinding.ActivityQuestionListBinding
import com.warmpot.android.stackoverflow.network.PageOptions
import com.warmpot.android.stackoverflow.network.StackoverflowApi
import com.warmpot.android.stackoverflow.utils.hide
import com.warmpot.android.stackoverflow.utils.show
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

        adapterItems.clear()
        loadQuestions()
    }

    private var hasNoMoreData = false
    private var isLoadMoreInProgress = false
    private val adapterItems = arrayListOf<QuestionSchema>()
    private fun setupViews() {
        binding.apply {
            questionRcv.adapter = questionAdapter
            questionRcv.addItemDecoration(DividerItemDecoration(this@QuestionListActivity, DividerItemDecoration.VERTICAL))
            questionRcv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    if (questionRcv.layoutManager is LinearLayoutManager) {
                        val linearLayoutManager = questionRcv.layoutManager as LinearLayoutManager
                        val reachedBottom =
                            linearLayoutManager.findLastVisibleItemPosition() == questionAdapter.itemCount - 1
                        if (!hasNoMoreData && reachedBottom && !isLoadMoreInProgress) {
                            println("###### load more!!!")
                            isLoadMoreInProgress = true
                            loadMore()
                        }
                    }
                }
            })
        }
    }

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.stackexchange.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private var currentPageNo = 0;
    private val stackOverflowApi: StackoverflowApi by lazy { retrofit.create() }

    private fun loadQuestions() {
        currentPageNo = 1
        loadMore()
    }

    private fun loadMore(pageNo: Int = currentPageNo) {
        if (hasNoMoreData) return

        lifecycleScope.launch {

            binding.loadingBar.show()

            val response: QuestionsResponse = getQuestions(PageOptions(page = pageNo, pagesize = 20))
            if (response.items.isEmpty()) {
                hasNoMoreData = false
                hideLoadMore()
                return@launch
            }

            if (!response.hasMore) {
                hasNoMoreData = true
            }

            adapterItems.addAll(response.items)
            questionAdapter.submitList(adapterItems)

            currentPageNo++
            hideLoadMore()
        }
    }

    private fun hideLoadMore() {
        binding.loadingBar.hide()
        isLoadMoreInProgress = false
    }

    private suspend fun getQuestions(options: PageOptions = PageOptions(page = 1, pagesize = 20)): QuestionsResponse =
        withContext(Dispatchers.IO) {
            stackOverflowApi.getQuestions(options)
        }
}

