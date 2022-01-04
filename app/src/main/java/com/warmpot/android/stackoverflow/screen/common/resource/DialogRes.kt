package com.warmpot.android.stackoverflow.screen.common.resource

import com.warmpot.android.stackoverflow.R

object DialogRes {

    val defaultErrorTitle: Str by lazy { Str.Id(R.string.data_load_error_title) }

    val yes: Str by lazy { Str.Id(android.R.string.ok) }

    val no: Str by lazy { Str.Id(android.R.string.cancel) }

    val retry: Str by lazy { Str.Id(R.string.dialog_retry_caption) }
}