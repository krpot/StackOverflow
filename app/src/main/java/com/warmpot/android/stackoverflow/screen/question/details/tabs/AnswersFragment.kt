package com.warmpot.android.stackoverflow.screen.question.details.tabs

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.warmpot.android.stackoverflow.R
import com.warmpot.android.stackoverflow.screen.question.details.viewmodel.QuestionDetailsViewModel
import com.warmpot.android.stackoverflow.utils.activityViewModel


class AnswersFragment : Fragment(R.layout.fragment_answers) {

    private val viewModel by activityViewModel<QuestionDetailsViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        println("###### AnswersFragment.onViewCreated: $viewModel")
    }
}
