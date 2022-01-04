package com.warmpot.android.stackoverflow.screen.common.base

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.warmpot.android.stackoverflow.common.di.AppModule
import com.warmpot.android.stackoverflow.screen.common.dialog.DialogHelper
import com.warmpot.android.stackoverflow.screen.common.navigation.ActivityNavigator

abstract class BaseActivity : AppCompatActivity() {

    protected val navigator: ActivityNavigator by lazy {
        AppModule.provideActivityNavigator(this)
    }

    protected val dialogHelper: DialogHelper by lazy {
        AppModule.provideDialogHelper(
            supportFragmentManager
        )
    }
}

val BaseActivity.context: Context get() = this
