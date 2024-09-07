package com.nomaddeveloper.examini.data.model.question

import android.os.Parcelable
import com.nomaddeveloper.examini.util.Enums
import kotlinx.parcelize.Parcelize

@Parcelize
data class Question(
    var id: String? = null,
    var lesson: Lesson,
    var topic: String,
    var image: String,
    var level: QuestionLevel,
    var estimatedSolvingTime: String,
    var correctAnswer: String,
    var language: Enums.Language
) : Parcelable
