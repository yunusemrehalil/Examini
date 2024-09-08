package com.nomaddeveloper.examini.data.model.question

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
enum class QuestionLevel : Parcelable {
    DEFAULT,
    VERY_EASY,
    EASY,
    MEDIUM,
    HARD,
    VERY_HARD
}
