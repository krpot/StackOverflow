package com.warmpot.android.stackoverflow.screen.question.list

import android.os.Bundle
import android.view.Menu
import com.warmpot.android.stackoverflow.common.di.DependencyInjector
import com.warmpot.android.stackoverflow.screen.common.base.BaseActivity
import com.warmpot.android.stackoverflow.screen.common.dialog.DialogArg
import com.warmpot.android.stackoverflow.screen.common.dialog.DialogListener
import com.warmpot.android.stackoverflow.screen.common.dialog.DialogResult
import com.warmpot.android.stackoverflow.screen.common.exception.toUiMessage
import com.warmpot.android.stackoverflow.screen.common.resource.DialogRes
import com.warmpot.android.stackoverflow.screen.question.list.viewmodel.QuestionListViewModel
import com.warmpot.android.stackoverflow.screen.question.model.Question
import com.warmpot.android.stackoverflow.screen.user.model.User


class QuestionListActivity : BaseActivity(), DialogListener {

    lateinit var viewModel: QuestionListViewModel

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

        binding.setupViews()
        observeUiState()

        loadFirstPageQuestions()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        binding.createOptionsMenu(menuInflater = menuInflater, menu = menu) { query ->
            viewModel.searchQueryChanged(query)
        }

        return true
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

    private fun loadFirstPageQuestions() {
        binding.showScreenLoadingScreen()
        viewModel.loadFirstPageQuestions()
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
