package com.nomaddeveloper.examini.ui.homepage.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.nomaddeveloper.examini.ui.base.BaseViewModel
import com.nomaddeveloper.examini.util.Enums
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LessonViewModel @Inject constructor() : BaseViewModel() {
    private val _selectedLesson = MutableLiveData<Enums.Lesson>()
    val selectedLesson: LiveData<Enums.Lesson> get() = _selectedLesson

    private val _selectedExam = MutableLiveData<Enums.Exam>()
    val selectedExam: LiveData<Enums.Exam> get() = _selectedExam

    private val _longClickedLesson = MutableLiveData<Enums.Lesson>()
    val longClickedLesson: LiveData<Enums.Lesson> get() = _longClickedLesson

    private val _longClickedExam = MutableLiveData<Enums.Exam>()
    val longClickedExam: LiveData<Enums.Exam> get() = _longClickedExam


    fun onLessonShortClick(exam: Enums.Exam, lesson: Enums.Lesson) {
        _selectedExam.value = exam
        _selectedLesson.value = lesson
    }

    fun onLessonLongClick(exam: Enums.Exam, lesson: Enums.Lesson) {
        _longClickedExam.value = exam
        _longClickedLesson.value = lesson
    }
}