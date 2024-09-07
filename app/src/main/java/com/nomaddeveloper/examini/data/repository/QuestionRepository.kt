package com.nomaddeveloper.examini.data.repository

import com.nomaddeveloper.examini.data.model.question.Question
import com.nomaddeveloper.examini.data.source.remote.ExaminiAPI
import javax.inject.Inject

class QuestionRepository @Inject constructor(private val examiniAPI: ExaminiAPI) {
    suspend fun getQuestionsByLesson(lesson: String): List<Question> {
        return examiniAPI.getQuestionsByLesson(lesson)
    }

    suspend fun getQuestionsByLessonAndTopic(
        lesson: String,
        topic: String,
        language: String
    ): ArrayList<Question> {
        return examiniAPI.getQuestionsByLessonAndTopic(lesson, topic, language)
    }
}