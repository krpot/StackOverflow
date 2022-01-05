package com.warmpot.android.stackoverflow.screen.question.list

import android.app.SearchManager
import android.content.ComponentName
import android.database.MatrixCursor
import android.os.Bundle
import android.provider.BaseColumns
import android.view.Menu
import androidx.appcompat.widget.SearchView
import androidx.core.content.getSystemService
import androidx.cursoradapter.widget.CursorAdapter
import androidx.cursoradapter.widget.SimpleCursorAdapter
import com.warmpot.android.stackoverflow.R
import com.warmpot.android.stackoverflow.common.di.DependencyInjector
import com.warmpot.android.stackoverflow.screen.common.base.BaseActivity
import com.warmpot.android.stackoverflow.screen.common.base.context
import com.warmpot.android.stackoverflow.screen.common.dialog.DialogArg
import com.warmpot.android.stackoverflow.screen.common.dialog.DialogListener
import com.warmpot.android.stackoverflow.screen.common.dialog.DialogResult
import com.warmpot.android.stackoverflow.screen.common.exception.toUiMessage
import com.warmpot.android.stackoverflow.screen.common.resource.DialogRes
import com.warmpot.android.stackoverflow.screen.question.list.viewmodel.QuestionListViewModel
import com.warmpot.android.stackoverflow.screen.question.model.Question
import com.warmpot.android.stackoverflow.screen.question.search.SearchActivity
import com.warmpot.android.stackoverflow.screen.user.model.User
import com.warmpot.android.stackoverflow.utils.setupAutoCompleteTextView


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

    // TODO : Use tags from api
    private val SUGGESTIONS = arrayOf(
        "Bauru", "Sao Paulo", "Rio de Janeiro",
        "Bahia", "Mato Grosso", "Minas Gerais",
        "Tocantins", "Rio Grande do Sul"
    )

    private val mAdapter by lazy {
        val from = arrayOf("cityName")
        val to = intArrayOf(android.R.id.text1)
        SimpleCursorAdapter(
            context,
            android.R.layout.simple_list_item_1,
            null,
            from,
            to,
            CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER
        )
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_search, menu)
        val searchView = menu?.findItem(R.id.menu_search)?.actionView as SearchView
        val searchManager: SearchManager = getSystemService() ?: return true

        searchView.setupAutoCompleteTextView()
        searchView.setSearchableInfo(
            searchManager.getSearchableInfo(
                ComponentName(
                    this,
                    SearchActivity::class.java
                )
            )
        )

        searchView.suggestionsAdapter = mAdapter
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(s: String): Boolean {
                return false
            }

            override fun onQueryTextChange(s: String): Boolean {
                populateAdapter(s)
                return false
            }
        })
        return true
    }

    private fun populateAdapter(query: String) {
        val cursor = MatrixCursor(arrayOf(BaseColumns._ID, "cityName"))
        for (i in SUGGESTIONS.indices) {
            if (SUGGESTIONS[i].lowercase().startsWith(query.lowercase())) {
                cursor.addRow(arrayOf<Any>(i, SUGGESTIONS[i]))
            }
        }
        mAdapter.changeCursor(cursor)
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
