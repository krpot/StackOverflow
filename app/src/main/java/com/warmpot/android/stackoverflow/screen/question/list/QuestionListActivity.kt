package com.warmpot.android.stackoverflow.screen.question.list

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.warmpot.android.stackoverflow.databinding.ActivityQuestionListBinding
import com.warmpot.android.stackoverflow.screen.common.recyclerview.LoadMoreListener
import com.warmpot.android.stackoverflow.screen.common.recyclerview.RecyclerViewHelper
import com.warmpot.android.stackoverflow.screen.question.list.adapter.QuestionAdapter
import com.warmpot.android.stackoverflow.screen.question.list.viewmodel.QuestionListViewModel
import com.warmpot.android.stackoverflow.utils.RecyclerViewDivider
import com.warmpot.android.stackoverflow.utils.hide
import com.warmpot.android.stackoverflow.utils.viewModel

class QuestionListActivity : AppCompatActivity() {

    private val viewModel by viewModel<QuestionListViewModel>()

    private val binding by lazy { ActivityQuestionListBinding.inflate(layoutInflater) }

    private val questionAdapter by lazy { QuestionAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupViews()
        setupViewModel()

        loadFirstPageQuestions()
    }

    private lateinit var loadMoreListener: LoadMoreListener

    private fun setupViews() {
        binding.apply {
            RecyclerViewHelper(
                recyclerView = questionRcv,
                adapter = questionAdapter,
                divider = RecyclerViewDivider.Vertical,
                onLoadMore = ::triggerLoadMore
            ).also { helper ->
                loadMoreListener = requireNotNull(helper.loadMoreListener)
            }

            questionAdapter.onRetryClicked {
               viewModel.retryClicked()
            }

            swipeRefresh.setOnRefreshListener {
                pullToRefresh()
            }
        }
    }

    private fun pullToRefresh() {
        binding.swipeRefresh.isRefreshing = true
        viewModel.pullToRefresh()
    }

    private fun loadFirstPageQuestions() {
        viewModel.loadFirstPageQuestions()
    }

    private fun triggerLoadMore() {
        viewModel.triggerLoadMore()
    }

    private fun loadMoreDone() {
        // TODO : Need to be fixed to show loading bar when initially load data
        binding.loadingBar.hide()
        binding.swipeRefresh.isRefreshing = false
        loadMoreListener.loadMoreDone()
    }

    private fun setupViewModel() {
        viewModel.listItemsLiveData.observe(this) { listItems ->
            questionAdapter.submitList(listItems)
            loadMoreDone()
        }
    }
}
