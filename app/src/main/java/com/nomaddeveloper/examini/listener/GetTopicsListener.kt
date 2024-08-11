package com.nomaddeveloper.examini.listener

import com.nomaddeveloper.examini.model.topic.LessonTopic

interface GetTopicsListener {
    fun onGetTopicsSuccess(topicsList: ArrayList<LessonTopic>)

    fun onGetTopicsFailure(throwable: Throwable)
}