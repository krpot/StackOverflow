package com.warmpot.android.stackoverflow.screen.common.resource

import android.content.Context
import androidx.annotation.StringRes
import java.io.Serializable

sealed class Str {
    data class Id(val id: Int) : Str()
    data class Txt(val s: String) : Str()

    companion object {
        fun from(@StringRes strId: Int) = Str.Id(strId)
        fun from(s: String) = Str.Txt(s)
    }
}

fun Str?.text(context: Context): String {
    return when (this) {
        is Str.Id -> context.getString(id)
        is Str.Txt -> s
        else -> ""
    }
}
