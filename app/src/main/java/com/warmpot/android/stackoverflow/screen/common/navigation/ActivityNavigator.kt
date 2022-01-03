package com.warmpot.android.stackoverflow.screen.common.navigation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentManager
import com.warmpot.android.stackoverflow.screen.common.constants.IntentConstant
import com.warmpot.android.stackoverflow.screen.common.dialog.InfoDialogArg
import com.warmpot.android.stackoverflow.screen.common.dialog.InfoDialogFragment
import com.warmpot.android.stackoverflow.screen.question.details.QuestionDetailsActivity
import com.warmpot.android.stackoverflow.screen.question.model.Question
import com.warmpot.android.stackoverflow.screen.user.UserActivity
import com.warmpot.android.stackoverflow.screen.user.model.User

class ActivityNavigator(
    private val activity: AppCompatActivity
) {

    private val fragmentManager: FragmentManager get() = activity.supportFragmentManager

    fun goToDetailsScreen(question: Question) {
        val intent = Intent(activity, QuestionDetailsActivity::class.java)
        intent.putExtra(IntentConstant.EXTRA_QUESTION, question)
        activity.startActivity(intent)
    }

    fun goToUserScreen(user: User) {
        val intent = Intent(activity, UserActivity::class.java)
        intent.putExtra(IntentConstant.EXTRA_USER_ID, user.userId)
        activity.startActivity(intent)
    }

    fun showInfoDialog(args: InfoDialogArg) {
        InfoDialogFragment()
            .also {
                it.arguments = bundleOf(InfoDialogFragment.EXTRA_INFO_DIALOG_ARG to args)
            }
            .show(fragmentManager, InfoDialogFragment.TAG)
    }
}