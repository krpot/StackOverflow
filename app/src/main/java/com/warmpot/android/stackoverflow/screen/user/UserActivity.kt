package com.warmpot.android.stackoverflow.screen.user

import android.os.Bundle
import android.view.MenuItem
import com.warmpot.android.stackoverflow.common.di.DependencyInjector
import com.warmpot.android.stackoverflow.screen.common.base.BaseActivity
import com.warmpot.android.stackoverflow.screen.common.constants.IntentConstant
import com.warmpot.android.stackoverflow.screen.common.dialog.DialogArg
import com.warmpot.android.stackoverflow.screen.common.dialog.DialogListener
import com.warmpot.android.stackoverflow.screen.common.dialog.DialogResult
import com.warmpot.android.stackoverflow.screen.common.resource.DialogRes
import com.warmpot.android.stackoverflow.screen.common.resource.Str
import com.warmpot.android.stackoverflow.screen.user.viewmodel.UserViewModel

class UserActivity : BaseActivity(), DialogListener {

    lateinit var viewModel: UserViewModel

    private val binding by lazy {
        UserActivityBinder(
            layoutInflater = layoutInflater,
            onShowError = ::showError
        )
    }

    private val userId by lazy { intent.getIntExtra(IntentConstant.EXTRA_USER_ID, 0) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DependencyInjector.inject(this)

        setContentView(binding.root)

        setupViews()
        setupViewModel()

        viewModel.fetchUser(userId)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupViews() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun setupViewModel() {
        viewModel.uiState.observe(this, binding::handleUiState)
    }

    private fun showError(str: Str?) {
        //binding.containerView.hide()

        val message: Str = str ?: return
        dialogHelper.showInfoDialog(
            DialogArg.Info(title = DialogRes.defaultErrorTitle, message = message)
        )
    }

    override fun onDialogCompleted(result: DialogResult) {
        println("##### onDialogCompleted: $result")
    }
}
