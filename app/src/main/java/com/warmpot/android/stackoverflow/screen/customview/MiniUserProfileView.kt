package com.warmpot.android.stackoverflow.screen.customview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.webkit.URLUtil
import android.widget.FrameLayout
import com.warmpot.android.stackoverflow.databinding.ViewMiniUserProfileBinding
import com.warmpot.android.stackoverflow.screen.user.model.User
import com.warmpot.android.stackoverflow.utils.circle

const val DOT = "\u2022"

class MiniUserProfileView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(
    context, attrs, defStyleAttr
) {

    var avatarUrl: String = ""
        set(value) {
            field = value
            updateAvatar()
        }

    var reputation: Int
        get() = binding.statsView.reputation
        set(value) {
            binding.statsView.reputation = reputation
        }

    var gold: Int
        get() = binding.statsView.gold
        set(value) {
            binding.statsView.gold = gold
        }

    var silver: Int
        get() = binding.statsView.silver
        set(value) {
            binding.statsView.silver = silver
        }

    var bronze: Int
        get() = binding.statsView.bronze
        set(value) {
            binding.statsView.bronze = bronze
        }

    var displayName: CharSequence
        get() = binding.displayNameTxt.text
        set(value) {
            binding.displayNameTxt.text = value
        }

    private val binding = ViewMiniUserProfileBinding.inflate(LayoutInflater.from(context), this, true)

    private fun updateAvatar() {
        if (!URLUtil.isValidUrl(avatarUrl)) return

        binding.avatarImg.circle(avatarUrl)
    }

    fun bindUser(user: User) {
        displayName = user.displayName
        reputation = user.reputation
        gold = user.badgeCounts?.gold ?: 0
        silver = user.badgeCounts?.silver ?: 0
        bronze = user.badgeCounts?.silver ?: 0
    }
}
