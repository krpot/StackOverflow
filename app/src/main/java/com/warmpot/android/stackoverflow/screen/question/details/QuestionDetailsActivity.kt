package com.warmpot.android.stackoverflow.screen.question.details

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MenuItem
import com.warmpot.android.stackoverflow.R
import com.warmpot.android.stackoverflow.domain.model.QuestionId
import com.warmpot.android.stackoverflow.screen.common.base.BaseActivity
import com.warmpot.android.stackoverflow.screen.common.constants.IntentConstant
import com.warmpot.android.stackoverflow.screen.common.dialog.InfoDialogArg
import com.warmpot.android.stackoverflow.screen.common.resource.Str
import com.warmpot.android.stackoverflow.screen.common.resource.text
import com.warmpot.android.stackoverflow.screen.question.details.viewmodel.QuestionDetailsUiState
import com.warmpot.android.stackoverflow.screen.question.details.viewmodel.QuestionDetailsViewModel
import com.warmpot.android.stackoverflow.screen.question.model.Question
import com.warmpot.android.stackoverflow.utils.viewModel


class QuestionDetailsActivity : BaseActivity() {

    private val viewModel by viewModel<QuestionDetailsViewModel>()

    private val binding: QuestionDetailsActivityBinder by lazy {
        QuestionDetailsActivityBinder(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        binding.setupTabLayout()
    }

    private val question: Question by lazy {
        intent.getSerializableExtra(IntentConstant.EXTRA_QUESTION) as Question
    }

    private fun loadQuestion() {
        viewModel.fetch(QuestionId(question.questionId))
    }

    private fun observeUiState() {
        viewModel.uiState.observe(this, ::handleUiState)
    }

    private fun handleUiState(uiState: QuestionDetailsUiState) {
        uiState.error?.also(::showError)
        uiState.question?.also(::bindQuestion)
    }

    private fun showError(error: Str) {
        navigator.showInfoDialog(
            InfoDialogArg(
                title = getString(R.string.data_load_error_title),
                message = error.text(context)
            )
        )
    }

    private fun bindQuestion(question: Question) {
        binding.bindQuestion(question) {
            navigator.goToUserScreen(question.owner)
        }
    }
}
