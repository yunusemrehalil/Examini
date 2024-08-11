package com.nomaddeveloper.examini.util

import android.os.CountDownTimer

class CountDownTimerUtil(
    millisInFuture: Long,
    countDownInterval: Long
) {
    private val millisUntilFinished = millisInFuture
    private var timer = InternalTimer(this, millisInFuture, countDownInterval)
    private var isRunning = false
    var onTick: ((millisUntilFinished: Long) -> Unit)? = null
    var onFinish: (() -> Unit)? = null

    private class InternalTimer(
        private val parent: CountDownTimerUtil,
        millisInFuture: Long,
        countDownInterval: Long
    ) : CountDownTimer(millisInFuture, countDownInterval) {
        var millisUntilFinished = parent.millisUntilFinished
        override fun onTick(millisUntilFinished: Long) {
            this.millisUntilFinished = millisUntilFinished
            parent.onTick?.invoke(millisUntilFinished)
        }

        override fun onFinish() {
            millisUntilFinished = 0
            parent.onFinish?.invoke()
        }
    }

    fun startTimer() {
        timer.start()
        isRunning = true
    }

    fun destroyTimer() {
        timer.cancel()
    }
}