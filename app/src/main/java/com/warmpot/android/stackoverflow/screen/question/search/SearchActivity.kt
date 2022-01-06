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

        handleSearchIntentAction()
    }

    // Verify the action and get the query
    private fun handleSearchIntentAction() {
        if (Intent.ACTION_SEARCH != intent.action) return

        intent.getStringExtra(SearchManager.QUERY)?.also { query ->
            binding.showScreenLoadingScreen()
            supportActionBar?.title = query
            viewModel.searchQueryChanged(query)
        }
    }

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
