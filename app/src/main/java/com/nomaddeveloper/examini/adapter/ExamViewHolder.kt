package com.nomaddeveloper.examini.adapter

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nomaddeveloper.examini.R
import com.nomaddeveloper.examini.listener.ExamRecyclerViewOnClickListener
import com.nomaddeveloper.examini.util.Enums

class ExamViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val tvExamName: TextView = itemView.findViewById(R.id.tv_exam_name)

    fun bindTo(exam: Enums.Exam, examOnClickListener: ExamRecyclerViewOnClickListener) {
        tvExamName.setOnClickListener {
            examOnClickListener.onExamShortClick(exam)
        }
        tvExamName.setOnLongClickListener {
            examOnClickListener.onExamLongClick(exam)
        }
        tvExamName.text = exam.name
    }
}