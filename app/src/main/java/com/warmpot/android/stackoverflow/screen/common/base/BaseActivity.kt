package com.warmpot.android.stackoverflow.screen.common.base

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.warmpot.android.stackoverflow.common.di.DependencyInjector
import com.warmpot.android.stackoverflow.screen.common.dialog.DialogHelper
import com.warmpot.android.stackoverflow.screen.common.navigation.ActivityNavigator
import com.warmpot.android.stackoverflow.screen.common.protocol.ActivityProtocol

abstract class BaseActivity : AppCompatActivity(), ActivityProtocol {

    override val self: AppCompatActivity get() = this

    lateinit var navigator: ActivityNavigator
    lateinit var dialogHelper: DialogHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DependencyInjector.injectBase(this)
    }
}

val BaseActivity.context: Context get() = this
