package com.warmpot.android.stackoverflow.screen.question.details

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MenuItem
import com.warmpot.android.stackoverflow.common.di.DependencyInjector
import com.warmpot.android.stackoverflow.domain.model.QuestionId
import com.warmpot.android.stackoverflow.screen.common.base.BaseActivity
import com.warmpot.android.stackoverflow.screen.common.constants.IntentConst
import com.warmpot.android.stackoverflow.screen.common.dialog.DialogArg
import com.warmpot.android.stackoverflow.screen.common.dialog.DialogListener
import com.warmpot.android.stackoverflow.screen.common.dialog.DialogResult
import com.warmpot.android.stackoverflow.screen.common.resource.DialogRes
import com.warmpot.android.stackoverflow.screen.common.resource.Str
import com.warmpot.android.stackoverflow.screen.question.details.viewmodel.QuestionDetailsViewModel
import com.warmpot.android.stackoverflow.screen.question.model.Question
import com.warmpot.android.stackoverflow.screen.user.model.User


class QuestionDetailsActivity : BaseActivity(), DialogListener {

    lateinit var viewModel: QuestionDetailsViewModel

    private val binding: QuestionDetailsActivityBinder by lazy {
        QuestionDetailsActivityBinder(
            layoutInflater = layoutInflater,
            onOwnerClicked = ::ownerClicked,
            onFetchFailed = ::showError
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DependencyInjector.inject(this)

        setContentView(binding.root)

        setupViews()
        observeUiState()

        loadQuestion()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setupViews() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.setupViews()
    }

    private val question: Question by lazy {
        intent.getSerializableExtra(IntentConst.EXTRA_QUESTION) as Question
    }

    private fun loadQuestion() {
        viewModel.fetch(QuestionId(question.questionId))
    }

    private fun observeUiState() {
        viewModel.uiState.observe(this, binding::handleUiState)
    }

    private fun showError(error: Str) {
        dialogHelper.showConfirmDialog(
            DialogArg.Confirm(
                title = DialogRes.defaultErrorTitle,
                message = error,
                positiveButtonCaption = DialogRes.retry,
            )
        )
    }

    private fun ownerClicked(owner: User) {
        navigator.goToUserScreen(owner)
    }

    override fun onDialogCompleted(result: DialogResult) {
        when (result) {
            is DialogResult.Yes -> {
                loadQuestion()
            }
            is DialogResult.No -> {}
        }
    }
}
