package com.nomaddeveloper.examini.ui.homepage.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.nomaddeveloper.examini.ui.base.BaseViewModel
import com.nomaddeveloper.examini.util.Enums
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ExamViewModel @Inject constructor() : BaseViewModel() {
    private val _selectedExam = MutableLiveData<Enums.Exam>()
    val selectedExam: LiveData<Enums.Exam> get() = _selectedExam

    private val _longClickedExam = MutableLiveData<Enums.Exam>()
    val longClickedExam: LiveData<Enums.Exam> get() = _longClickedExam

    fun onExamShortClick(exam: Enums.Exam) {
        _selectedExam.value = exam
    }

    fun onExamLongClick(exam: Enums.Exam) {
        _longClickedExam.value = exam
    }
}