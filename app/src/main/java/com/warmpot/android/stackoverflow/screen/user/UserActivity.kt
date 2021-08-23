package com.warmpot.android.stackoverflow.screen.user

import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.warmpot.android.stackoverflow.R
import com.warmpot.android.stackoverflow.databinding.ActivityUserBinding
import com.warmpot.android.stackoverflow.screen.common.constants.IntentConstant
import com.warmpot.android.stackoverflow.screen.user.model.User
import com.warmpot.android.stackoverflow.screen.user.viewmodel.UserViewModel
import com.warmpot.android.stackoverflow.screen.user.viewmodel.UserViewState
import com.warmpot.android.stackoverflow.utils.circle
import com.warmpot.android.stackoverflow.utils.show
import com.warmpot.android.stackoverflow.utils.toHtml
import com.warmpot.android.stackoverflow.utils.viewModel

class UserActivity : AppCompatActivity() {

    private val viewModel by viewModel<UserViewModel>()

    private val binding by lazy { ActivityUserBinding.inflate(layoutInflater) }

    private val userId by lazy { intent.getIntExtra(IntentConstant.INTENT_PARAM_KEY, 0) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupViews()
        setupViewModel()

        viewModel.fetchUser(userId)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupViews() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun setupViewModel() {
        viewModel.viewState.observe(this) { userViewState ->
            handleViewState(userViewState)
        }
    }

    private fun handleViewState(state: UserViewState) {
        state.user?.also(::bindUser)
    }

    private fun bindUser(user: User) {
        binding.apply {
            photoImg.circle(user.profileImage)
            displayNameTxt.text = user.displayName
            aboutMeTxt.text = user.aboutMe.toHtml()
            locationTxt.text = user.location
            websiteTxt.text = user.websiteUrl
            goldBadgeCountTxt.text = user.badgeCounts?.gold.toString()
            silverBadgeCountTxt.text = user.badgeCounts?.silver.toString()
            bronzeBadgeCountTxt.text = user.badgeCounts?.bronze.toString()
            linkTxt.text = user.link

            reputationTxt.text = setSpanStyle(user.reputation, "reputations")
            answerCountTxt.text = setSpanStyle(user.answerCount, "answers")
            questionCountTxt.text = setSpanStyle(user.questionCount, "questions")

            scoreAndBadgeGroup.show()
        }
    }

    private fun setSpanStyle(count: Int, s: String): CharSequence {
        val spannable = SpannableString("$count\n$s")
        spannable.setSpan(
            RelativeSizeSpan(0.5f),
            count.toString().length, spannable.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spannable.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(this, R.color.material_on_background_disabled)),
            count.toString().length, spannable.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        return spannable
    }
}
