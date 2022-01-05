package com.warmpot.android.stackoverflow.screen.question.list

import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.ConcatAdapter
import com.warmpot.android.stackoverflow.databinding.ActivityQuestionListBinding
import com.warmpot.android.stackoverflow.screen.common.adapter.LoadingState
import com.warmpot.android.stackoverflow.screen.common.adapter.LoadingStateAdapter
import com.warmpot.android.stackoverflow.screen.common.listener.FastClickHandler
import com.warmpot.android.stackoverflow.screen.common.recyclerview.LoadMoreListener
import com.warmpot.android.stackoverflow.screen.common.recyclerview.RecyclerViewHelper
import com.warmpot.android.stackoverflow.screen.question.list.adapter.QuestionAdapter
import com.warmpot.android.stackoverflow.screen.question.list.adapter.QuestionViewHolder
import com.warmpot.android.stackoverflow.screen.question.list.viewmodel.QuestionListUiState
import com.warmpot.android.stackoverflow.screen.question.model.Question
import com.warmpot.android.stackoverflow.screen.user.model.User
import com.warmpot.android.stackoverflow.utils.RecyclerViewDivider
import com.warmpot.android.stackoverflow.utils.hide

class QuestionListActivityBinder(
    private val layoutInflater: LayoutInflater,
    private val onPullToRefresh: () -> Unit,
    private val onTriggerLoadMore: () -> Unit,
    private val onLoadMoreRetryClicked: () -> Unit,
    private val onTagClicked: (String) -> Unit,
    private val onQuestionClicked: (Question) -> Unit,
    private val onOwnerClicked: (User) -> Unit,
    private val onShowError: (Throwable) -> Unit
) {
    val root: View
        get() = binding.root

    private val self: QuestionListActivityBinder get() = this@QuestionListActivityBinder

    private val binding by lazy { ActivityQuestionListBinding.inflate(layoutInflater) }

    private val loadingStateAdapter by lazy { LoadingStateAdapter() }
    private val questionAdapter by lazy { QuestionAdapter() }
    private val concatAdapter by lazy { ConcatAdapter(questionAdapter, loadingStateAdapter) }

    private val fastClickHandler by lazy { FastClickHandler() }

    private lateinit var loadMoreListener: LoadMoreListener

    fun setupViews() {
        binding.apply {
            RecyclerViewHelper(
                recyclerView = questionRcv,
                adapter = concatAdapter,
                divider = RecyclerViewDivider.Vertical,
                onLoadMore = onTriggerLoadMore
            ).also { helper ->
                loadMoreListener = requireNotNull(helper.loadMoreListener)
            }

            setupAdapters()

            swipeRefresh.setOnRefreshListener {
                onPullToRefresh()
            }
        }
    }

    private fun setupAdapters() {
        questionAdapter.listener = object : QuestionViewHolder.Listener {
            override fun onItemClicked(question: Question) {
                fastClicked { self.onQuestionClicked(question) }
            }

            override fun onOwnerClicked(user: User) {
                fastClicked { self.onOwnerClicked(user) }
            }

            override fun onTagClicked(tag: String) {
                fastClicked { self.onTagClicked(tag) }
            }
        }

        loadingStateAdapter.onRetryClicked {
            self.onLoadMoreRetryClicked()
        }
    }

    private fun fastClicked(action: () -> Unit) {
        fastClickHandler.performClick {
            action()
        }
    }

    private fun loadMoreDone() {
        binding.apply {
            loadingBar.hide()
            swipeRefresh.isRefreshing = false
        }
        loadMoreListener.loadMoreDone()
    }

    fun handleUiState(uiState: QuestionListUiState) {
        uiState.loading?.value()?.also(::bindLoading)
        uiState.error?.value()?.also(onShowError)
        uiState.listItems?.also(::bindListItems)
    }

    private fun bindListItems(questions: List<Question>?) {
        questionAdapter.submitList(questions) {
            loadMoreDone()
        }
    }

    private fun bindLoading(loadingStates: List<LoadingState>?) {
        loadMoreDone()
        loadingStateAdapter.submitList(loadingStates)
    }
}