package com.nomaddeveloper.examini.ui.question.viewmodel

import androidx.lifecycle.MutableLiveData
import com.nomaddeveloper.examini.ui.base.BaseViewModel
import com.nomaddeveloper.examini.util.CountDownTimerUtil
import com.nomaddeveloper.examini.util.CountDownTimerUtilFactory
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TimerViewModel @Inject constructor(private val countDownTimerUtilFactory: CountDownTimerUtilFactory) :
    BaseViewModel() {
    val timerLiveData = MutableLiveData<Long>()
    val timerFinishedLiveData = MutableLiveData<Boolean>()

    private lateinit var countDownTimerUtil: CountDownTimerUtil

    init {
        setupTimer(0L, 1000L)
    }

    fun setupTimer(millisInFuture: Long, countDownInterval: Long) {
        countDownTimerUtil = countDownTimerUtilFactory.create(millisInFuture, countDownInterval)

        countDownTimerUtil.onTick = { millisUntilFinished ->
            timerLiveData.postValue(millisUntilFinished)
        }
        countDownTimerUtil.onFinish = {
            timerFinishedLiveData.postValue(true)
        }
    }

    fun startTimer() {
        countDownTimerUtil.startTimer()
    }

    fun pauseTimer() {
        countDownTimerUtil.pauseTimer()
    }

    fun resumeTimer() {
        countDownTimerUtil.resumeTimer()
    }

    fun restartTimer() {
        countDownTimerUtil.restartTimer()
    }

    fun destroyTimer() {
        countDownTimerUtil.destroyTimer()
    }

    override fun onCleared() {
        super.onCleared()
        destroyTimer()
    }
}