package com.warmpot.android.stackoverflow.screen.common.base

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.warmpot.android.stackoverflow.screen.common.dialog.DialogHelper
import com.warmpot.android.stackoverflow.screen.common.navigation.ActivityNavigator

abstract class BaseActivity : AppCompatActivity() {

    protected val context: Context get() = this

    protected lateinit var navigator: ActivityNavigator

    protected lateinit var dialogHelper: DialogHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializeDependencies()
    }

    protected fun initializeDependencies() {
        navigator = ActivityNavigator(this)
        dialogHelper = DialogHelper(supportFragmentManager)
    }
}