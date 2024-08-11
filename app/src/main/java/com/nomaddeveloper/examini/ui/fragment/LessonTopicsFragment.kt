package com.nomaddeveloper.examini.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nomaddeveloper.examini.adapter.TopicRecyclerViewAdapter
import com.nomaddeveloper.examini.databinding.FragmentLessonTopicsBinding
import com.nomaddeveloper.examini.listener.GetTopicsListener
import com.nomaddeveloper.examini.model.topic.LessonTopic
import com.nomaddeveloper.examini.util.Constant.KEY_EXAM
import com.nomaddeveloper.examini.util.Constant.KEY_LESSON
import com.nomaddeveloper.examini.util.Enums
import com.nomaddeveloper.examini.util.StringUtil.Companion.convertStringToExam
import com.nomaddeveloper.examini.util.StringUtil.Companion.convertStringToLesson
import com.nomaddeveloper.examini.util.ToastUtil.Companion.showErrorDialogTopicNotFound

class LessonTopicsFragment : BaseFragment(), GetTopicsListener {
    private lateinit var binding: FragmentLessonTopicsBinding
    private lateinit var allTopics: List<LessonTopic>
    private lateinit var exam: Enums.Exam
    private lateinit var lesson: Enums.Lesson
    private lateinit var topicRecyclerView: RecyclerView
    private lateinit var topicRecyclerViewAdapter: TopicRecyclerViewAdapter
    private lateinit var lessonTopicContext: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            val examString = it.getString(KEY_EXAM)
            val lessonString = it.getString(KEY_LESSON)
            exam = convertStringToExam(examString)
            lesson = convertStringToLesson(lessonString)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLessonTopicsBinding.inflate(layoutInflater)
        setupUI()
        return binding.root
    }

    private fun setupUI() {
        lessonTopicContext = requireContext()
        binding.apply {
            topicRecyclerView = rvTopics
        }
        topicRecyclerView.layoutManager =
            LinearLayoutManager(this.context, RecyclerView.VERTICAL, false)
        getAllTopics(lesson)
    }

    private fun getAllTopics(lesson: Enums.Lesson) {
        showLoading()
        connection.getAllTopics(this, lesson.name, exam.name)
    }

    companion object {
        @JvmStatic
        fun newInstance(exam: Enums.Exam, lesson: Enums.Lesson) =
            LessonTopicsFragment().apply {
                arguments = Bundle().apply {
                    putString(KEY_LESSON, lesson.name)
                    putString(KEY_EXAM, exam.name)
                }
            }
    }

    override fun onGetTopicsSuccess(topicsList: List<LessonTopic>) {
        hideLoading()
        allTopics = topicsList
        if (allTopics.isNotEmpty()) {
            allTopics = allTopics.sortedByDescending { it.frequency }
            topicRecyclerViewAdapter =
                TopicRecyclerViewAdapter(
                    allTopics,
                    parentFragmentManager,
                    realmCRUD,
                    googleProfile.id!!
                )
            topicRecyclerView.adapter = topicRecyclerViewAdapter
        } else {
            showErrorDialogTopicNotFound(
                lessonTopicContext,
                parentFragmentManager,
                LessonTopicsFragment::class.java.name,
                "Bu ders hakkında konu bulunamadı."
            )
        }
    }

    override fun onGetTopicsFailure(throwable: Throwable) {
        hideLoading()
        allTopics = emptyList()
        showErrorDialogTopicNotFound(
            lessonTopicContext,
            parentFragmentManager,
            LessonTopicsFragment::class.java.name,
            "Konuları alırken bir hata oluştu."
        )
    }
}