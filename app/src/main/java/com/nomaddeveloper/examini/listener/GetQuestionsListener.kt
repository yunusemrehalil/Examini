package com.nomaddeveloper.examini.listener

import com.nomaddeveloper.examini.model.question.Question

interface GetQuestionsListener {
    fun onGetQuestionsSuccess(questionList: ArrayList<Question>)

    fun onGetQuestionsFailure(throwable: Throwable)
}
