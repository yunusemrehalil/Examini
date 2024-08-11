package com.nomaddeveloper.examini.ui.fragment

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RadioGroup
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.ai.client.generativeai.type.Content
import com.google.ai.client.generativeai.type.GenerateContentResponse
import com.google.ai.client.generativeai.type.content
import com.google.android.material.button.MaterialButton
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.nomaddeveloper.examini.R
import com.nomaddeveloper.examini.databinding.FragmentSolveQuestionBinding
import com.nomaddeveloper.examini.listener.GlideErrorListener
import com.nomaddeveloper.examini.model.question.Question
import com.nomaddeveloper.examini.util.ChangeQuestionAlertDialog
import com.nomaddeveloper.examini.util.Constant.DEFAULT_CORRECT_ANSWER
import com.nomaddeveloper.examini.util.Constant.DEFAULT_IMAGE
import com.nomaddeveloper.examini.util.Constant.DEFAULT_LESSON
import com.nomaddeveloper.examini.util.Constant.DEFAULT_LEVEL
import com.nomaddeveloper.examini.util.Constant.DEFAULT_SOLVING_TIME
import com.nomaddeveloper.examini.util.Constant.DEFAULT_TOPIC
import com.nomaddeveloper.examini.util.Constant.KEY_QUESTION_CORRECT_ANSWER
import com.nomaddeveloper.examini.util.Constant.KEY_QUESTION_IMAGE
import com.nomaddeveloper.examini.util.Constant.KEY_QUESTION_LESSON
import com.nomaddeveloper.examini.util.Constant.KEY_QUESTION_LEVEL
import com.nomaddeveloper.examini.util.Constant.KEY_QUESTION_LIST
import com.nomaddeveloper.examini.util.Constant.KEY_QUESTION_SOLVING_TIME
import com.nomaddeveloper.examini.util.Constant.KEY_QUESTION_TOPIC
import com.nomaddeveloper.examini.util.Constant.OPTION_A
import com.nomaddeveloper.examini.util.Constant.OPTION_B
import com.nomaddeveloper.examini.util.Constant.OPTION_C
import com.nomaddeveloper.examini.util.Constant.OPTION_D
import com.nomaddeveloper.examini.util.Constant.OPTION_E
import com.nomaddeveloper.examini.util.Constant.REGEX_PATTERN_FOR_REMOVE_ASTERISKS
import com.nomaddeveloper.examini.util.Constant.SECONDS
import com.nomaddeveloper.examini.util.CountDownTimerUtil
import com.nomaddeveloper.examini.util.StringUtil.Companion.levelToString
import com.nomaddeveloper.examini.util.ToastUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

class SolveQuestionFragment : BaseFragment(), View.OnClickListener {
    private lateinit var binding: FragmentSolveQuestionBinding
    private lateinit var solveQuestionContext: Context
    private lateinit var countDownTimerUtil: CountDownTimerUtil
    private lateinit var sendMyAnswerButton: MaterialButton
    private lateinit var changeQuestionButton: MaterialButton
    private lateinit var lessonNameTV: TextView
    private lateinit var countDownTimerLPI: LinearProgressIndicator
    private lateinit var secondsLeftTV: TextView
    private lateinit var optionsRG: RadioGroup
    private lateinit var randomTestQuestionIV: ImageView
    private lateinit var geminiContent: Content
    private lateinit var questionList: List<Question>
    private lateinit var questionTopic: String
    private lateinit var questionLesson: String
    private lateinit var questionLevel: String
    private lateinit var questionImage: String
    private lateinit var questionCorrectAnswer: String
    private lateinit var questionEstimatedSolvingTime: String
    private lateinit var glideErrorListener: GlideErrorListener
    private var geminiResponse: GenerateContentResponse? = null
    private var selectedAnswer: String? = null
    private var secondsLeft: Int = 0
    private var clockTime: Long = 0
    private var progressTime: Float = 0f

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    countDownTimerUtil.destroyTimer()
                    realmCRUD.addPointToRealm(
                        googleProfile.id!!,
                        questionLesson,
                        questionTopic,
                        0f
                    )
                    parentFragmentManager.beginTransaction()
                        .setCustomAnimations(
                            R.anim.slide_in_right,
                            R.anim.fade_out,
                            R.anim.fade_in,
                            R.anim.slide_out_left
                        )
                        .remove(this@SolveQuestionFragment)
                        .commit()
                    requireActivity().finish()
                    //TODO() check this
                }
            })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            questionLesson = it.getString(KEY_QUESTION_LESSON, DEFAULT_LESSON)
            questionTopic = it.getString(KEY_QUESTION_TOPIC, DEFAULT_TOPIC)
            questionImage = it.getString(KEY_QUESTION_IMAGE, DEFAULT_IMAGE)
            questionLevel = it.getString(KEY_QUESTION_LEVEL, DEFAULT_LEVEL)
            questionCorrectAnswer =
                it.getString(KEY_QUESTION_CORRECT_ANSWER, DEFAULT_CORRECT_ANSWER)
            questionEstimatedSolvingTime =
                it.getString(KEY_QUESTION_SOLVING_TIME, DEFAULT_SOLVING_TIME)
            questionList = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                it.getParcelableArrayList(KEY_QUESTION_LIST, Question::class.java)
                    ?: arrayListOf()
            } else {
                it.getParcelableArrayList(KEY_QUESTION_LIST) ?: arrayListOf()
            }
        }
        clockTime = (questionEstimatedSolvingTime.toInt() * 1000).toLong()
        progressTime = (clockTime / 1000).toFloat()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSolveQuestionBinding.inflate(layoutInflater)
        setupUseful()
        setupUI()
        return binding.root
    }

    private fun setupUseful() {
        solveQuestionContext = requireContext()
        countDownTimerUtil = CountDownTimerUtil(clockTime, 1000)
        glideErrorListener = GlideErrorListener(
            solveQuestionContext,
            parentFragmentManager,
            SolveQuestionFragment::class.java.name,
            getString(R.string.error_loading_question),
            countDownTimerUtil
        )
    }

    private fun setupUI() {
        binding.apply {
            sendMyAnswerButton = mbSolveQuestionSendMyAnswer
            changeQuestionButton = mbSolveQuestionChangeQuestion
            randomTestQuestionIV = ivSolveQuestionRandomTestQuestion
            lessonNameTV = tvSolveQuestionLessonName
            secondsLeftTV = tvSolveQuestionSecondsLeft
            countDownTimerLPI = lpiSolveQuestionCountdownTimer
            optionsRG = rgSolveQuestionOptions
        }
        sendMyAnswerButton.setOnClickListener(this)
        changeQuestionButton.setOnClickListener(this)
        setQuestionImage()
        setupOptionsListener()
        setupCountDownTimer()
        countDownTimerUtil.startTimer()
    }

    private fun setQuestionImage() {
        Glide.with(this)
            .load(questionImage)
            .placeholder(R.drawable.placeholder_question_loading)
            .transform(RoundedCorners(16))
            .listener(glideErrorListener)
            .into(randomTestQuestionIV)
    }

    private fun setupOptionsListener() {
        optionsRG.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rb_A -> selectedAnswer = OPTION_A
                R.id.rb_B -> selectedAnswer = OPTION_B
                R.id.rb_C -> selectedAnswer = OPTION_C
                R.id.rb_D -> selectedAnswer = OPTION_D
                R.id.rb_E -> selectedAnswer = OPTION_E
            }
        }
    }

    private fun setupCountDownTimer() {
        countDownTimerLPI.max = progressTime.toInt()
        countDownTimerLPI.progress = progressTime.toInt()
        secondsLeft = 0
        countDownTimerUtil.onTick = { millisUntilFinished ->
            val second = (millisUntilFinished / 1000.0f).roundToInt()
            if (second != secondsLeft) {
                secondsLeft = second
                val secondsLeftText =
                    resources.getQuantityString(R.plurals.seconds_left, secondsLeft, secondsLeft)
                secondsLeftTV.text = secondsLeftText
                countDownTimerLPI.progress = secondsLeft
            }
        }
        countDownTimerUtil.onFinish = {
            // todo()
        }
    }

    private fun extractPointFromResponse(response: String): Float? {
        val cleanedResponse = response.replace(Regex(REGEX_PATTERN_FOR_REMOVE_ASTERISKS), "").trim()
        val keywordIndex = cleanedResponse.indexOf("point out of 5.").takeIf { it != -1 }
            ?: cleanedResponse.indexOf("points out of 5.")
        if (keywordIndex == -1) return null
        val beforeKeyword = cleanedResponse.substring(0, keywordIndex).trim()
        val numberString = beforeKeyword.split(" ").lastOrNull()?.trim()
        return numberString?.toFloatOrNull()
    }


    private suspend fun generateGeminiContent() {
        showLoading()
        countDownTimerUtil.destroyTimer()
        val time: Int =
            (questionEstimatedSolvingTime.toInt() - secondsLeft)
        val myAnswer = selectedAnswer
        geminiContent = content {
            text("input: For this question, I answered option A, and the correct answer is option B. I found my answer in 25 seconds, and the expert-estimated solution time is 60 seconds. I will be taking the university entrance exam this year. Could you rate me between 0 and 5 points? Please don't solve the question.")
            text("output: Although your response time is impressive, accuracy is critical. Since your answer is incorrect, your score will be low. Therefore, you can receive 1 point out of 5.")

            text("input: For this question, I answered option C, and the correct answer is option A. I found my answer in 30 seconds, and the expert-estimated solution time is 120 seconds. I will be taking the university entrance exam this year. Could you rate me between 0 and 5 points? Please don't solve the question.")
            text("output: Despite your quick response, the incorrect answer significantly impacts your score. You can receive 1 point out of 5.")

            text("input: For this question, I answered option D, and the correct answer is option B. I found my answer in 50 seconds, and the expert-estimated solution time is 60 seconds. I will be taking the university entrance exam this year. Could you rate me between 0 and 5 points? Please don't solve the question.")
            text("output: Your timing is close to the estimate, but the incorrect answer means a low score. Therefore, you can receive 0 points out of 5.")

            text("input: For this question, I answered option C, and the correct answer is option A. I found my answer in 45 seconds, and the expert-estimated solution time is 90 seconds. I will be taking the university entrance exam this year. Could you rate me between 0 and 5 points? Please don't solve the question.")
            text("output: Your response time is good, but accuracy is key. Since the answer is wrong, your score will reflect that. Therefore, you can receive 1 point out of 5.")

            text("input: For this question, I answered option B, and the correct answer is option C. I found my answer in 80 seconds, and the expert-estimated solution time is 60 seconds. I will be taking the university entrance exam this year. Could you rate me between 0 and 5 points? Please don't solve the question.")
            text("output: You took longer than the estimated time and provided an incorrect answer. This will result in a very low score. Therefore, you can receive 0 points out of 5.")

            text("input: For this question, I answered option E, and the correct answer is option D. I found my answer in 40 seconds, and the expert-estimated solution time is 70 seconds. I will be taking the university entrance exam this year. Could you rate me between 0 and 5 points? Please don't solve the question.")
            text("output: While your answer time is decent, the incorrect answer significantly reduces your score. Therefore, you can receive 1 point out of 5.")

            text("input: For this question, I answered option B, and the correct answer is option A. I found my answer in 65 seconds, and the expert-estimated solution time is 60 seconds. I will be taking the university entrance exam this year. Could you rate me between 0 and 5 points? Please don't solve the question.")
            text("output: Your answer time exceeds the estimated duration, and the answer is incorrect. As a result, you can receive 0 points out of 5.")

            text("input: For this question, I answered option C, and the correct answer is option B. I found my answer in 55 seconds, and the expert-estimated solution time is 90 seconds. I will be taking the university entrance exam this year. Could you rate me between 0 and 5 points? Please don't solve the question.")
            text("output: Your response time is reasonable, but the incorrect answer significantly lowers your score. Therefore, you can receive 1 point out of 5.")

            text("input: For this question, I answered option A, and the correct answer is option C. I found my answer in 75 seconds, and the expert-estimated solution time is 60 seconds. I will be taking the university entrance exam this year. Could you rate me between 0 and 5 points? Please don't solve the question.")
            text("output: You took longer than the estimated time, and your answer is incorrect. This leads to a low score. Therefore, you can receive 0 points out of 5.")

            text("input: For this question, I answered option D, and the correct answer is option A. I found my answer in 30 seconds, and the expert-estimated solution time is 80 seconds. I will be taking the university entrance exam this year. Could you rate me between 0 and 5 points? Please don't solve the question.")
            text("output: Despite your quick response, the incorrect answer lowers your score significantly. Therefore, you can receive 1 point out of 5.")

            text("input: For this question, I answered option C, and the correct answer is option E. I found my answer in 100 seconds, and the expert-estimated solution time is 90 seconds. I will be taking the university entrance exam this year. Could you rate me between 0 and 5 points? Please don't solve the question.")
            text("output: Your response time was slightly longer than the estimate, and your answer is incorrect. This results in a low score. Therefore, you can receive 0 points out of 5.")

            text("input: For this question, I answered option A, and the correct answer is option B. I found my answer in 20 seconds, and the expert-estimated solution time is 50 seconds. I will be taking the university entrance exam this year. Could you rate me between 0 and 5 points? Please don't solve the question.")
            text("output: Although you responded quickly, the incorrect answer impacts your score negatively. Therefore, you can receive 1 point out of 5.")

            text("input: For this question, I answered option D, and the correct answer is option C. I found my answer in 60 seconds, and the expert-estimated solution time is 90 seconds. I will be taking the university entrance exam this year. Could you rate me between 0 and 5 points? Please don't solve the question.")
            text("output: Your response time is within the expected range, but the incorrect answer results in a low score. Therefore, you can receive 1 point out of 5.")

            text("input: For this question, I answered option E, and the correct answer is option A. I found my answer in 35 seconds, and the expert-estimated solution time is 70 seconds. I will be taking the university entrance exam this year. Could you rate me between 0 and 5 points? Please don't solve the question.")
            text("output: While your timing is decent, the incorrect answer lowers your score. Therefore, you can receive 1 point out of 5.")

            text("input: For this question, I answered option B, and the correct answer is option D. I found my answer in 50 seconds, and the expert-estimated solution time is 50 seconds. I will be taking the university entrance exam this year. Could you rate me between 0 and 5 points? Please don't solve the question.")
            text("output: Your timing is on point, but the incorrect answer leads to a low score. Therefore, you can receive 1 point out of 5.")

            text("input:  For this question, I answered option A, and the correct answer is option D. I found my answer in 45 seconds, and the expert-estimated solution time is 75 seconds. I will be taking the university entrance exam this year. Could you rate me between 0 and 5 points? Please don't solve the question.")
            text("output: Your response time is within a reasonable range, but the incorrect answer significantly impacts your score. Therefore, you can receive 1 point out of 5.")

            text("input:  For this question, I answered option B, and the correct answer is option E. I found my answer in 70 seconds, and the expert-estimated solution time is 60 seconds. I will be taking the university entrance exam this year. Could you rate me between 0 and 5 points? Please don't solve the question.")
            text("output: You exceeded the estimated time and provided an incorrect answer, resulting in a low score. Therefore, you can receive 0 points out of 5.")

            text("input: For this question, I answered option C, and the correct answer is option A. I found my answer in 60 seconds, and the expert-estimated solution time is 100 seconds. I will be taking the university entrance exam this year. Could you rate me between 0 and 5 points? Please don't solve the question")
            text("output: Your response time is efficient, but the incorrect answer lowers your score. Therefore, you can receive 1 point out of 5.")

            text("input: For this question, I answered option D, and the correct answer is option D. I found my answer in 65 seconds, and the expert-estimated solution time is 50 seconds. I will be taking the university entrance exam this year. Could you rate me between 0 and 5 points? Please don't solve the question.")
            text("output: While you exceeded the estimated time, you provided the correct answer. This shows good understanding. You can receive 3 points out of 5.")

            text("input: For this question, I answered option A, and the correct answer is option A. I found my answer in 30 seconds, and the expert-estimated solution time is 65 seconds. I will be taking the university entrance exam this year. Could you rate me between 0 and 5 points? Please don't solve the question.")
            text("output: Your response time is impressive, and you answered correctly. This indicates excellent problem-solving skills. You can receive 5 points out of 5.")

            text("input: For this question, I answered option C, and the correct answer is option C. I found my answer in 55 seconds, and the expert-estimated solution time is 80 seconds. I will be taking the university entrance exam this year. Could you rate me between 0 and 5 points? Please don't solve the question.")
            text("output: Your response time is within a reasonable range, and you answered correctly. This shows good understanding. You can receive 3 points out of 5.")

            text("input: For this question, I answered option B, and the correct answer is option B. I found my answer in 40 seconds, and the expert-estimated solution time is 75 seconds. I will be taking the university entrance exam this year. Could you rate me between 0 and 5 points? Please don't solve the question.")
            text("output: Your response time is efficient, and you answered correctly. This demonstrates strong problem-solving skills. You can receive 4 points out of 5.")

            text("input: For this question, I answered option D, and the correct answer is option D. I found my answer in 70 seconds, and the expert-estimated solution time is 60 seconds. I will be taking the university entrance exam this year. Could you rate me between 0 and 5 points? Please don't solve the question.")
            text("output: While you took slightly longer than the estimated time, you provided the correct answer. This shows good understanding. You can receive 3 points out of 5.")

            text("input: For this question, I answered option A, and the correct answer is option A. I found my answer in 25 seconds, and the expert-estimated solution time is 50 seconds. I will be taking the university entrance exam this year. Could you rate me between 0 and 5 points? Please don't solve the question.")
            text("output: Your response time is impressive, and you answered correctly. This indicates excellent problem-solving skills. You can receive 5 points out of 5.")

            text("input: For this question, I answered option C, and the correct answer is option C. I found my answer in 60 seconds, and the expert-estimated solution time is 90 seconds. I will be taking the university entrance exam this year. Could you rate me between 0 and 5 points? Please don't solve the question.")
            text("output: Your response time is efficient, and you answered correctly. This demonstrates good problem-solving skills. You can receive 4 points out of 5.")

            text("input: For this question, I answered option B, and the correct answer is option B. I found my answer in 50 seconds, and the expert-estimated solution time is 45 seconds. I will be taking the university entrance exam this year. Could you rate me between 0 and 5 points? Please don't solve the question.")
            text("output: While your response time is slightly above the estimate, you answered correctly. This indicates solid understanding. You can receive 3 points out of 5.")

            text("input: For this question, I answered option D, and the correct answer is option D. I found my answer in 45 seconds, and the expert-estimated solution time is 75 seconds. I will be taking the university entrance exam this year. Could you rate me between 0 and 5 points? Please don't solve the question.")
            text("output: Your response time is within a reasonable range, and you provided the correct answer. This demonstrates good problem-solving skills. You can receive 4 points out of 5.")

            text("input: For this question, I answered option A, and the correct answer is option A. I found my answer in 35 seconds, and the expert-estimated solution time is 60 seconds. I will be taking the university entrance exam this year. Could you rate me between 0 and 5 points? Please don't solve the question.")
            text("output: Your response time is efficient, and providing the correct answer is impressive. Given your performance, you can receive 4 points out of 5.")

            text("input: For this question, I answered option $myAnswer, and the correct answer is option $questionCorrectAnswer. I found my answer in $time seconds, and the expert-estimated solution time is $questionEstimatedSolvingTime seconds. I will be taking the university entrance exam this year. Could you rate me between 0 and 5 points? Please don't solve the question.")
            text("output: ")
        }
        geminiResponse = geminiGenerativeModel.generateContent(geminiContent)
    }

    companion object {
        @JvmStatic
        fun newInstance(question: Question, questionList: ArrayList<Question>) =
            SolveQuestionFragment().apply {
                arguments = Bundle().apply {
                    putString(KEY_QUESTION_LESSON, question.lesson.name)
                    putString(KEY_QUESTION_TOPIC, question.topic)
                    putString(KEY_QUESTION_IMAGE, question.image)
                    putString(KEY_QUESTION_LEVEL, levelToString(question.level))
                    putString(KEY_QUESTION_SOLVING_TIME, question.estimatedSolvingTime)
                    putString(KEY_QUESTION_CORRECT_ANSWER, question.correctAnswer)
                    putParcelableArrayList(KEY_QUESTION_LIST, questionList)
                }
            }
    }

    override fun onClick(v: View?) {
        v?.let {
            when (it.id) {
                sendMyAnswerButton.id -> {
                    sendAnswerToGemini()
                }

                changeQuestionButton.id -> {
                    changeQuestion()
                }

                else -> {
                    //ignored
                }
            }
        }
    }

    private fun sendAnswerToGemini() {
        if (selectedAnswer != null) {
            CoroutineScope(Dispatchers.Main).launch {
                val generating = launch {
                    generateGeminiContent()
                }
                generating.join()
                hideLoading()
                optionsRG.clearCheck()
                selectedAnswer = null
                if (geminiResponse != null && geminiResponse!!.text != null) {
                    if (extractPointFromResponse(geminiResponse!!.text!!) != null) {
                        val givenPointFromGemini =
                            extractPointFromResponse(geminiResponse!!.text!!)!!
                        realmCRUD.addPointToRealm(
                            googleProfile.id!!,
                            questionLesson,
                            questionTopic,
                            givenPointFromGemini
                        )
                        navigateToGeminiResponseFragment(
                            geminiResponse!!.text!!,
                            givenPointFromGemini
                        )
                    }
                }
            }
        } else {
            geminiResponse = null
            ToastUtil.showToast(solveQuestionContext, getString(R.string.please_select_answer))
        }
    }

    private fun navigateToGeminiResponseFragment(response: String, givenPoint: Float) {
        val geminiResponseFragment =
            GeminiResponseFragment.newInstance(response, givenPoint)
        parentFragmentManager.beginTransaction()
            .setCustomAnimations(
                R.anim.slide_in_right,
                R.anim.fade_out,
                R.anim.fade_in,
                R.anim.slide_out_left
            ).replace(
                R.id.lesson_container_fragment,
                geminiResponseFragment,
                GeminiResponseFragment::class.java.name
            ).commit()
    }

    private fun changeQuestion() {
        val questionListWithoutCurrentQuestion =
            questionList.filter { it.image != questionImage }

        if (questionListWithoutCurrentQuestion.isNotEmpty()) {
            countDownTimerUtil.destroyTimer()
            optionsRG.clearCheck()
            val changedQuestion = questionListWithoutCurrentQuestion.random()
            val changeQuestionDialog = ChangeQuestionAlertDialog(solveQuestionContext)
            changeQuestionDialog.window?.setBackgroundDrawableResource(R.drawable.alert_dialog_rounded_corners)
            changeQuestionDialog.show()
            changeQuestionDialog.apply {
                setCancelable(false)
                setLevel(levelToString(changedQuestion.level))
                setEstimatedTime("${changedQuestion.estimatedSolvingTime} $SECONDS")
                setChangeQuestionButtonListener {
                    questionImage = changedQuestion.image
                    questionLevel = levelToString(changedQuestion.level)
                    questionEstimatedSolvingTime = changedQuestion.estimatedSolvingTime
                    questionCorrectAnswer = changedQuestion.correctAnswer
                    clockTime = (questionEstimatedSolvingTime.toInt() * 1000).toLong()
                    progressTime = (clockTime / 1000).toFloat()
                    setupUseful()
                    setupCountDownTimer()
                    setQuestionImage()
                    countDownTimerUtil.startTimer()
                    changeQuestionDialog.dismiss()
                }
            }
        } else {
            ToastUtil.showErrorDialogQuestionNotFound(
                solveQuestionContext,
                parentFragmentManager,
                SolveQuestionFragment::class.java.name,
                getString(R.string.no_another_question)
            )
        }
    }
}