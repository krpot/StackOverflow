package com.warmpot.android.stackoverflow.screen.common.exception

import com.warmpot.android.stackoverflow.R
import com.warmpot.android.stackoverflow.screen.common.resource.Str
import retrofit2.HttpException
import java.net.UnknownHostException

fun Throwable.toUiMessage(defaultMessage: Str? = null): Str {
    return when (this) {
        is UnknownHostException -> Str.from(R.string.error_no_connectivity)
        is HttpException -> Str.from("${this.message()} (${this.code()})")
        else -> defaultMessage ?: Str.from(R.string.error_network_unavailable)
    }
}
