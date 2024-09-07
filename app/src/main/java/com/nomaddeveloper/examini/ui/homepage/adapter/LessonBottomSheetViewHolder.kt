package com.nomaddeveloper.examini.ui.homepage.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.nomaddeveloper.examini.R
import com.nomaddeveloper.examini.ui.homepage.viewmodel.LessonViewModel
import com.nomaddeveloper.examini.util.Enums

class LessonBottomSheetViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val mbLessonButton: MaterialButton = itemView.findViewById(R.id.mb_lesson_button)

    fun bindTo(
        exam: Enums.Exam,
        lesson: Enums.Lesson,
        lessonViewModel: LessonViewModel
    ) {
        mbLessonButton.setOnClickListener {
            lessonViewModel.onLessonShortClick(exam, lesson)
        }
        mbLessonButton.setOnLongClickListener {
            lessonViewModel.onLessonLongClick(exam, lesson)
            true
        }
        mbLessonButton.text = lesson.name
        //mbLessonButton.text = getTurkishName(lesson)
    }
}