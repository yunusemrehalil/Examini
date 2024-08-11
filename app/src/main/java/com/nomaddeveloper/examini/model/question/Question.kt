package com.nomaddeveloper.examini.model.question

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Question(
    var id: String? = null,
    var lesson: Lesson,
    var topic: String,
    var image: String,
    var level: QuestionLevel,
    var estimatedSolvingTime: String,
    var correctAnswer: String
):Parcelable
