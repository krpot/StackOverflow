package com.warmpot.android.stackoverflow.screen.question.list

import android.content.Context
import android.database.MatrixCursor
import android.provider.BaseColumns
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.cursoradapter.widget.CursorAdapter
import androidx.cursoradapter.widget.SimpleCursorAdapter
import androidx.recyclerview.widget.ConcatAdapter
import com.warmpot.android.stackoverflow.R
import com.warmpot.android.stackoverflow.databinding.ActivityQuestionListBinding
import com.warmpot.android.stackoverflow.screen.common.adapter.LoadingState
import com.warmpot.android.stackoverflow.screen.common.adapter.LoadingStateAdapter
import com.warmpot.android.stackoverflow.screen.common.listener.FastClickHandler
import com.warmpot.android.stackoverflow.screen.common.recyclerview.LoadMoreListener
import com.warmpot.android.stackoverflow.screen.common.recyclerview.RecyclerViewHelper
import com.warmpot.android.stackoverflow.screen.question.list.adapter.QuestionAdapter
import com.warmpot.android.stackoverflow.screen.question.list.adapter.QuestionViewHolder
import com.warmpot.android.stackoverflow.screen.question.list.viewmodel.QuestionListUiState
import com.warmpot.android.stackoverflow.screen.question.list.viewmodel.TagUiState
import com.warmpot.android.stackoverflow.screen.question.model.Question
import com.warmpot.android.stackoverflow.screen.question.search.SearchActivity
import com.warmpot.android.stackoverflow.screen.user.model.User
import com.warmpot.android.stackoverflow.utils.*

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
    companion object {
        private const val SUGGESTION_COLUMN_NAME = "tag"
        private const val SUGGESTION_COLUMN_INDEX = 1
    }

    val root: View get() = binding.root

    private val context: Context get() = root.context

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

    fun createOptionsMenu(
        menuInflater: MenuInflater,
        menu: Menu?,
        onQueryTextChange: (String) -> Unit
    ): Boolean {
        menuInflater.inflate(R.menu.menu_search, menu)
        val searchView = menu?.findItem(R.id.menu_search)?.actionView as SearchView

        searchView.setupAutoCompleteTextView()
        searchView.setupSearchableInfo<SearchActivity>()

        searchView.suggestionsAdapter = suggestionAdapter
        searchView.onQueryTextChange { query ->
            onQueryTextChange(query)
        }

        searchView.onSuggestClick { cursor, _ ->
            cursor.getString(SUGGESTION_COLUMN_INDEX)
        }

        return true
    }

    private fun loadMoreDone() {
        binding.apply {
            loadingBar.hide()
            swipeRefresh.isRefreshing = false
        }
        loadMoreListener.loadMoreDone()
    }

    fun handleUiState(uiState: QuestionListUiState) {
        uiState.screenLoading.value()?.also(::bindScreenLoading)
        uiState.loading.value()?.also(::bindLoading)
        uiState.error?.value()?.also(onShowError)
        uiState.questions?.also(::bindQuestions)
        uiState.tags?.also(::bindTags)
    }

    private fun bindScreenLoading(showing: Boolean) {
        loadingStateAdapter.submitList(emptyList())
        binding.loadingBar.isVisible = showing
    }

    private fun bindQuestions(questions: List<Question>?) {
        questionAdapter.submitList(questions) {
            loadMoreDone()
        }
    }

    private fun bindLoading(loadingStates: List<LoadingState>?) {
        loadMoreDone()
        loadingStateAdapter.submitList(loadingStates)
    }

    val suggestionAdapter: CursorAdapter by lazy {
        val from = arrayOf(SUGGESTION_COLUMN_NAME)
        val to = intArrayOf(android.R.id.text1)
        SimpleCursorAdapter(
            root.context,
            android.R.layout.simple_list_item_1,
            null,
            from,
            to,
            CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER
        )
    }

    private fun bindTags(uiState: TagUiState) {
        val cursor = MatrixCursor(arrayOf(BaseColumns._ID, SUGGESTION_COLUMN_NAME))
        uiState.tags.forEachIndexed { index, tag ->
            if (tag.name.lowercase().startsWith(uiState.query.lowercase())) {
                cursor.addRow(arrayOf(index, tag.name))
            }
        }
        suggestionAdapter.changeCursor(cursor)
    }
}