package com.warmpot.android.stackoverflow.screen.common.dialog

import android.content.Context
import androidx.fragment.app.DialogFragment
import com.warmpot.android.stackoverflow.screen.common.constants.IntentConstant.EXTRA_DIALOG_ARG
import com.warmpot.android.stackoverflow.screen.common.resource.text

abstract class BaseDialogFragment : DialogFragment() {

    private val dialogArg: DialogArg by lazy {
        requireArguments().getSerializable(EXTRA_DIALOG_ARG) as DialogArg
    }

    protected val dialogTitle: String get() = dialogArg.title.text(requireContext())
    protected val dialogMessage: String get() = dialogArg.message.text(requireContext())
    protected val positiveButtonCaption: String
        get() = dialogArg.positiveButtonCaption.text(
            requireContext()
        )
    protected val negativeButtonCaption: String?
        get() = dialogArg.negativeButtonCaption?.text(
            requireContext()
        )

    protected lateinit var dialogListener: DialogListener

    override fun onAttach(context: Context) {
        super.onAttach(context)
        dialogListener = context as DialogListener
    }
}