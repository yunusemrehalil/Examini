package com.nomaddeveloper.examini.listener

import com.nomaddeveloper.examini.model.student.Student


interface GetStudentsListener {
    fun onGetStudentsSuccess(studentList: List<Student>)

    fun onGetStudentsFailure(throwable: Throwable)
}
