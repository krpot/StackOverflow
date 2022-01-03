package com.warmpot.android.stackoverflow.screen.common.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.io.Serializable

class InfoDialogFragment : DialogFragment() {

    companion object {
        const val TAG = "InfoDialogFragment"
        const val EXTRA_INFO_DIALOG_ARG = "Extra.InfoDialogArg"
    }

    private val dialogArg: InfoDialogArg by lazy {
        requireArguments().getSerializable(EXTRA_INFO_DIALOG_ARG) as InfoDialogArg
    }

    private lateinit var dialogListener: DialogListener

    override fun onAttach(context: Context) {
        super.onAttach(context)
        dialogListener = context as DialogListener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return MaterialAlertDialogBuilder(requireContext())
            .setTitle(dialogArg.title)
            .setMessage(dialogArg.message)
            .setPositiveButton(android.R.string.ok) { dialog, _ ->
                dialog.dismiss()
                dialogListener.onDialogCompleted(DialogResult.Yes(Unit))
            }
            .create()
    }
}

data class InfoDialogArg(
    val title: String,
    val message: String
) : Serializable