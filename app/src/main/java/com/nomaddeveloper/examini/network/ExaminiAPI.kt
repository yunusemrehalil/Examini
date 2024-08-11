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
    fun getStudents(): Call<List<Student>>

    @GET("{lesson}/questions")
    fun getQuestionsByLesson(@Path("lesson") lesson: String): Call<List<Question>>

    @GET("{lesson}/questions/topic")
    fun getQuestionsByLessonAndTopic(
        @Path("lesson") lesson: String, @Query("topic") topic: String
    ): Call<List<Question>>

    @GET("topics")
    fun getTopics(
        @Query("lesson") lesson: String?,
        @Query("exam") exam: String?
    ): Call<List<LessonTopic>>
}
