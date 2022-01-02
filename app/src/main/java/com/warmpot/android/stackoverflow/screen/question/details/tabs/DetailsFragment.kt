package com.warmpot.android.stackoverflow.screen.question.details.tabs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.warmpot.android.stackoverflow.R
import com.warmpot.android.stackoverflow.databinding.FragmentDetailsBinding
import com.warmpot.android.stackoverflow.screen.common.constants.IntentConstant
import com.warmpot.android.stackoverflow.screen.question.details.viewmodel.QuestionDetailsViewModel
import com.warmpot.android.stackoverflow.screen.question.model.Question
import com.warmpot.android.stackoverflow.utils.activityViewModel


class DetailsFragment : Fragment(R.layout.fragment_details) {

    private val viewModel by activityViewModel<QuestionDetailsViewModel>()

    private val question: Question by lazy { requireArguments().getSerializable(IntentConstant.EXTRA_USER_ID) as Question }

    private var _binding: FragmentDetailsBinding? = null
    private val binding: FragmentDetailsBinding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return FragmentDetailsBinding.inflate(inflater, container, false).also { aBinding ->
            _binding = aBinding
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupViews() {
        binding.apply {
            webView.loadDataWithBaseURL(null, question.body, "text/html", "utf-8", null)
        }
    }

    companion object {
        fun create(question: Question): DetailsFragment {
            return DetailsFragment().apply {
                arguments = bundleOf(IntentConstant.EXTRA_USER_ID to question)
            }
        }
    }
}
