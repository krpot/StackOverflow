package com.warmpot.android.stackoverflow.screen.question.list.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.warmpot.android.stackoverflow.R
import com.warmpot.android.stackoverflow.common.OneOf
import com.warmpot.android.stackoverflow.common.onError
import com.warmpot.android.stackoverflow.common.onSuccess
import com.warmpot.android.stackoverflow.common.tryOneOf
import com.warmpot.android.stackoverflow.data.schema.QuestionsResponse
import com.warmpot.android.stackoverflow.network.PageOptions
import com.warmpot.android.stackoverflow.network.StackoverflowApi
import com.warmpot.android.stackoverflow.screen.common.adapter.ListItem
import com.warmpot.android.stackoverflow.screen.question.list.LoadingState
import com.warmpot.android.stackoverflow.screen.question.mapper.QuestionMapper
import com.warmpot.android.stackoverflow.screen.question.model.Question
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.net.UnknownHostException

class QuestionListViewModel : ViewModel() {

    val listItemsLiveData = MutableLiveData<List<ListItem>>(emptyList())

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

    private val stackOverflowApi: StackoverflowApi by lazy { retrofit.create() }

    private val listItems = arrayListOf<ListItem>()

    private var currentPageNo = 0
    private var hasNoMoreData = false
    private val questionMapper by lazy { QuestionMapper() }

    fun loadFirstPageQuestions() {
        currentPageNo = 1
        loadMore()
    }

    fun triggerLoadMore() {
        if (hasNoMoreData) return

        showLoadMoreLoading()
        loadMore()
    }

    fun loadMore(pageNo: Int = currentPageNo) {
        if (hasNoMoreData) return

        viewModelScope.launch {
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

                val questions = convertQuestionSchemas(res)
                submitListItem(questions)

                currentPageNo++
                loadMoreDone()
            }
        }
    }

    private fun submitListItem(questions: List<Question>) {
        listItems.addAll(questions)
        //questionAdapter.submitList(listItems)
        postListItems(listItems)
    }

    private fun convertQuestionSchemas(res: QuestionsResponse) =
        res.items.map { questionSchema ->
            questionMapper.convert(questionSchema)
        }

    private fun showLoadMoreLoading() {
        postListItems(listItems.plus(LoadingState(isLoading = true)))
    }

    private fun showLoadQuestionsError(th: Throwable) {
        val strId = when (th) {
            is UnknownHostException -> R.string.error_no_connectivity
            else -> R.string.error_network_unavailable
        }

        postListItems(listItems.plus(LoadingState(messageId = strId, isRetry = true)))
    }

    private fun showNoMoreDataItem() {
        postListItems(listItems.plus(LoadingState(messageId = R.string.message_no_more_items)))
    }

    private fun loadMoreDone() {
        //binding.loadingBar.hide()
        //binding.swipeRefresh.isRefreshing = false
        //loadMoreListener.loadMoreDone()
    }

    private suspend fun getQuestions(options: PageOptions = PageOptions(page = 1, pagesize = 20)): OneOf<QuestionsResponse> =
        withContext(Dispatchers.IO) {
            tryOneOf {
                stackOverflowApi.getQuestions(options)
            }
        }

    private fun postListItems(items: List<ListItem>) {
        listItemsLiveData.postValue(items)
    }

    fun retryClicked() {
        showLoadMoreLoading()
        loadMore()
    }

    fun pullToRefresh() {
        loadFirstPageQuestions()
    }
}
