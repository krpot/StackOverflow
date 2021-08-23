package com.warmpot.android.stackoverflow.screen.question.details.viewpager

import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.warmpot.android.stackoverflow.R
import com.warmpot.android.stackoverflow.screen.question.details.tabs.AnswersFragment
import com.warmpot.android.stackoverflow.screen.question.details.tabs.CommentsFragment
import com.warmpot.android.stackoverflow.screen.question.details.tabs.DetailsFragment
import com.warmpot.android.stackoverflow.screen.question.details.viewpager.QuestionDetailsViewPagerAdapter.Pages.Companion.PAGES
import com.warmpot.android.stackoverflow.screen.question.model.Question

class QuestionDetailsViewPagerAdapter(
    activity: FragmentActivity,
    private val question: Question
) : FragmentStateAdapter(activity) {

    enum class Pages(@StringRes val titleId: Int) {
        QUESTION(R.string.question_details_tab_title),
        ANSWERS(R.string.question_answers_tab_title),
        COMMENTS(R.string.question_comments_tab_title);

        companion object {
            val PAGES = values()
        }
    }

    override fun getItemCount(): Int = PAGES.size

    override fun createFragment(position: Int): Fragment {
        return when (PAGES[position]) {
            Pages.QUESTION -> DetailsFragment.create(question)
            Pages.ANSWERS -> AnswersFragment()
            Pages.COMMENTS -> CommentsFragment()
        }
    }

    fun tabTitleId(position: Int): Int = PAGES[position].titleId
}
