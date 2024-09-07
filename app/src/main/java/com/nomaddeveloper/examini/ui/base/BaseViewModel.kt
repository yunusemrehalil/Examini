package com.nomaddeveloper.examini.ui.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
open class BaseViewModel @Inject constructor() : ViewModel() {
    private val _loadingState = MutableLiveData<Boolean>()
    val loadingState: LiveData<Boolean> = _loadingState

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun showLoading() {
        _loadingState.value = true
    }

    fun hideLoading() {
        _loadingState.value = false
    }

    fun setError(message: String?) {
        _error.value = message
    }
}