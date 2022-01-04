package com.warmpot.android.stackoverflow.common.di

import androidx.lifecycle.ViewModelProvider
import com.warmpot.android.stackoverflow.screen.common.base.BaseActivity
import com.warmpot.android.stackoverflow.screen.common.navigation.ActivityNavigator
import com.warmpot.android.stackoverflow.screen.question.details.QuestionDetailsActivity
import com.warmpot.android.stackoverflow.screen.question.details.viewmodel.QuestionDetailsViewModel
import com.warmpot.android.stackoverflow.screen.question.list.QuestionListActivity
import com.warmpot.android.stackoverflow.screen.question.list.viewmodel.QuestionListViewModel
import com.warmpot.android.stackoverflow.screen.user.UserActivity
import com.warmpot.android.stackoverflow.screen.user.viewmodel.UserViewModel

object DependencyInjector {

    fun injectBase(activity: BaseActivity) {
        activity.apply {
            navigator = ActivityNavigator(activity)
            dialogHelper = AppModule.provideDialogHelper(activity.supportFragmentManager)
        }
    }

    fun inject(activity: QuestionListActivity) {
        activity.apply {
            viewModel = ViewModelProvider(
                this,
                AppModule.viewModelFactory
            ).get(QuestionListViewModel::class.java)
        }
    }

    fun inject(activity: QuestionDetailsActivity) {
        activity.apply {
            viewModel = ViewModelProvider(
                this,
                AppModule.viewModelFactory
            ).get(QuestionDetailsViewModel::class.java)
        }
    }

    fun inject(activity: UserActivity) {
        activity.apply {
            viewModel = ViewModelProvider(
                this,
                AppModule.viewModelFactory
            ).get(UserViewModel::class.java)
        }
    }
}