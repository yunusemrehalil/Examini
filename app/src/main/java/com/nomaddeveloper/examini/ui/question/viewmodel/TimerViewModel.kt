package com.nomaddeveloper.examini.ui.question.viewmodel

import androidx.lifecycle.MutableLiveData
import com.nomaddeveloper.examini.ui.base.BaseViewModel
import com.nomaddeveloper.examini.util.CountDownTimerUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TimerViewModel @Inject constructor(private val countDownTimerUtil: CountDownTimerUtil) :
    BaseViewModel() {
    val timerLiveData = MutableLiveData<Long>()
    val timerFinishedLiveData = MutableLiveData<Boolean>()

    init {
        setupTimer()
    }

    private fun setupTimer() {
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