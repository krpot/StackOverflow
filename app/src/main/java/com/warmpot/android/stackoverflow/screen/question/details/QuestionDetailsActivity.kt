package com.warmpot.android.stackoverflow.screen.question.details

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
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


    @SuppressLint("SetJavaScriptEnabled")
    private fun setupViews() {
        binding.apply {
            webView.settings.javaScriptEnabled = true
            webView.webChromeClient = object : WebChromeClient() {
                override fun onProgressChanged(view: WebView?, newProgress: Int) {
                    if (newProgress >= 90) {
                        loadingBar.isVisible = false
                    }
                }
            }
            //webView.setPadding(0, 0, 0, 0);
            //webView.setInitialScale(getScale());
        }
    }

    private fun loadQuestion() {
        val question: Question = intent.getSerializableExtra(IntentConstant.INTENT_PARAM_KEY) as Question
        viewModel.fetch(question.questionId)
    }

    private fun setupViewModel() {
        viewModel.question.observe(this) { question ->
            binding.titleTxt.text = question.title.toHtml()
            binding.webView.loadDataWithBaseURL(null, question.body, "text/html", "utf-8", null)
        }

        viewModel.loading.observe(this) { visible ->

        }
    }
}
