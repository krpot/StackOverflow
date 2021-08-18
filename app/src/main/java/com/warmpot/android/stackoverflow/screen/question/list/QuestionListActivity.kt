package com.warmpot.android.stackoverflow.screen.question.list

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import com.warmpot.android.stackoverflow.data.schema.QuestionSchema
import com.warmpot.android.stackoverflow.databinding.ActivityQuestionListBinding
import java.time.LocalDate
import java.util.*

class QuestionListActivity : AppCompatActivity() {

    private val binding by lazy { ActivityQuestionListBinding.inflate(layoutInflater) }

    private val questionAdapter by lazy { QuestionAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupViews()
        loadQuestions()
    }

    private fun setupViews() {
        binding.apply {
            questionRcv.adapter = questionAdapter
            questionRcv.addItemDecoration(DividerItemDecoration(this@QuestionListActivity, DividerItemDecoration.VERTICAL))
        }
    }

    private fun loadQuestions() {
        val sampleDate = LocalDate.of(2021, 8, 19).toEpochDay()
        questionAdapter.submitList(
            listOf(
                QuestionSchema(title = "Yup Dynamic Validation based on Array", creationDate = sampleDate),
                QuestionSchema(title = "how to set a GIF image as your desktop background", creationDate = sampleDate)
            )
        )
    }
}
