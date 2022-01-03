package com.warmpot.android.stackoverflow.screen.common.base

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.warmpot.android.stackoverflow.screen.common.navigation.ActivityNavigator

abstract class BaseActivity : AppCompatActivity() {

    val context: Context get() = this

    lateinit var navigator: ActivityNavigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializeDependencies()
    }

    protected fun initializeDependencies() {
        navigator = ActivityNavigator(this)
    }
}