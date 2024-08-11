package com.nomaddeveloper.examini.network

import com.google.gson.GsonBuilder
import com.nomaddeveloper.examini.BuildConfig
import com.nomaddeveloper.examini.listener.GetQuestionsListener
import com.nomaddeveloper.examini.listener.GetStudentsListener
import com.nomaddeveloper.examini.listener.GetTopicsListener
import com.nomaddeveloper.examini.model.question.Question
import com.nomaddeveloper.examini.model.student.Student
import com.nomaddeveloper.examini.model.topic.LessonTopic
import com.nomaddeveloper.examini.util.LocalDateTimeAdapter
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDateTime

class Connection {
    private lateinit var examiniAPI: ExaminiAPI

    init {
        createUniversityAPI()
    }

    fun getAllStudents(getStudentsListener: GetStudentsListener) {
        examiniAPI.getStudents().enqueue(object : Callback<List<Student>> {
            override fun onResponse(call: Call<List<Student>>, response: Response<List<Student>>) {
                if (response.isSuccessful && response.body() != null) {
                    getStudentsListener.onGetStudentsSuccess(response.body()!!)
                } else {
                    getStudentsListener.onGetStudentsFailure(Exception("Failed to get all students."))
                }
            }

            override fun onFailure(call: Call<List<Student>>, throwable: Throwable) {
                getStudentsListener.onGetStudentsFailure(throwable)
            }
        })
    }

    fun getAllTopics(getTopicsListener: GetTopicsListener, lesson: String, exam: String) {
        examiniAPI.getTopics(lesson, exam).enqueue(object : Callback<List<LessonTopic>> {
            override fun onResponse(
                call: Call<List<LessonTopic>>,
                response: Response<List<LessonTopic>>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    getTopicsListener.onGetTopicsSuccess(response.body()!!)
                } else {
                    getTopicsListener.onGetTopicsFailure(Exception("Failed to get all topics."))
                }
            }

            override fun onFailure(call: Call<List<LessonTopic>>, throwable: Throwable) {
                getTopicsListener.onGetTopicsFailure(throwable)
            }
        })
    }

    fun getQuestionsByLessonAndTopic(
        getQuestionsListener: GetQuestionsListener,
        lesson: String,
        topic: String
    ) {
        examiniAPI.getQuestionsByLessonAndTopic(lesson, topic)
            .enqueue(object : Callback<List<Question>> {
                override fun onResponse(
                    call: Call<List<Question>>,
                    response: Response<List<Question>>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        getQuestionsListener.onGetQuestionsSuccess(response.body()!!)
                    } else {
                        getQuestionsListener.onGetQuestionsFailure(Exception("Failed to get questions."))
                    }
                }

                override fun onFailure(call: Call<List<Question>>, throwable: Throwable) {
                    getQuestionsListener.onGetQuestionsFailure(throwable)
                }

            })
    }

    private fun createUniversityAPI() {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

        val gson = GsonBuilder()
            .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeAdapter())
            .create()

        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(BaseInterceptor())
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
            .build()

        examiniAPI = retrofit.create(ExaminiAPI::class.java)
    }
}
