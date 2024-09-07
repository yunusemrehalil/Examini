package com.nomaddeveloper.examini.ui.question

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.viewModels
import com.google.android.material.button.MaterialButton
import com.nomaddeveloper.examini.R
import com.nomaddeveloper.examini.data.model.question.Question
import com.nomaddeveloper.examini.databinding.FragmentPrepareToQuestionBinding
import com.nomaddeveloper.examini.ui.base.BaseFragment
import com.nomaddeveloper.examini.ui.question.viewmodel.QuestionViewModel
import com.nomaddeveloper.examini.util.Constant
import com.nomaddeveloper.examini.util.Constant.DECIMAL_2_CHAR
import com.nomaddeveloper.examini.util.Constant.DEFAULT_LESSON
import com.nomaddeveloper.examini.util.Constant.DEFAULT_TOPIC
import com.nomaddeveloper.examini.util.Constant.SECONDS
import java.util.Locale

class PrepareToQuestionFragment : BaseFragment(), View.OnClickListener {
    private lateinit var binding: FragmentPrepareToQuestionBinding
    private lateinit var prepareToQuestionContext: Context
    private lateinit var startToSolveButton: MaterialButton
    private lateinit var currentTopic: String
    private lateinit var currentLesson: String
    private lateinit var questionList: ArrayList<Question>
    private lateinit var randomQuestion: Question
    private lateinit var lessonNameTV: TextView
    private lateinit var topicNameTV: TextView
    private lateinit var levelValueTV: TextView
    private lateinit var solvingTimeValueTV: TextView
    private lateinit var average6PointsValueTV: TextView

    private val questionViewModel: QuestionViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            currentTopic = it.getString(Constant.KEY_TOPIC, DEFAULT_TOPIC)
            currentLesson = it.getString(Constant.KEY_LESSON, DEFAULT_LESSON)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPrepareToQuestionBinding.inflate(layoutInflater)
        setupUseful()
        setupUI()
        setupObserver()
        getQuestionsByLessonAndTopic()
        return binding.root
    }

    private fun getQuestionsByLessonAndTopic() {
        questionViewModel.fetchQuestions(currentLesson, currentTopic)
    }


    private fun setupUseful() {
        prepareToQuestionContext = requireContext()
    }

    private fun setupUI() {
        binding.apply {
            startToSolveButton = mbPrepareQuestionStartToSolve
            lessonNameTV = tvPrepareQuestionLessonName
            topicNameTV = tvPrepareQuestionTopicName
            levelValueTV = tvPrepareQuestionLevelValue
            solvingTimeValueTV = tvPrepareQuestionTimeValue
            average6PointsValueTV = tvPrepareQuestionAverageValue
        }
        startToSolveButton.setOnClickListener(this)
    }

    private fun setupObserver() {
        questionViewModel.questions.observe(viewLifecycleOwner) { questions ->
            loadQuestions(questions)
        }
        baseViewModel.error.observe(viewLifecycleOwner) { error ->
            errorWhileRetrievingQuestions()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(lesson: String, topic: String) =
            PrepareToQuestionFragment().apply {
                arguments = Bundle().apply {
                    putString(Constant.KEY_LESSON, lesson)
                    putString(Constant.KEY_TOPIC, topic)
                }
            }
    }

    private fun loadQuestions(questionList: ArrayList<Question>) {
        if (questionList.isNotEmpty()) {
            this.questionList = questionList
            randomQuestion = questionList.random()
            lessonNameTV.text = currentLesson
            topicNameTV.text = currentTopic
            levelValueTV.text = stringUtil.levelToString(randomQuestion.level)
            val estimatedSolvingTimeText = "${randomQuestion.estimatedSolvingTime} $SECONDS"
            solvingTimeValueTV.text = estimatedSolvingTimeText
            average6PointsValueTV.text = getAverageOfLast6Points().toString()
        } else {
            toastUtil.showErrorDialogQuestionNotFound(
                this@PrepareToQuestionFragment,
                parentFragmentManager,
                getString(R.string.no_questions_found_for_topic)
            )
        }
        baseViewModel.hideLoading()
    }

    private fun errorWhileRetrievingQuestions() {
        baseViewModel.hideLoading()
        toastUtil.showErrorDialogQuestionNotFound(
            this@PrepareToQuestionFragment,
            parentFragmentManager,
            getString(R.string.error_while_retrieving_questions)
        )
    }

    private fun getAverageOfLast6Points(): Double {
        val points = realmCRUD.getLast6Points(googleProfile.id!!, currentLesson, currentTopic)
        return if (points.isNotEmpty()) {
            val average = points.map { it.value }.average()
            String.format(Locale.US, DECIMAL_2_CHAR, average).toDouble()
        } else {
            0.0
        }
    }

    override fun onClick(v: View?) {
        v?.let {
            when (it.id) {
                startToSolveButton.id -> {
                    navigateToSolveQuestionFragment()
                }

                else -> {
                    //ignored
                }
            }
        }
    }

    private fun navigateToSolveQuestionFragment() {
        val solveQuestionFragment =
            SolveQuestionFragment.newInstance(
                question = randomQuestion,
                questionList = questionList
            )
        parentFragmentManager.beginTransaction()
            .setCustomAnimations(
                R.anim.slide_in_right,
                R.anim.fade_out,
                R.anim.fade_in,
                R.anim.slide_out_left
            ).replace(
                R.id.lesson_container_fragment,
                solveQuestionFragment,
                SolveQuestionFragment::class.java.name
            ).commit()
    }
}