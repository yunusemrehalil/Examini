package com.nomaddeveloper.examini.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.nomaddeveloper.examini.R
import com.nomaddeveloper.examini.listener.LessonRecyclerViewOnClickListener
import com.nomaddeveloper.examini.util.Enums
import com.nomaddeveloper.examini.util.StringUtil.Companion.getTurkishName

class BottomSheetViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val mbLessonButton: MaterialButton = itemView.findViewById(R.id.mb_lesson_button)

    fun bindTo(
        exam: Enums.Exam,
        lesson: Enums.Lesson,
        lessonRecyclerViewOnClickListener: LessonRecyclerViewOnClickListener
    ) {
        mbLessonButton.setOnClickListener {
            lessonRecyclerViewOnClickListener.onLessonShortClick(exam, lesson)
        }
        mbLessonButton.setOnLongClickListener {
            lessonRecyclerViewOnClickListener.onLessonLongClick(exam, lesson)
        }
        mbLessonButton.text = getTurkishName(lesson)
    }
}