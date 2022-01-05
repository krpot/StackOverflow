package com.warmpot.android.stackoverflow.screen.question.details.tabs

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.warmpot.android.stackoverflow.R
import com.warmpot.android.stackoverflow.databinding.FragmentAnswersBinding
import com.warmpot.android.stackoverflow.screen.common.constants.IntentConst
import com.warmpot.android.stackoverflow.screen.question.details.tabs.adapter.AnswerAdapter
import com.warmpot.android.stackoverflow.screen.question.details.viewmodel.QuestionDetailsViewModel
import com.warmpot.android.stackoverflow.screen.question.model.Answer
import com.warmpot.android.stackoverflow.utils.activityViewModel


class AnswersFragment : Fragment(R.layout.fragment_answers) {

    private val viewModel by activityViewModel<QuestionDetailsViewModel>()

    private val answers by lazy { requireArguments().getSerializable(IntentConst.EXTRA_USER_ID) as List<Answer> }

    private val answerAdapter by lazy { AnswerAdapter() }

    private var _binding: FragmentAnswersBinding? = null
    private val binding: FragmentAnswersBinding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAnswersBinding.bind(requireView())
        setupViews()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupViews() {
        binding.apply {
            answerRcv.adapter = answerAdapter
        }

        answerAdapter.submitList(answers)
    }

    companion object {
        fun create(answers: List<Answer>): AnswersFragment {
            return AnswersFragment().also {
                it.arguments = bundleOf(IntentConst.EXTRA_USER_ID to answers)
            }
        }
    }
}
