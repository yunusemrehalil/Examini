package com.nomaddeveloper.examini.ui.lesson.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.nomaddeveloper.examini.data.model.topic.LessonTopic
import com.nomaddeveloper.examini.data.repository.LessonRepository
import com.nomaddeveloper.examini.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LessonViewModel @Inject constructor(private val lessonRepository: LessonRepository) :
    BaseViewModel() {
    private val _topics = MutableLiveData<ArrayList<LessonTopic>>()
    val topics: LiveData<ArrayList<LessonTopic>> = _topics

    fun fetchTopics(lesson: String, exam: String) {
        showLoading()
        viewModelScope.launch {
            try {
                val topicList = lessonRepository.getTopics(lesson, exam)
                _topics.value = topicList
                setError(null)
            } catch (e: Exception) {
                setError(e.message)
            } finally {
                hideLoading()
            }
        }
    }
}