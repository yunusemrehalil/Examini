package com.nomaddeveloper.examini.listener

import com.nomaddeveloper.examini.model.topic.LessonTopic

interface GetTopicsListener {
    fun onGetTopicsSuccess(topicsList: List<LessonTopic>)

    fun onGetTopicsFailure(throwable: Throwable)
}