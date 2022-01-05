package com.warmpot.android.stackoverflow.screen.common.navigation

import android.content.Intent
import android.content.Intent.ACTION_SEARCH
import androidx.appcompat.app.AppCompatActivity
import com.warmpot.android.stackoverflow.screen.common.constants.IntentConstant
import com.warmpot.android.stackoverflow.screen.question.details.QuestionDetailsActivity
import com.warmpot.android.stackoverflow.screen.question.model.Question
import com.warmpot.android.stackoverflow.screen.user.UserActivity
import com.warmpot.android.stackoverflow.screen.user.model.User

class ActivityNavigator(
    private val activity: AppCompatActivity
) {

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

    fun goToSearchScreen() {
        //val intent = Intent(activity, SearchActivity::class.java)
        val intent = Intent(ACTION_SEARCH)
        activity.startActivity(intent)
    }
}