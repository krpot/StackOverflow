package com.warmpot.android.stackoverflow.screen.common.dialog

import android.app.Dialog
import android.os.Bundle
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.warmpot.android.stackoverflow.screen.common.resource.text

class ConfirmDialogFragment : BaseDialogFragment() {

    companion object {
        const val TAG = "YesNoDialogFragment"
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return MaterialAlertDialogBuilder(requireContext())
            .setTitle(dialogTitle)
            .setMessage(dialogMessage)
            .setPositiveButton(positiveButtonCaption) { dialog, _ ->
                dialog.dismiss()
                dialogListener.onDialogCompleted(DialogResult.Yes(Unit))
            }
            .setNegativeButton(negativeButtonCaption) { dialog, _ ->
                dialog.dismiss()
                dialogListener.onDialogCompleted(DialogResult.No)
            }
            .create()
    }
}
