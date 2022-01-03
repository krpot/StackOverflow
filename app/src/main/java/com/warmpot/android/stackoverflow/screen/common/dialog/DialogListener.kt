package com.warmpot.android.stackoverflow.screen.common.dialog

interface DialogListener {
    fun onDialogCompleted(result: DialogResult)
}

sealed class DialogResult {
    data class Yes(val data: Any) : DialogResult()
    object No : DialogResult()
}