package com.warmpot.android.stackoverflow.screen.common.dialog

import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager

class DialogHelper(
    private val fragmentManager: FragmentManager
) {
    fun showInfoDialog(args: InfoDialogArg) {
        val dialogFragment = findDialogFragmentByTag(InfoDialogFragment.TAG)
        dialogFragment?.dismiss()

        InfoDialogFragment()
            .also {
                it.arguments = bundleOf(InfoDialogFragment.EXTRA_INFO_DIALOG_ARG to args)
            }
            .show(fragmentManager, InfoDialogFragment.TAG)
    }

    private fun showConfirmDialog(args: InfoDialogArg) {
        // TODO : Implementation
    }

    private fun findDialogFragmentByTag(tag: String): DialogFragment? {
        return fragmentManager.findFragmentByTag(tag) as? DialogFragment
    }
}