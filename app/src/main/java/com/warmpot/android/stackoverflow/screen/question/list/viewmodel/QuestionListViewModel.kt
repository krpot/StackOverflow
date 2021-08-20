package com.warmpot.android.stackoverflow.screen.question.list.viewmodel

import androidx.lifecycle.LiveData
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
import com.warmpot.android.stackoverflow.screen.common.resource.Str
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

    private val listItemsLiveData = MutableLiveData<List<ListItem>>(emptyList())
    val listItems: LiveData<List<ListItem>> get() = listItemsLiveData
    private val loadingLiveData = MutableLiveData(false)
    val loading: LiveData<Boolean> get() = loadingLiveData

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

    private val questions = arrayListOf<ListItem>()

    private var currentPageNo = 0
    private var hasNoMoreData = false
    private val questionMapper by lazy { QuestionMapper() }

    // region public functions
    fun loadFirstPageQuestions() {
        loadingLiveData.postValue(true)
        currentPageNo = 1
        loadMore()
    }

    fun triggerLoadMore() {
        if (hasNoMoreData) return

        postLoadMoreLoading()
        loadMore()
    }

    fun retryClicked() {
        postLoadMoreLoading()
        loadMore()
    }

    fun pullToRefresh() {
        loadFirstPageQuestions()
    }
    // endregion public functions

    private fun loadMore(pageNo: Int = currentPageNo) {
        if (hasNoMoreData) return

        viewModelScope.launch {
            val response = getQuestions(PageOptions(page = pageNo, pagesize = 20))
            response.onError { th ->
                postLoadQuestionsError(th)
                return@onError
            }

            response.onSuccess { res ->
                if (res.items.isEmpty()) {
                    hasNoMoreData = false
                    postNoMoreDataItem()
                    return@onSuccess
                }

                if (!res.hasMore) {
                    hasNoMoreData = true
                }

                val questions = convertQuestionSchemas(res)
                postFetchedQuestions(questions)

                currentPageNo++
            }

            loadingLiveData.postValue(false)
        }
    }

    // region post functions
    private fun postFetchedQuestions(questions: List<Question>) {
        this.questions.addAll(questions)
        postListItems(this.questions)
    }

    private fun postLoadMoreLoading() {
        postListItems(questions.plus(LoadingState(isLoading = true)))
    }

    private fun postLoadQuestionsError(th: Throwable) {
        val strId = throwableToStrId(th)
        postListItems(questions.plus(LoadingState(message = Str.from(strId), isRetry = true)))
    }

    private fun postNoMoreDataItem() {
        val loadingState = LoadingState(message = Str.from(R.string.message_no_more_items))
        postListItems(questions.plus(loadingState))
    }
    // endregion post functions

    // region private helper functions
    private suspend fun getQuestions(options: PageOptions = PageOptions(page = 1, pagesize = 20)): OneOf<QuestionsResponse> =
        withContext(Dispatchers.IO) {
            tryOneOf {
                stackOverflowApi.getQuestions(options)
            }
        }

    private fun postListItems(items: List<ListItem>) {
        listItemsLiveData.postValue(items)
    }

    private fun convertQuestionSchemas(res: QuestionsResponse) =
        res.items.map { questionSchema ->
            questionMapper.convert(questionSchema)
        }

    private fun throwableToStrId(th: Throwable): Int {
        return when (th) {
            is UnknownHostException -> R.string.error_no_connectivity
            else -> R.string.error_network_unavailable
        }
    }
    // endregion private helper functions
}
