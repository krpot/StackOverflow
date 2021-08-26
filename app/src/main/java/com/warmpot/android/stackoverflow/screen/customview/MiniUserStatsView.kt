package com.warmpot.android.stackoverflow.screen.customview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.warmpot.android.stackoverflow.databinding.ViewMiniUserStatsBinding

class MiniUserStatsView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(
    context, attrs, defStyleAttr
) {

    var reputation: Int = 0
        set(value) {
            field = value
            binding.reputationTxt.text = value.toString()
        }

    var gold: Int = 0
        set(value) {
            field = value
            binding.goldBadgeCountTxt.text = value.toString()
        }

    var silver: Int = 0
        set(value) {
            field = value
            binding.silverBadgeCountTxt.text = value.toString()
        }

    var bronze: Int = 0
        set(value) {
            field = value
            binding.bronzeBadgeCountTxt.text = value.toString()
        }

    private val binding = ViewMiniUserStatsBinding.inflate(LayoutInflater.from(context), this)

    init {
        orientation = HORIZONTAL
    }

}
