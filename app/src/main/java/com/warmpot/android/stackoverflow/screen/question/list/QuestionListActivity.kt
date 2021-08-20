package com.warmpot.android.stackoverflow.screen.question.list

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import com.warmpot.android.stackoverflow.R
import com.warmpot.android.stackoverflow.common.OneOf
import com.warmpot.android.stackoverflow.common.onError
import com.warmpot.android.stackoverflow.common.onSuccess
import com.warmpot.android.stackoverflow.common.tryOneOf
import com.warmpot.android.stackoverflow.data.schema.QuestionsResponse
import com.warmpot.android.stackoverflow.databinding.ActivityQuestionListBinding
import com.warmpot.android.stackoverflow.network.PageOptions
import com.warmpot.android.stackoverflow.network.StackoverflowApi
import com.warmpot.android.stackoverflow.screen.common.adapter.ListItem
import com.warmpot.android.stackoverflow.screen.common.recyclerview.LoadMoreListener
import com.warmpot.android.stackoverflow.screen.common.recyclerview.onLoadMore
import com.warmpot.android.stackoverflow.utils.hide
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
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

        listItems.clear()
        loadFirstPageQuestions()
    }

    private lateinit var loadMoreListener: LoadMoreListener
    private var hasNoMoreData = false
    private val listItems = arrayListOf<ListItem>()
    private fun setupViews() {
        binding.apply {
            questionRcv.adapter = questionAdapter
            questionRcv.addItemDecoration(DividerItemDecoration(this@QuestionListActivity, DividerItemDecoration.VERTICAL))
            loadMoreListener = questionRcv.onLoadMore {
                if (hasNoMoreData) return@onLoadMore
                showLoadMoreLoading()
                loadMore()
            }

            questionAdapter.onRetryClicked {
                showLoadMoreLoading()
                loadMore()
            }

            swipeRefresh.setOnRefreshListener {
                pullToRefresh()
            }
        }
    }

    private fun pullToRefresh() {
        binding.swipeRefresh.isRefreshing = true
        loadFirstPageQuestions()
    }

    private val okHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor())
            .build()
    }

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.stackexchange.com/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private var currentPageNo = 0
    private val stackOverflowApi: StackoverflowApi by lazy { retrofit.create() }

    private fun loadFirstPageQuestions() {
        currentPageNo = 1
        loadMore()
    }

    private fun loadMore(pageNo: Int = currentPageNo) {
        if (hasNoMoreData) return

        lifecycleScope.launch {

            val response = getQuestions(PageOptions(page = pageNo, pagesize = 20))
            response.onError { th ->
                showLoadQuestionsError(th)
                loadMoreDone()
                return@onError
            }

            response.onSuccess { res ->
                if (res.items.isEmpty()) {
                    hasNoMoreData = false
                    loadMoreDone()
                    showNoMoreDataItem()
                    return@onSuccess
                }

                if (!res.hasMore) {
                    hasNoMoreData = true
                }

                val questions = res.items.map { questionSchema ->
                    val owner = questionSchema.owner
                    Question(
                        questionId = questionSchema.questionId,
                        title = questionSchema.title,
                        creationDate = questionSchema.creationDate,
                        lastActivityDate = questionSchema.lastActivityDate,
                        lastEditDate = questionSchema.lastEditDate,
                        link = questionSchema.link,
                        owner = Owner(
                            userId = owner?.userId ?: 0,
                            acceptRate = owner?.acceptRate ?: 0,
                            accountId = owner?.accountId ?: 0,
                            displayName = owner?.displayName ?: "",
                            link = owner?.link ?: "",
                            profileImage = owner?.profileImage ?: "",
                            reputation = owner?.reputation ?: 0,
                            userType = owner?.userType ?: ""
                        ),
                        answerCount = questionSchema.answerCount,
                        score = questionSchema.score,
                        upvoteCount = questionSchema.upvoteCount,
                        viewCount = questionSchema.viewCount,
                    )
                }

                listItems.addAll(questions)
                questionAdapter.submitList(listItems)

                currentPageNo++
                loadMoreDone()
            }
        }
    }

    private fun showLoadMoreLoading() {
        questionAdapter.submitList(listItems.plus(LoadingState(isLoading = true)))
    }

    private fun showLoadQuestionsError(th: Throwable) {
        val strId = when (th) {
            is UnknownHostException -> R.string.error_no_connectivity
            else -> R.string.error_network_unavailable
        }

        questionAdapter.submitList(listItems.plus(LoadingState(message = getString(strId), isRetry = true)))
    }

    private fun showNoMoreDataItem() {
        questionAdapter.submitList(listItems.plus(LoadingState(message = getString(R.string.message_no_more_items))))
    }

    private fun loadMoreDone() {
        binding.loadingBar.hide()
        binding.swipeRefresh.isRefreshing = false
        loadMoreListener.loadMoreDone()
    }

    private suspend fun getQuestions(options: PageOptions = PageOptions(page = 1, pagesize = 20)): OneOf<QuestionsResponse> =
        withContext(Dispatchers.IO) {
            tryOneOf {
                stackOverflowApi.getQuestions(options)
            }
        }
}

