package com.warmpot.android.stackoverflow.screen.question.search

import android.app.SearchManager
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import com.warmpot.android.stackoverflow.common.di.DependencyInjector
import com.warmpot.android.stackoverflow.screen.common.base.BaseActivity
import com.warmpot.android.stackoverflow.screen.common.dialog.DialogArg
import com.warmpot.android.stackoverflow.screen.common.dialog.DialogListener
import com.warmpot.android.stackoverflow.screen.common.dialog.DialogResult
import com.warmpot.android.stackoverflow.screen.common.exception.toUiMessage
import com.warmpot.android.stackoverflow.screen.common.resource.DialogRes
import com.warmpot.android.stackoverflow.screen.question.list.QuestionListActivityBinder
import com.warmpot.android.stackoverflow.screen.question.model.Question
import com.warmpot.android.stackoverflow.screen.question.search.viewmodel.SearchViewModel
import com.warmpot.android.stackoverflow.screen.user.model.User

class SearchActivity : BaseActivity(), DialogListener {

    lateinit var viewModel: SearchViewModel

    private val binding: QuestionListActivityBinder by lazy {
        QuestionListActivityBinder(
            layoutInflater = layoutInflater,
            onPullToRefresh = viewModel::pullToRefresh,
            onTriggerLoadMore = viewModel::triggerLoadMore,
            onLoadMoreRetryClicked = viewModel::loadMoreRetryClicked,
            onTagClicked = { },
            onQuestionClicked = ::questionClicked,
            onOwnerClicked = ::ownerClicked,
            onShowError = ::showError
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DependencyInjector.inject(this)

        setContentView(binding.root)

        setupViews()
        observeUiState()

        // Verify the action and get the query
        if (Intent.ACTION_SEARCH == intent.action) {
            intent.getStringExtra(SearchManager.QUERY)?.also { query ->
                supportActionBar?.title = query
                viewModel.searchQueryChanged(query)
            }
        }
    }

//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        menuInflater.inflate(R.menu.menu_search, menu)
//        return super.onCreateOptionsMenu(menu)
//    }

//    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
//        val searchView = menu?.findItem(R.id.menu_search)?.actionView as? SearchView
//        searchView?.suggestionsAdapter = object: CursorAdapter(context) {
//            override fun newView(context: Context?, cursor: Cursor?, parent: ViewGroup?): View {
//                return layoutInflater.inflate(android.R.layout.simple_list_item_1, parent, false)
//            }
//
//            override fun bindView(view: View?, context: Context?, cursor: Cursor?) {
//
//            }
//        }
//
//        searchView?.suggestionsAdapter?.setFilterQueryProvider {
//
//        }
//
//        searchView?.setOnSearchClickListener {
//
//        }
//
//        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
//            override fun onQueryTextSubmit(query: String?): Boolean {
//                return false
//            }
//
//            override fun onQueryTextChange(newText: String?): Boolean {
//                viewModel.searchQueryChanged(newText)
//                return true
//            }
//        })
//
//        searchView?.setOnCloseListener {
//            true
//        }
//
//        searchView?.isIconified = false
//
//        return super.onPrepareOptionsMenu(menu)
//    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupViews() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.setupViews()
    }

    private fun observeUiState() {
        viewModel.uiState.observe(this, binding::handleUiState)
    }

    private fun ownerClicked(user: User) {
        navigator.goToUserScreen(user)
    }

    private fun questionClicked(question: Question) {
        navigator.goToDetailsScreen(question)
    }

    private fun showError(throwable: Throwable) {
        dialogHelper.showInfoDialog(
            DialogArg.Info(
                title = DialogRes.defaultErrorTitle,
                message = throwable.toUiMessage()
            )
        )
    }

    override fun onDialogCompleted(result: DialogResult) {
    }
}
