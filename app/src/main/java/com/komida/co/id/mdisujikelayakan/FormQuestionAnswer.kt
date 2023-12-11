package com.komida.co.id.mdisujikelayakan

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.komida.co.id.mdisujikelayakan.adapter.QuestionAnswerAdapter
import com.komida.co.id.mdisujikelayakan.model.QuestionAnswerModel

class FormQuestionAnswer : AppCompatActivity() {

    private val questions = mutableListOf(
        QuestionAnswerModel(1, "Question 1", listOf("Answer 1", "Answer 2", "Answer 3", "Answer 4")),
        QuestionAnswerModel(2, "Question 2", listOf("Answer A", "Answer B", "Answer C", "Answer D")),
        // Add more questions here
    )

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: QuestionAnswerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_question_answer)

        recyclerView = findViewById(R.id.recyclerViewQuestions)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = QuestionAnswerAdapter(questions)
        recyclerView.adapter = adapter
    }
}
