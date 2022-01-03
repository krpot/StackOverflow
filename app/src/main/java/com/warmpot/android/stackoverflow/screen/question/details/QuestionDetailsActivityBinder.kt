package com.warmpot.android.stackoverflow.screen.question.details

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.StringRes
import com.warmpot.android.stackoverflow.R
import com.warmpot.android.stackoverflow.common.DateTimeFormatType
import com.warmpot.android.stackoverflow.common.format
import com.warmpot.android.stackoverflow.databinding.ActivityQuestionDetailsBinding
import com.warmpot.android.stackoverflow.databinding.FragmentAnswersBinding
import com.warmpot.android.stackoverflow.databinding.FragmentCommentsBinding
import com.warmpot.android.stackoverflow.databinding.FragmentDetailsBinding
import com.warmpot.android.stackoverflow.screen.customview.bindWith
import com.warmpot.android.stackoverflow.screen.question.details.tabs.adapter.AnswerAdapter
import com.warmpot.android.stackoverflow.screen.question.model.Answer
import com.warmpot.android.stackoverflow.screen.question.model.Question
import com.warmpot.android.stackoverflow.screen.user.model.User
import com.warmpot.android.stackoverflow.utils.*

class QuestionDetailsActivityBinder(
    private val layoutInflater: LayoutInflater
) {
    val binding: ActivityQuestionDetailsBinding by lazy {
        ActivityQuestionDetailsBinding.inflate(
            layoutInflater
        )
    }

    val root: View get() = binding.root

    private val detailsBinding by lazy { FragmentDetailsBinding.inflate(layoutInflater) }
    private val answersBinding by lazy { FragmentAnswersBinding.inflate(layoutInflater) }
    private val commentsBinding by lazy { FragmentCommentsBinding.inflate(layoutInflater) }

    private val context: Context get() = binding.root.context

    private val answerAdapter by lazy { AnswerAdapter() }

    fun setupTabLayout() {
        fun addTab(@StringRes strId: Int) {
            val tab = binding.detailsTabs.newTab().also { it.setText(strId) }
            binding.detailsTabs.addTab(tab)
        }

        binding.apply {
            viewFlipper.addView(detailsBinding.root)
            viewFlipper.addView(answersBinding.root)
            viewFlipper.addView(commentsBinding.root)

            addTab(R.string.question_details_tab_title)
            addTab(R.string.question_answers_tab_title)
            addTab(R.string.question_comments_tab_title)

            detailsTabs.doTabSelected { _, position ->
                viewFlipper.displayedChild = position
            }
        }

        answersBinding.apply {
            answerRcv.adapter = answerAdapter
        }
    }

    fun bindQuestion(question: Question, ownerClicked: (User) -> Unit) {
        updateQuestion(question, ownerClicked)
        updateDetailsPage(question)
        updateAnswersBadge(question)
        submitAnswers(question.answers)

        binding.loadingBar.hide()
    }

    private fun updateQuestion(question: Question, ownerClicked: (User) -> Unit) {
        binding.apply {
            titleTxt.text = question.title.toHtml()
            voteCountTxt.text =
                context.getString(R.string.question_votes_fmt, question.upvoteCount.formatted())
            viewCountTxt.text =
                context.getString(R.string.question_views_fmt, question.viewCount.formatted())
            createdDateTxt.text = question.creationDate.format(DateTimeFormatType.ddMMyyHHmm)
            ownerTxt.text = question.owner.displayName
            ownerTxt.setOnClickListener {
                ownerClicked(question.owner)
            }
        }
    }

    private fun updateDetailsPage(question: Question) {
        detailsBinding.apply {
            webView.loadHtml(question.body)
            ownerStatsView.bindWith(question)
        }
    }

    private fun updateAnswersBadge(question: Question) {
        if (question.answerCount < 1) return

        binding.apply {
            val badge = detailsTabs.getTabAt(1)!!.orCreateBadge
            badge.backgroundColor =
                detailsTabs.context.colorRes(R.color.design_default_color_primary)
            badge.isVisible = true
            badge.number = question.answerCount
        }
    }

    private fun submitAnswers(answers: List<Answer>) {
        answerAdapter.submitList(answers)
    }
}