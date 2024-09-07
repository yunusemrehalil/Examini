package com.nomaddeveloper.examini.ui.homepage.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nomaddeveloper.examini.R
import com.nomaddeveloper.examini.ui.homepage.viewmodel.ExamViewModel
import com.nomaddeveloper.examini.util.Enums

class ExamRecyclerViewAdapter(
    examList: ArrayList<Enums.Exam>,
    examViewModel: ExamViewModel
) :
    RecyclerView.Adapter<ExamViewHolder>() {
    private val _examList = examList
    private val _examViewModel = examViewModel
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExamViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.exam_recycler_view_item, parent, false)
        return ExamViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return _examList.size
    }

    override fun onBindViewHolder(holder: ExamViewHolder, position: Int) {
        holder.bindTo(_examList[position], _examViewModel)
    }
}