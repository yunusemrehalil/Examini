package com.nomaddeveloper.examini.listener

import com.nomaddeveloper.examini.model.question.Question

interface GetQuestionsListener {
    fun onGetQuestionsSuccess(questionList: List<Question>)

    fun onGetQuestionsFailure(throwable: Throwable)
}
