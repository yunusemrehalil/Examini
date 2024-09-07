package com.nomaddeveloper.examini.ui.lesson

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nomaddeveloper.examini.R
import com.nomaddeveloper.examini.data.model.topic.LessonTopic
import com.nomaddeveloper.examini.databinding.FragmentLessonTopicsBinding
import com.nomaddeveloper.examini.ui.base.BaseFragment
import com.nomaddeveloper.examini.ui.lesson.adapter.TopicRecyclerViewAdapter
import com.nomaddeveloper.examini.ui.lesson.viewmodel.LessonViewModel
import com.nomaddeveloper.examini.util.Constant.KEY_EXAM
import com.nomaddeveloper.examini.util.Constant.KEY_LESSON
import com.nomaddeveloper.examini.util.Enums

class LessonTopicsFragment : BaseFragment() {
    private lateinit var binding: FragmentLessonTopicsBinding
    private lateinit var allTopics: List<LessonTopic>
    private lateinit var exam: Enums.Exam
    private lateinit var lesson: Enums.Lesson
    private lateinit var topicRecyclerView: RecyclerView
    private lateinit var topicRecyclerViewAdapter: TopicRecyclerViewAdapter
    private lateinit var lessonTopicContext: Context

    private val lessonViewModel: LessonViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            val examString = it.getString(KEY_EXAM)
            val lessonString = it.getString(KEY_LESSON)
            exam = stringUtil.convertStringToExam(examString)
            lesson = stringUtil.convertStringToLesson(lessonString)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLessonTopicsBinding.inflate(layoutInflater)
        setupObserver()
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
        getAllTopics()
    }

    private fun setupObserver() {
        lessonViewModel.topics.observe(viewLifecycleOwner) { topics ->
            loadTopics(topics)
        }

        lessonViewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let {
                errorWhileRetrievingTopics()
            }
        }
    }

    private fun getAllTopics() {
        lessonViewModel.fetchTopics(lesson.name, exam.name)
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

    private fun loadTopics(topicsList: ArrayList<LessonTopic>) {
        baseViewModel.hideLoading()
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
            topicRecyclerViewAdapter.setHasStableIds(true)
            topicRecyclerView.setHasFixedSize(true)
            topicRecyclerView.adapter = topicRecyclerViewAdapter
        } else {
            toastUtil.showErrorDialogTopicNotFound(
                this@LessonTopicsFragment,
                parentFragmentManager,
                getString(R.string.no_topic_found_for_lesson)
            )
        }
    }

    private fun errorWhileRetrievingTopics() {
        baseViewModel.hideLoading()
        allTopics = emptyList()
        toastUtil.showErrorDialogTopicNotFound(
            this@LessonTopicsFragment,
            parentFragmentManager,
            getString(R.string.error_while_retrieving_topics)
        )
    }
}