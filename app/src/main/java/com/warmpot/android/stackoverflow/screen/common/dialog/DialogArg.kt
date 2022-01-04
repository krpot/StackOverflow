package com.warmpot.android.stackoverflow.screen.common.dialog

import com.warmpot.android.stackoverflow.screen.common.resource.DialogRes
import com.warmpot.android.stackoverflow.screen.common.resource.Str
import java.io.Serializable

sealed class DialogArg(
) : Serializable {
    abstract val title: Str
    abstract val message: Str
    abstract val positiveButtonCaption: Str
    open val negativeButtonCaption: Str? = null

    data class Info(
        override val title: Str,
        override val message: Str,
        override val positiveButtonCaption: Str = Str.Id(android.R.string.ok)
    ) : DialogArg()

    data class Confirm(
        override val title: Str,
        override val message: Str,
        override val positiveButtonCaption: Str = DialogRes.yes,
        override val negativeButtonCaption: Str = DialogRes.no
    ) : DialogArg()
}