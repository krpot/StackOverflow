package com.warmpot.android.stackoverflow.screen.common.resource

import android.content.Context
import androidx.annotation.StringRes
import com.warmpot.android.stackoverflow.R
import java.io.Serializable

sealed class Str {
    data class Id(val id: Int) : Str()
    data class Txt(val s: String) : Str()

    companion object {
        fun from(@StringRes strId: Int) = Id(strId)
        fun from(s: String) = Txt(s)
    }
}

fun Str?.text(context: Context): String {
    return when (this) {
        is Str.Id -> context.getString(id)
        is Str.Txt -> s
        else -> ""
    }
}
