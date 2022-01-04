package com.warmpot.android.stackoverflow.screen.user

import android.content.Context
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.view.LayoutInflater
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.warmpot.android.stackoverflow.R
import com.warmpot.android.stackoverflow.databinding.ActivityUserBinding
import com.warmpot.android.stackoverflow.screen.common.resource.Str
import com.warmpot.android.stackoverflow.screen.user.model.User
import com.warmpot.android.stackoverflow.screen.user.viewmodel.UserUiState
import com.warmpot.android.stackoverflow.utils.circle
import com.warmpot.android.stackoverflow.utils.hide
import com.warmpot.android.stackoverflow.utils.show
import com.warmpot.android.stackoverflow.utils.toHtml

class UserActivityBinder(
    private val layoutInflater: LayoutInflater,
    private val onShowError: (Str) -> Unit
) {
    val root: View get() = binding.root

    private val context: Context get() = root.context

    private val binding by lazy { ActivityUserBinding.inflate(layoutInflater) }

    fun handleUiState(state: UserUiState) {
        state.loading.value().also(::bindLoading)
        state.error?.value()?.also(::bindError)
        state.user?.also(::bindUser)
    }

    private fun bindLoading(isLoading: Boolean?) {
        binding.apply {
            loadingBar.isVisible = isLoading == true
        }
    }

    private fun bindError(str: Str?) {
        str?.also { onShowError(it) }
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
                    context,
                    R.color.material_on_background_disabled
                )
            ),
            count.toString().length, spannable.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        return spannable
    }
}