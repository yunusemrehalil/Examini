package com.nomaddeveloper.examini.model.question

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
enum class QuestionLevel : Parcelable {
    VERY_EASY,
    EASY,
    MEDIUM,
    HARD,
    VERY_HARD
}
