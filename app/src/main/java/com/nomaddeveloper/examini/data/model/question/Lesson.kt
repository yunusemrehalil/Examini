package com.nomaddeveloper.examini.data.model.question

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
enum class Lesson : Parcelable {
    TURKISH,
    MATH,
    SOCIAL_SCIENCE,
    SCIENCE
}
