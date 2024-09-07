package com.nomaddeveloper.examini.data.repository

import com.nomaddeveloper.examini.data.model.topic.LessonTopic
import com.nomaddeveloper.examini.data.source.remote.ExaminiAPI
import javax.inject.Inject

class LessonRepository @Inject constructor(private val examiniAPI: ExaminiAPI) {
    suspend fun getTopics(lesson: String, exam: String): ArrayList<LessonTopic> {
        return examiniAPI.getTopics(lesson, exam)
    }
}