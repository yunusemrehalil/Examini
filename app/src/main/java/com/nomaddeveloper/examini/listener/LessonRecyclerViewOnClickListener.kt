package com.nomaddeveloper.examini.listener

import com.nomaddeveloper.examini.util.Enums

interface LessonRecyclerViewOnClickListener {
    fun onLessonShortClick(exam: Enums.Exam, lesson: Enums.Lesson)

    fun onLessonLongClick(exam: Enums.Exam, lesson: Enums.Lesson): Boolean
}