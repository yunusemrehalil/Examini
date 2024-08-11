package com.nomaddeveloper.examini.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nomaddeveloper.examini.R
import com.nomaddeveloper.examini.listener.LessonRecyclerViewOnClickListener
import com.nomaddeveloper.examini.util.Enums

class LessonBottomSheetRecyclerViewAdapter(
    exam: Enums.Exam,
    lessonList: ArrayList<Enums.Lesson>,
    onClickListener: LessonRecyclerViewOnClickListener
) :
    RecyclerView.Adapter<BottomSheetViewHolder>() {
    private val _exam = exam
    private val _lessonList = lessonList
    private val _onClickListener = onClickListener
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BottomSheetViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.bottom_sheet_dialog_recycler_view_item, parent, false)
        return BottomSheetViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return _lessonList.size
    }

    override fun onBindViewHolder(holder: BottomSheetViewHolder, position: Int) {
        holder.bindTo(_exam, _lessonList[position], _onClickListener)
    }
}