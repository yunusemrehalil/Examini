package com.nomaddeveloper.examini.ui.question.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.nomaddeveloper.examini.data.model.question.Question
import com.nomaddeveloper.examini.data.repository.QuestionRepository
import com.nomaddeveloper.examini.ui.base.BaseViewModel
import com.nomaddeveloper.examini.util.Enums
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuestionViewModel @Inject constructor(private val questionRepository: QuestionRepository) :
    BaseViewModel() {
    private val _questions = MutableLiveData<ArrayList<Question>>()
    val questions: LiveData<ArrayList<Question>> = _questions

    fun fetchQuestions(lesson: String, topic: String) {
        showLoading()
        viewModelScope.launch {
            try {
                val questionList = questionRepository.getQuestionsByLessonAndTopic(
                    lesson,
                    topic,
                    Enums.Language.EN.name
                )
                _questions.value = questionList
                setError(null)
            } catch (e: Exception) {
                setError(e.message)
            } finally {
                hideLoading()
            }
        }
    }
}