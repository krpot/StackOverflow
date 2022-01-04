package com.warmpot.android.stackoverflow.common.di

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.warmpot.android.stackoverflow.screen.common.dialog.DialogHelper
import com.warmpot.android.stackoverflow.screen.common.navigation.ActivityNavigator
import com.warmpot.android.stackoverflow.screen.common.viewmodel.ViewModelFactory

object AppModule {

    val viewModelFactory: ViewModelFactory = ViewModelModule.viewModelFactory

    fun provideDialogHelper(fragmentManager: FragmentManager): DialogHelper {
        return DialogHelper(fragmentManager)
    }

    fun provideActivityNavigator(activity: AppCompatActivity): ActivityNavigator {
        return ActivityNavigator(activity)
    }
}