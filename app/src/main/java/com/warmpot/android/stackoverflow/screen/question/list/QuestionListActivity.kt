package com.warmpot.android.stackoverflow.screen.question.list

import android.app.SearchManager
import android.content.ComponentName
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.widget.SearchView
import androidx.core.content.getSystemService
import com.warmpot.android.stackoverflow.R
import com.warmpot.android.stackoverflow.common.di.DependencyInjector
import com.warmpot.android.stackoverflow.screen.common.base.BaseActivity
import com.warmpot.android.stackoverflow.screen.common.dialog.DialogArg
import com.warmpot.android.stackoverflow.screen.common.dialog.DialogListener
import com.warmpot.android.stackoverflow.screen.common.dialog.DialogResult
import com.warmpot.android.stackoverflow.screen.common.exception.toUiMessage
import com.warmpot.android.stackoverflow.screen.common.resource.DialogRes
import com.warmpot.android.stackoverflow.screen.question.list.viewmodel.QuestionListViewModel
import com.warmpot.android.stackoverflow.screen.question.model.Question
import com.warmpot.android.stackoverflow.screen.question.search.SearchActivity
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
        menuInflater.inflate(R.menu.menu_search, menu)
        val searchView = menu?.findItem(R.id.menu_search)?.actionView as SearchView
        val searchManager: SearchManager = getSystemService() ?: return true
        searchView.setSearchableInfo(
            searchManager.getSearchableInfo(
                ComponentName(
                    this,
                    SearchActivity::class.java
                )
            )
        )
        //searchView.queryHint = resources.getString(R.string.search_hint)
        return true
    }

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        if (item.itemId == R.id.menu_search) {
//            navigator.goToSearchScreen()
//            return true
//        }
//        return super.onOptionsItemSelected(item)
//    }

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
