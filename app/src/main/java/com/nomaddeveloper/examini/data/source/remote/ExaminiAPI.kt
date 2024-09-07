package com.nomaddeveloper.examini.data.source.remote

import com.nomaddeveloper.examini.data.model.question.Question
import com.nomaddeveloper.examini.data.model.student.Student
import com.nomaddeveloper.examini.data.model.topic.LessonTopic
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ExaminiAPI {

    @GET("students")
    suspend fun getStudents(): ArrayList<Student>

    @GET("{lesson}/questions")
    suspend fun getQuestionsByLesson(@Path("lesson") lesson: String): ArrayList<Question>

    @GET("{lesson}/questions/topic")
    suspend fun getQuestionsByLessonAndTopic(
        @Path("lesson") lesson: String,
        @Query("topic") topic: String,
        @Query("lang") language: String
    ): ArrayList<Question>

    @GET("topics")
    suspend fun getTopics(
        @Query("lesson") lesson: String?,
        @Query("exam") exam: String?
    ): ArrayList<LessonTopic>
}
