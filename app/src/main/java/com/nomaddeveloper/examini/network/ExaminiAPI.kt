package com.nomaddeveloper.examini.network

import com.nomaddeveloper.examini.model.question.Question
import com.nomaddeveloper.examini.model.student.Student
import com.nomaddeveloper.examini.model.topic.LessonTopic
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ExaminiAPI {

    @GET("students")
    fun getStudents(): Call<ArrayList<Student>>

    @GET("{lesson}/questions")
    fun getQuestionsByLesson(@Path("lesson") lesson: String): Call<ArrayList<Question>>

    @GET("{lesson}/questions/topic")
    fun getQuestionsByLessonAndTopic(
        @Path("lesson") lesson: String,
        @Query("topic") topic: String,
        @Query("lang") language: String
    ): Call<ArrayList<Question>>

    @GET("topics")
    fun getTopics(
        @Query("lesson") lesson: String?,
        @Query("exam") exam: String?
    ): Call<ArrayList<LessonTopic>>
}
