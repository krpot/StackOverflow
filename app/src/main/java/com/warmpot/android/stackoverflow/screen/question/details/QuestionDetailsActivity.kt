package com.warmpot.android.stackoverflow.screen.question.details

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
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
import com.warmpot.android.stackoverflow.screen.question.details.tabs.adapter.AnswerAdapter
import com.warmpot.android.stackoverflow.screen.question.details.viewmodel.QuestionDetailsViewModel
import com.warmpot.android.stackoverflow.screen.question.details.viewmodel.QuestionDetailsUiState
import com.warmpot.android.stackoverflow.screen.question.model.Question
import com.warmpot.android.stackoverflow.screen.user.UserActivity
import com.warmpot.android.stackoverflow.screen.user.model.User
import com.warmpot.android.stackoverflow.utils.colorRes
import com.warmpot.android.stackoverflow.utils.toHtml
import com.warmpot.android.stackoverflow.utils.viewModel


class QuestionDetailsActivity : AppCompatActivity() {

    private val viewModel by viewModel<QuestionDetailsViewModel>()

    private val binding by lazy { ActivityQuestionDetailsBinding.inflate(layoutInflater) }
    private val detailsBinding by lazy { FragmentDetailsBinding.inflate(layoutInflater) }
    private val answersBinding by lazy { FragmentAnswersBinding.inflate(layoutInflater) }
    private val commentsBinding by lazy { FragmentCommentsBinding.inflate(layoutInflater) }

    private val answerAdapter by lazy { AnswerAdapter() }

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

        answersBinding.apply {
            answerRcv.adapter = answerAdapter
        }
    }

    private fun loadQuestion() {
        val question: Question = intent.getSerializableExtra(IntentConstant.INTENT_PARAM_KEY) as Question
        viewModel.fetch(question.questionId)
    }

    private fun setupViewModel() {
        viewModel.uiState.observe(this) { viewState ->
            bindViewState(viewState)
        }
    }

    private fun bindViewState(uiState: QuestionDetailsUiState) {
        uiState.error?.also { bindError(it) }
        uiState.question?.also { bindQuestion(it) }
    }

    private fun bindError(error: Str) {
    }

    private fun bindQuestion(question: Question) {
        binding.apply {
            titleTxt.text = question.title.toHtml()
            voteCountTxt.text = getString(R.string.question_votes_fmt, question.upvoteCount)
            viewCountTxt.text = getString(R.string.question_views_fmt, question.viewCount)
            createdDateTxt.text = question.creationDate.format(DateTimeFormatType.ddMMyyHHmm)
            ownerTxt.text = question.owner.displayName
            ownerTxt.setOnClickListener {
                navigateToUser(question.owner)
            }
        }

        bindDetailsPage(question)
        bindAnswersBadge(question)
        bindAnswerPage(question)

        binding.loadingBar.isVisible = false
    }

    private fun bindDetailsPage(question: Question) {
        val owner = question.owner
        detailsBinding.apply {
            webView.loadDataWithBaseURL(null, question.body, "text/html", "utf-8", null)
            //ownerTxt.text = owner.displayName
            //avatarImg.circle(owner.profileImage)
            val badges = owner.badgeCounts
            ownerStatsView.displayName = owner.displayName
            ownerStatsView.avatarUrl = owner.profileImage
            ownerStatsView.reputation = owner.reputation
            ownerStatsView.gold = badges?.gold ?: 0
            ownerStatsView.silver = badges?.silver ?: 0
            ownerStatsView.reputation = badges?.bronze ?: 0
        }
    }

    private fun bindAnswersBadge(question: Question) {
        binding.apply {
            if (question.answerCount > 0) {
                val badge = detailsTabs.getTabAt(1)!!.orCreateBadge
                badge.backgroundColor = detailsTabs.context.colorRes(R.color.design_default_color_primary)
                badge.isVisible = true
                badge.number = question.answerCount
            }
        }
    }

    private fun bindAnswerPage(question: Question) {
        answerAdapter.submitList(question.answers)
    }

    private fun navigateToUser(user: User) {
        val intent = Intent(this, UserActivity::class.java)
        intent.putExtra(IntentConstant.INTENT_PARAM_KEY, user.userId)
        startActivity(intent)
    }
}
