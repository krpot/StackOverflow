package com.warmpot.android.stackoverflow.screen.customview

import android.content.Context
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.core.widget.TextViewCompat
import com.warmpot.android.stackoverflow.R
import com.warmpot.android.stackoverflow.utils.dpToPx

class StatView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatTextView(
    context, attrs, defStyleAttr
) {

    var stat: Int = 0
        set(value) {
            if (field == value) return
            field = value
            updateText()
        }

    var unit: String = ""
        set(value) {
            if (field == value) return
            field = value
            updateText()
        }

    init {
        TextViewCompat.setTextAppearance(this, R.style.TextAppearance_MaterialComponents_Headline6)

        this.setLineSpacing(8.dpToPx(context), 1f)
        this.textAlignment = TEXT_ALIGNMENT_CENTER
    }

    private fun setSpanStyle(count: Int, s: String): SpannableStringBuilder {
        val str = "$count\n$s"
        val sb = SpannableStringBuilder(str)
        sb.setSpan(
            RelativeSizeSpan(0.5f),
            count.toString().length, str.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        sb.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(context, R.color.material_on_background_disabled)),
            count.toString().length, str.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        return sb
    }

    private fun updateText() {
        this.text = setSpanStyle(stat, unit)
    }
}
