package com.warmpot.android.stackoverflow.screen.question.details

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MenuItem
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.warmpot.android.stackoverflow.R
import com.warmpot.android.stackoverflow.common.DateTimeFormatType
import com.warmpot.android.stackoverflow.common.format
import com.warmpot.android.stackoverflow.databinding.ActivityQuestionDetailsBinding
import com.warmpot.android.stackoverflow.screen.common.constants.IntentConstant
import com.warmpot.android.stackoverflow.screen.question.details.viewmodel.QuestionDetailsViewModel
import com.warmpot.android.stackoverflow.screen.question.model.Question
import com.warmpot.android.stackoverflow.utils.toHtml
import com.warmpot.android.stackoverflow.utils.viewModel


class QuestionDetailsActivity : AppCompatActivity() {

    private val viewModel by viewModel<QuestionDetailsViewModel>()

    private val binding by lazy { ActivityQuestionDetailsBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupViews()
        setupViewModel()

        loadQuestion()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (backPressedByWebView()) return
        super.onBackPressed()
    }

    private fun backPressedByWebView(): Boolean {
        binding.webView.apply {
            if (canGoBack()) {
                goBack()
                return true
            }
        }

        return false
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setupViews() {
        binding.apply {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)

            webView.settings.javaScriptEnabled = true
            webView.webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    loadingBar.isVisible = false
                }
            }
        }
    }

    private fun loadQuestion() {
        val question: Question = intent.getSerializableExtra(IntentConstant.INTENT_PARAM_KEY) as Question
        viewModel.fetch(question.questionId)
    }

    private fun setupViewModel() {
        viewModel.question.observe(this) { question ->
            binding.titleTxt.text = question.title.toHtml()
            binding.answerCountTxt.text = getString(R.string.question_answer_fmt, question.answerCount)
            binding.commentCountTxt.text = getString(R.string.question_comment_fmt, question.commentCount)
            binding.createdDateTxt.text = question.creationDate.format(DateTimeFormatType.ddMMyyHHmm)
            binding.webView.loadDataWithBaseURL(null, question.body, "text/html", "utf-8", null)
            binding.ownerTxt.text = question.owner?.displayName
        }
    }
}
