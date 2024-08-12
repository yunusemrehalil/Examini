package com.nomaddeveloper.examini.model.topic

import com.nomaddeveloper.examini.util.Enums

data class LessonTopic(
    val lesson: String,
    val topic: String,
    val frequency: Int,
    val language: Enums.Language
)