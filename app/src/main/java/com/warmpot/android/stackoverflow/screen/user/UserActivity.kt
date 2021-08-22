package com.warmpot.android.stackoverflow.screen.user

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.warmpot.android.stackoverflow.databinding.ActivityUserBinding
import com.warmpot.android.stackoverflow.screen.common.constants.IntentConstant
import com.warmpot.android.stackoverflow.screen.user.model.User
import com.warmpot.android.stackoverflow.screen.user.viewmodel.UserViewModel
import com.warmpot.android.stackoverflow.screen.user.viewmodel.UserViewState
import com.warmpot.android.stackoverflow.utils.viewModel

class UserActivity : AppCompatActivity() {

    private val viewModel by viewModel<UserViewModel>()

    private val binding by lazy { ActivityUserBinding.inflate(layoutInflater) }

    private val userId by lazy { intent.getIntExtra(IntentConstant.INTENT_PARAM_KEY, 0) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        viewModel.viewState.observe(this) { userViewState ->
            handleViewState(userViewState)
        }
    }

    private fun handleViewState(state: UserViewState) {
        state.user?.also(::bindUser)
    }

    private fun bindUser(user: User) {
        binding.apply {
            displayNameTxt.text = user.displayName
            aboutMeTxt.text = user.aboutMe
            locationTxt.text = user.location
            websiteTxt.text = user.websiteUrl
            goldBadgeCountTxt.text = user.badgeCounts?.gold.toString()
            silverBadgeCountTxt.text = user.badgeCounts?.silver.toString()
            bronzeBadgeCountTxt.text = user.badgeCounts?.bronze.toString()
        }
    }

}
