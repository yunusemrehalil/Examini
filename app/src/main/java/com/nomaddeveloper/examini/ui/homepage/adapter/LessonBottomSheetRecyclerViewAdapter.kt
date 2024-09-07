package com.nomaddeveloper.examini.ui.homepage.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nomaddeveloper.examini.R
import com.nomaddeveloper.examini.ui.homepage.viewmodel.LessonViewModel
import com.nomaddeveloper.examini.util.Enums

class LessonBottomSheetRecyclerViewAdapter(
    exam: Enums.Exam,
    lessonList: ArrayList<Enums.Lesson>,
    lessonViewModel: LessonViewModel
) :
    RecyclerView.Adapter<LessonBottomSheetViewHolder>() {
    private val _exam = exam
    private val _lessonList = lessonList
    private val _lessonViewModel = lessonViewModel
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LessonBottomSheetViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.bottom_sheet_dialog_recycler_view_item, parent, false)
        return LessonBottomSheetViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return _lessonList.size
    }

    override fun onBindViewHolder(holder: LessonBottomSheetViewHolder, position: Int) {
        holder.bindTo(_exam, _lessonList[position], _lessonViewModel)
    }
}