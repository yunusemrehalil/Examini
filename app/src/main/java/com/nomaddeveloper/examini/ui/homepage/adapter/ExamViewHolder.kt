package com.nomaddeveloper.examini.ui.homepage.adapter

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nomaddeveloper.examini.R
import com.nomaddeveloper.examini.ui.homepage.viewmodel.ExamViewModel
import com.nomaddeveloper.examini.util.Enums

class ExamViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val tvExamName: TextView = itemView.findViewById(R.id.tv_exam_name)

    fun bindTo(exam: Enums.Exam, examViewModel: ExamViewModel) {
        tvExamName.setOnClickListener {
            examViewModel.onExamShortClick(exam)
        }
        tvExamName.setOnLongClickListener {
            examViewModel.onExamLongClick(exam)
            true
        }
        tvExamName.text = exam.name
    }
}