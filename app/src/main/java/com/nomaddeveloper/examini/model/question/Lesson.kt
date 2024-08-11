package com.nomaddeveloper.examini.model.question

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
enum class Lesson : Parcelable {
    TURKISH,
    MATH,
    SOCIAL_SCIENCE,
    SCIENCE
}
