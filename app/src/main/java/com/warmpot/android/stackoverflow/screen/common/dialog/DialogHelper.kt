package com.warmpot.android.stackoverflow.screen.common.dialog

import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.warmpot.android.stackoverflow.screen.common.constants.IntentConst

class DialogHelper(
    private val fragmentManager: FragmentManager
) {
    fun showInfoDialog(args: DialogArg) {
        showDialogFragment(
            fragment = InfoDialogFragment(),
            tag = InfoDialogFragment.TAG,
            args = args
        )
    }

    fun showConfirmDialog(args: DialogArg) {
        showDialogFragment(
            fragment = ConfirmDialogFragment(),
            tag = ConfirmDialogFragment.TAG,
            args = args
        )
    }

    private fun showDialogFragment(fragment: BaseDialogFragment, tag: String, args: DialogArg) {
        val dialogFragment = findDialogFragmentByTag(tag)
        dialogFragment?.dismiss()

        fragment.also {
            it.arguments = bundleOf(IntentConst.EXTRA_DIALOG_ARG to args)
        }.show(fragmentManager, tag)
    }

    private fun findDialogFragmentByTag(tag: String): DialogFragment? {
        return fragmentManager.findFragmentByTag(tag) as? DialogFragment
    }
}