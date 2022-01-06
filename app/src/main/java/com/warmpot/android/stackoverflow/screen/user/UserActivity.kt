package com.warmpot.android.stackoverflow.screen.user

import android.os.Bundle
import android.view.MenuItem
import com.warmpot.android.stackoverflow.common.di.DependencyInjector
import com.warmpot.android.stackoverflow.screen.common.base.BaseActivity
import com.warmpot.android.stackoverflow.screen.common.constants.IntentConst
import com.warmpot.android.stackoverflow.screen.common.dialog.DialogArg
import com.warmpot.android.stackoverflow.screen.common.dialog.DialogListener
import com.warmpot.android.stackoverflow.screen.common.dialog.DialogResult
import com.warmpot.android.stackoverflow.screen.common.protocol.HomePressProtocol
import com.warmpot.android.stackoverflow.screen.common.resource.DialogRes
import com.warmpot.android.stackoverflow.screen.common.resource.Str
import com.warmpot.android.stackoverflow.screen.user.viewmodel.UserViewModel

class UserActivity : BaseActivity(), HomePressProtocol, DialogListener {

    lateinit var viewModel: UserViewModel

    private val binding by lazy {
        UserActivityBinder(
            layoutInflater = layoutInflater,
            onShowError = ::showError
        )
    }

    private val userId by lazy { intent.getIntExtra(IntentConst.EXTRA_USER_ID, 0) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DependencyInjector.inject(this)

        setContentView(binding.root)

        setupViews()
        setupViewModel()

        viewModel.fetchUser(userId)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (homePressHandled(item = item)) return true
        return false
    }

    private fun setupViews() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun setupViewModel() {
        viewModel.uiState.observe(this, binding::handleUiState)
    }

    private fun showError(str: Str?) {
        val message: Str = str ?: return
        dialogHelper.showInfoDialog(
            DialogArg.Info(title = DialogRes.defaultErrorTitle, message = message)
        )
    }

    override fun onDialogCompleted(result: DialogResult) {
        println("##### onDialogCompleted: $result")
    }
}
