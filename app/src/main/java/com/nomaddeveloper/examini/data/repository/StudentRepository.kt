package com.nomaddeveloper.examini.data.repository

import com.nomaddeveloper.examini.data.model.student.Student
import com.nomaddeveloper.examini.data.source.remote.ExaminiAPI
import javax.inject.Inject

class StudentRepository @Inject constructor(private val examiniAPI: ExaminiAPI) {
    suspend fun getStudents(): ArrayList<Student> {
        return examiniAPI.getStudents()
    }
}