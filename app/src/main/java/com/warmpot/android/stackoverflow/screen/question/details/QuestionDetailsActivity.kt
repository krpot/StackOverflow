package com.warmpot.android.stackoverflow.screen.question.details

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.google.android.material.tabs.TabLayout
import com.warmpot.android.stackoverflow.R
import com.warmpot.android.stackoverflow.common.DateTimeFormatType
import com.warmpot.android.stackoverflow.common.format
import com.warmpot.android.stackoverflow.databinding.ActivityQuestionDetailsBinding
import com.warmpot.android.stackoverflow.databinding.FragmentAnswersBinding
import com.warmpot.android.stackoverflow.databinding.FragmentCommentsBinding
import com.warmpot.android.stackoverflow.databinding.FragmentDetailsBinding
import com.warmpot.android.stackoverflow.screen.common.constants.IntentConstant
import com.warmpot.android.stackoverflow.screen.common.resource.Str
import com.warmpot.android.stackoverflow.screen.question.details.viewmodel.QuestionDetailsViewModel
import com.warmpot.android.stackoverflow.screen.question.details.viewmodel.QuestionDetailsViewState
import com.warmpot.android.stackoverflow.screen.question.model.Question
import com.warmpot.android.stackoverflow.screen.user.UserActivity
import com.warmpot.android.stackoverflow.screen.user.model.User
import com.warmpot.android.stackoverflow.utils.toHtml
import com.warmpot.android.stackoverflow.utils.viewModel


class QuestionDetailsActivity : AppCompatActivity() {

    private val viewModel by viewModel<QuestionDetailsViewModel>()

    private val binding by lazy { ActivityQuestionDetailsBinding.inflate(layoutInflater) }
    private val detailsBinding by lazy { FragmentDetailsBinding.inflate(layoutInflater) }
    private val answersBinding by lazy { FragmentAnswersBinding.inflate(layoutInflater) }
    private val commentsBinding by lazy { FragmentCommentsBinding.inflate(layoutInflater) }

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

    @SuppressLint("SetJavaScriptEnabled")
    private fun setupViews() {
        binding.apply {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        setupTabLayout()
    }

    private fun setupTabLayout() {
        binding.apply {
            viewFlipper.addView(detailsBinding.root)
            viewFlipper.addView(answersBinding.root)
            viewFlipper.addView(commentsBinding.root)

            val detailsTab = detailsTabs.newTab().also { it.setText(R.string.question_details_tab_title) }
            val answersTab = detailsTabs.newTab().also { it.setText(R.string.question_answers_tab_title) }
            val commentsTab = detailsTabs.newTab().also { it.setText(R.string.question_comments_tab_title) }
            detailsTabs.addTab(detailsTab)
            detailsTabs.addTab(answersTab)
            detailsTabs.addTab(commentsTab)

            detailsTabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    val position = tab?.position ?: return
                    viewFlipper.displayedChild = position
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                }

                override fun onTabReselected(tab: TabLayout.Tab?) {
                }
            })
        }
    }

    private fun loadQuestion() {
        val question: Question = intent.getSerializableExtra(IntentConstant.INTENT_PARAM_KEY) as Question
        viewModel.fetch(question.questionId)
    }

    private fun setupViewModel() {
        viewModel.viewState.observe(this) { viewState ->
            bindViewState(viewState)
        }
    }

    private fun bindViewState(viewState: QuestionDetailsViewState) {
        viewState.error?.also { bindError(it) }
        viewState.question?.also { bindQuestion(it) }
    }

    private fun bindError(error: Str) {
    }

    private fun bindQuestion(question: Question) {
        binding.apply {
            titleTxt.text = question.title.toHtml()
            answerCountTxt.text = getString(R.string.question_answer_fmt, question.answerCount)
            commentCountTxt.text = getString(R.string.question_comment_fmt, question.commentCount)
            createdDateTxt.text = question.creationDate.format(DateTimeFormatType.ddMMyyHHmm)
            ownerTxt.text = question.owner.displayName
            ownerTxt.setOnClickListener {
                navigateToUser(question.owner)
            }
        }

        setupViewPager(question)

        binding.loadingBar.isVisible = false
    }

    private fun setupViewPager(question: Question) {
        binding.apply {
            if (question.answerCount > 0) {
                val badge = detailsTabs.getTabAt(1)!!.orCreateBadge
                badge.backgroundColor = ContextCompat.getColor(detailsTabs.context, R.color.design_default_color_primary)
                badge.isVisible = true
                badge.number = question.answerCount
            }
        }

        detailsBinding.webView.loadDataWithBaseURL(null, question.body, "text/html", "utf-8", null)
    }

    private fun navigateToUser(user: User) {
        val intent = Intent(this, UserActivity::class.java)
        intent.putExtra(IntentConstant.INTENT_PARAM_KEY, user.userId)
        startActivity(intent)
    }
}
