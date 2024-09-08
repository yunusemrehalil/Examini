package com.nomaddeveloper.examini.util

import javax.inject.Inject

class CountDownTimerUtilFactory @Inject constructor() {

    fun create(millisInFuture: Long, countDownInterval: Long): CountDownTimerUtil {
        return CountDownTimerUtil(millisInFuture, countDownInterval)
    }
}