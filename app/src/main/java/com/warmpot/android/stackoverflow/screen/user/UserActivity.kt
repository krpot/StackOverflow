package com.warmpot.android.stackoverflow.screen.user

import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.view.MenuItem
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.warmpot.android.stackoverflow.R
import com.warmpot.android.stackoverflow.databinding.ActivityUserBinding
import com.warmpot.android.stackoverflow.screen.common.base.BaseActivity
import com.warmpot.android.stackoverflow.screen.common.constants.IntentConstant
import com.warmpot.android.stackoverflow.screen.common.dialog.DialogArg
import com.warmpot.android.stackoverflow.screen.common.dialog.DialogListener
import com.warmpot.android.stackoverflow.screen.common.dialog.DialogResult
import com.warmpot.android.stackoverflow.screen.common.resource.DialogRes
import com.warmpot.android.stackoverflow.screen.common.resource.Str
import com.warmpot.android.stackoverflow.screen.user.model.User
import com.warmpot.android.stackoverflow.screen.user.viewmodel.UserUiState
import com.warmpot.android.stackoverflow.screen.user.viewmodel.UserViewModel
import com.warmpot.android.stackoverflow.utils.*

class UserActivity : BaseActivity(), DialogListener {

    private val viewModel by viewModel<UserViewModel>()

    private val binding by lazy { ActivityUserBinding.inflate(layoutInflater) }

    private val userId by lazy { intent.getIntExtra(IntentConstant.EXTRA_USER_ID, 0) }

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
        viewModel.uiState.observe(this, ::handleUiState)
    }

    private fun handleUiState(state: UserUiState) {
        state.loading.value()?.also(::bindLoading)
        state.error?.value()?.also(::showError)
        state.user?.also(::bindUser)
    }

    private fun showError(str: Str?) {
        binding.containerView.hide()

        val message: Str = str ?: return
        dialogHelper.showInfoDialog(
            DialogArg.Info(title = DialogRes.defaultErrorTitle, message = message)
        )
    }

    private fun bindLoading(isLoading: Boolean) {
        binding.apply {
            loadingBar.isVisible = isLoading
        }
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
            containerView.show()
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
            ForegroundColorSpan(
                ContextCompat.getColor(
                    this,
                    R.color.material_on_background_disabled
                )
            ),
            count.toString().length, spannable.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        return spannable
    }

    override fun onDialogCompleted(result: DialogResult) {
        println("##### onDialogCompleted: $result")
    }
}
