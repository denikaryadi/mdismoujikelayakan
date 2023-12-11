package com.komida.co.id.mdisujikelayakan.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.komida.co.id.mdisujikelayakan.R
import com.komida.co.id.mdisujikelayakan.model.QuestionAnswerModel

class QuestionAnswerAdapter(private val questions: List<QuestionAnswerModel>) :
    RecyclerView.Adapter<QuestionAnswerAdapter.QuestionViewHolder>() {

    inner class QuestionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewQuestion: TextView = itemView.findViewById(R.id.textViewQuestion)
        val radioGroupAnswers: RadioGroup = itemView.findViewById(R.id.radioGroupAnswers)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item_question_answer, parent, false)
        return QuestionViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: QuestionViewHolder, position: Int) {
        val question = questions[position]
        holder.textViewQuestion.text = question.text
        holder.radioGroupAnswers.removeAllViews()

        for ((index, answer) in question.answers.withIndex()) {
            val radioButton = RadioButton(holder.itemView.context)
            radioButton.text = answer
            radioButton.id = index
            holder.radioGroupAnswers.addView(radioButton)

            radioButton.isChecked = index == question.selectedAnswerIndex
            radioButton.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    question.selectedAnswerIndex = index
                }
            }
        }
    }

    override fun getItemCount(): Int = questions.size
}