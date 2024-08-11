package com.nomaddeveloper.examini.listener

import com.nomaddeveloper.examini.util.Enums

interface ExamRecyclerViewOnClickListener {
    fun onExamShortClick(exam: Enums.Exam)

    fun onExamLongClick(exam: Enums.Exam): Boolean
}