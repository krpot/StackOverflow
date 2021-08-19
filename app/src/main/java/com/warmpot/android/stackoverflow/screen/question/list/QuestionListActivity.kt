package com.warmpot.android.stackoverflow.screen.question.list

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.warmpot.android.stackoverflow.R
import com.warmpot.android.stackoverflow.common.OneOf
import com.warmpot.android.stackoverflow.common.onError
import com.warmpot.android.stackoverflow.common.onSuccess
import com.warmpot.android.stackoverflow.common.tryOneOf
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
import java.net.UnknownHostException
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

            val response = getQuestions(PageOptions(page = pageNo, pagesize = 20))
            response.onError { th ->
                showLoadQuestionsError(th)
                hideLoadMore()
                return@onError
            }

            response.onSuccess { res ->
                if (res.items.isEmpty()) {
                    hasNoMoreData = false
                    hideLoadMore()
                    return@onSuccess
                }

                if (!res.hasMore) {
                    hasNoMoreData = true
                }

                adapterItems.addAll(res.items)
                questionAdapter.submitList(adapterItems)

                currentPageNo++
                hideLoadMore()
            }
        }
    }

    private fun showLoadQuestionsError(th: Throwable) {
        val strId = when (th) {
            is UnknownHostException -> R.string.error_no_connectivity
            else -> R.string.error_network_unavailable
        }
        Toast.makeText(this, strId, Toast.LENGTH_SHORT).show()
    }

    private fun hideLoadMore() {
        binding.loadingBar.hide()
        isLoadMoreInProgress = false
    }

    private suspend fun getQuestions(options: PageOptions = PageOptions(page = 1, pagesize = 20)): OneOf<QuestionsResponse> =
        withContext(Dispatchers.IO) {
            tryOneOf {
                stackOverflowApi.getQuestions(options)
            }
        }
}

