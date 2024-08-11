package com.nomaddeveloper.examini.ui.fragment

import android.content.Context
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
import com.nomaddeveloper.examini.util.Constant.KEY_QUESTION_SOLVING_TIME
import com.nomaddeveloper.examini.util.Constant.KEY_QUESTION_TOPIC
import com.nomaddeveloper.examini.util.Constant.REGEX_PATTERN_FOR_REMOVE_ASTERISKS
import com.nomaddeveloper.examini.util.CountDownTimerUtil
import com.nomaddeveloper.examini.util.StringUtil.Companion.levelToString
import com.nomaddeveloper.examini.util.ToastUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

class SolveQuestionFragment : BaseFragment(), View.OnClickListener {
    private lateinit var binding: FragmentSolveQuestionBinding
    private lateinit var sendTimeToGeminiContext: Context
    private lateinit var countDownTimerUtil: CountDownTimerUtil
    private lateinit var sendMyAnswerButton: MaterialButton
    private lateinit var changeQuestionButton: MaterialButton
    private lateinit var lessonNameTV: TextView
    private lateinit var countDownTimerLPI: LinearProgressIndicator
    private lateinit var secondsLeftTV: TextView
    private lateinit var optionsRG: RadioGroup
    private lateinit var randomTestQuestionIV: ImageView
    private lateinit var geminiContent: Content
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
                    //TODO() kontrol et, burada bir sıkıntı olabilir!
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
        sendTimeToGeminiContext = requireContext()
        countDownTimerUtil = CountDownTimerUtil(clockTime, 1000)
        glideErrorListener = GlideErrorListener(
            sendTimeToGeminiContext,
            parentFragmentManager,
            SolveQuestionFragment::class.java.name,
            "Soruyu yüklerken hata oluştu.",
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
                R.id.rb_A -> selectedAnswer = "A"
                R.id.rb_B -> selectedAnswer = "B"
                R.id.rb_C -> selectedAnswer = "C"
                R.id.rb_D -> selectedAnswer = "D"
                R.id.rb_E -> selectedAnswer = "E"
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
                val secondsLeftText = "$secondsLeft seconds"
                secondsLeftTV.text = secondsLeftText
                countDownTimerLPI.progress = secondsLeft
            }
        }
        countDownTimerUtil.onFinish = {
            // todo()
        }
    }

    private fun extractPointFromResponse(response: String): Float? {
        val cleanedResponse = response
            .replace(Regex(REGEX_PATTERN_FOR_REMOVE_ASTERISKS), "")
            .trim()

        val keywordIndex = cleanedResponse.indexOf("puan alabilirsiniz")
        if (keywordIndex == -1) return null
        val beforeKeyword = cleanedResponse.substring(0, keywordIndex).trim()
        val numberString = beforeKeyword.split(" ").lastOrNull()?.trim()
        return numberString?.toFloatOrNull()
    }

    private suspend fun generateGeminiContent() {
        showLoading()
        countDownTimerUtil.destroyTimer()
        //val question = randomTestQuestionIV.drawable.let { (it as BitmapDrawable).bitmap }
        val time: Int =
            (questionEstimatedSolvingTime.toInt() - secondsLeft)
        val myAnswer = selectedAnswer
        val prompt =
            "Bu soru için benim cevabım ${myAnswer} ve doğru cevap ${questionCorrectAnswer}. Ben, cevabımı ${time} saniyede buldum. Bu sene üniversite sınavına gireceğim. Beni 0 ile 5 puan arasında değerlendirebilir misin? Lütfen soruyu çözme."
        /*if (time < 120) {
            "Bu soru için benim cevabım ${myAnswer} ve doğru cevap ${correctAnswer}. Ben, cevabımı ${time} saniyede buldum. Bu sene üniversite sınavına gireceğim. Beni 0 ile 5 puan arasında değerlendirebilir misin? Lütfen soruyu çözme."
        } else {
            //TODO() eğer süre fazlaysa gemini'den öneri isteyebiliriz.
            //"Bu soru için benim cevabım ${myAnswer} ve doğru cevap ${correctAnswer}. Ben, cevabımı ${time} saniyede buldum. Bu tarz soruları çözmek için bana bir kaç öneride bulunabilir misin? Lütfen soruyu çözme."
        }*/
        geminiContent = content {
            text("input: Bu soru için benim cevabım A şıkkı ve bu sorunun doğru cevabı B şıkkı. Ben, cevabımı 25 saniyede buldum ve bu sorunun, uzmanlar tarafından hesaplanmış tahmini çözüm süresi 60 saniye. Bu sene üniversite sınavına gireceğim. Beni 0 ile 5 puan arasında değerlendirebilir misin? Lütfen soruyu çözme.")
            text("output: Cevabınızın doğru olmaması puanınızı etkiler, ancak çözüm süreniz 25 saniye ile tahmini süreden (60 saniye) oldukça iyi. Bu, problem çözme hızınızın iyi olduğunu gösteriyor. Bu yüzden 0 ile 5 puan arasında 3 puan alabilirsiniz.")
            text("input: Bu soru için benim cevabım B şıkkı ve bu sorunun doğru cevabı B şıkkı. Ben, cevabımı 60 saniyede buldum ve bu sorunun, uzmanlar tarafından hesaplanmış tahmini çözüm süresi 60 saniye. Bu sene üniversite sınavına gireceğim. Beni 0 ile 5 puan arasında değerlendirebilir misin? Lütfen soruyu çözme.")
            text("output: Soruyu doğru cevapladınız ve çözüm süreniz de tahmini süreyle uyumlu. Bu durumda, hem doğru cevabı bulmanız hem de belirtilen süre içinde yapmanız nedeniyle yüksek bir puan alırsınız. Bu yüzden 0 ile 5 puan arasında 5 puan alabilirsiniz.")
            text("input: Bu soru için benim cevabım C şıkkı ve bu sorunun doğru cevabı A şıkkı. Ben, cevabımı 30 saniyede buldum ve bu sorunun, uzmanlar tarafından hesaplanmış tahmini çözüm süresi 120 saniye. Bu sene üniversite sınavına gireceğim. Beni 0 ile 5 puan arasında değerlendirebilir misin? Lütfen soruyu çözme.")
            text("output: Doğru cevabı bulamamış olsanız da, cevabınızı 30 saniyede bulmanız tahmini süreden (120 saniye) oldukça hızlı. Hızınız iyi olmakla birlikte, doğru cevabı bulamadığınız için puanınız biraz düşük olacaktır. Bu nedenle, 0 ile 5 puan arasında 2 puan alabilirsiniz.")
            text("input: Bu soru için benim cevabım C şıkkı ve bu sorunun doğru cevabı C şıkkı. Ben, cevabımı 160 saniyede buldum ve bu sorunun, uzmanlar tarafından hesaplanmış tahmini çözüm süresi 80 saniye. Bu sene üniversite sınavına gireceğim. Beni 0 ile 5 puan arasında değerlendirebilir misin? Lütfen soruyu çözme.")
            text("output: Soruyu doğru cevapladınız, ancak çözüm süreniz tahmini sürenin (80 saniye) iki katından fazla. Doğru cevabı bulmanız olumlu, ancak hızınızı geliştirmeniz gerekiyor. Bu yüzden, 0 ile 5 puan arasında 3 puan alabilirsiniz.")
            text("input: Bu soru için benim cevabım C şıkkı ve bu sorunun doğru cevabı C şıkkı. Ben, cevabımı 160 saniyede buldum ve bu sorunun, uzmanlar tarafından hesaplanmış tahmini çözüm süresi 120 saniye. Bu sene üniversite sınavına gireceğim. Beni 0 ile 5 puan arasında değerlendirebilir misin? Lütfen soruyu çözme.")
            text("output: Soruyu doğru cevapladınız ve çözüm süreniz tahmini süreden (120 saniye) biraz daha uzun olsa da, yine de makul bir sürede tamamladınız. Hem doğru cevabı bulmanız hem de çözüm sürenizin biraz uzun olması göz önüne alındığında, 0 ile 5 puan arasında 4 puan alabilirsiniz.")
            text("input: Bu soru için benim cevabım E şıkkı ve bu sorunun doğru cevabı C şıkkı. Ben, cevabımı 160 saniyede buldum ve bu sorunun, uzmanlar tarafından hesaplanmış tahmini çözüm süresi 120 saniye. Bu sene üniversite sınavına gireceğim. Beni 0 ile 5 puan arasında değerlendirebilir misin? Lütfen soruyu çözme.")
            text("output: Doğru cevabı bulamadınız, ancak çözüm süreniz tahmini süreden (120 saniye) biraz uzun. Yanıtınızın yanlış olması puanınızı etkiler, ancak hızınız ortalamanın üzerinde. Bu nedenle, 0 ile 5 puan arasında 2 puan alabilirsiniz.")
            text("input: Bu soru için benim cevabım E şıkkı ve bu sorunun doğru cevabı D şıkkı. Ben, cevabımı 100 saniyede buldum ve bu sorunun, uzmanlar tarafından hesaplanmış tahmini çözüm süresi 120 saniye. Bu sene üniversite sınavına gireceğim. Beni 0 ile 5 puan arasında değerlendirebilir misin? Lütfen soruyu çözme.")
            text("output: Doğru cevabı bulamadınız ama cevabınızı 100 saniyede bulmanız tahmini süreden (120 saniye) daha hızlı. Yanıtınız yanlış olsa da hızınız iyi. Bu nedenle, 0 ile 5 puan arasında 3 puan alabilirsiniz.")
            text("input: Bu soru için benim cevabım E şıkkı ve bu sorunun doğru cevabı E şıkkı. Ben, cevabımı 100 saniyede buldum ve bu sorunun, uzmanlar tarafından hesaplanmış tahmini çözüm süresi 120 saniye. Bu sene üniversite sınavına gireceğim. Beni 0 ile 5 puan arasında değerlendirebilir misin? Lütfen soruyu çözme.")
            text("output: Soruyu doğru cevapladınız ve cevabınızı tahmini süreden (120 saniye) daha hızlı (100 saniye) buldunuz. Hem doğru cevabı bulmanız hem de hızınız oldukça iyi. Bu nedenle, 0 ile 5 puan arasında 5 puan alabilirsiniz.")
            text("input: Bu soru için benim cevabım A şıkkı ve bu sorunun doğru cevabı E şıkkı. Ben, cevabımı 60 saniyede buldum ve bu sorunun, uzmanlar tarafından hesaplanmış tahmini çözüm süresi 80saniye. Bu sene üniversite sınavına gireceğim. Beni 0 ile 5 puan arasında değerlendirebilir misin? Lütfen soruyu çözme.")
            text("output: Doğru cevabı bulamadınız, ancak cevabınızı 60 saniyede bulmanız tahmini süreden (80 saniye) daha hızlı. Yanıtınız yanlış olsa da hızınız iyi. Bu nedenle, 0 ile 5 puan arasında 3 puan alabilirsiniz.")
            text("input: Bu soru için benim cevabım A şıkkı ve bu sorunun doğru cevabı A şıkkı. Ben, cevabımı 60 saniyede buldum ve bu sorunun, uzmanlar tarafından hesaplanmış tahmini çözüm süresi 80 saniye. Bu sene üniversite sınavına gireceğim. Beni 0 ile 5 puan arasında değerlendirebilir misin? Lütfen soruyu çözme.")
            text("output: Soruyu doğru cevapladınız ve cevabınızı tahmini süreden (80 saniye) daha hızlı (60 saniye) buldunuz. Hem doğru cevabı bulmanız hem de hızlı çözümünüz oldukça iyi. Bu nedenle, 0 ile 5 puan arasında 5 puan alabilirsiniz.")
            text("input: Bu soru için benim cevabım B şıkkı ve bu sorunun doğru cevabı D şıkkı. Ben, cevabımı 90 saniyede buldum ve bu sorunun, uzmanlar tarafından hesaplanmış tahmini çözüm süresi 100 saniye. Bu sene üniversite sınavına gireceğim. Beni 0 ile 5 puan arasında değerlendirebilir misin? Lütfen soruyu çözme.")
            text("output: Doğru cevabı bulamadınız, ancak cevabınızı tahmini süreden (100 saniye) daha hızlı (90 saniye) buldunuz. Yanıtınız yanlış olsa da hızınız iyi. Bu nedenle, 0 ile 5 puan arasında 3 puan alabilirsiniz.")
            text("input: Bu soru için benim cevabım C şıkkı ve bu sorunun doğru cevabı C şıkkı. Ben, cevabımı 150 saniyede buldum ve bu sorunun, uzmanlar tarafından hesaplanmış tahmini çözüm süresi 120 saniye. Bu sene üniversite sınavına gireceğim. Beni 0 ile 5 puan arasında değerlendirebilir misin? Lütfen soruyu çözme.")
            text("output: Soruyu doğru cevapladınız ve cevabınızı tahmini süreden (120 saniye) biraz daha uzun sürede (150 saniye) buldunuz. Doğru cevabı bulmanız olumlu, ancak hızınızı geliştirmeniz gerekiyor. Bu nedenle, 0 ile 5 puan arasında 4 puan alabilirsiniz.")
            text("input: Bu soru için benim cevabım D şıkkı ve bu sorunun doğru cevabı B şıkkı. Ben, cevabımı 70 saniyede buldum ve bu sorunun, uzmanlar tarafından hesaplanmış tahmini çözüm süresi 90 saniye. Bu sene üniversite sınavına gireceğim. Beni 0 ile 5 puan arasında değerlendirebilir misin? Lütfen soruyu çözme.")
            text("output: Doğru cevabı bulamadınız, ancak cevabınızı tahmini süreden (90 saniye) daha hızlı (70 saniye) buldunuz. Yanıtınız yanlış olsa da hızınız iyi. Bu nedenle, 0 ile 5 puan arasında 3 puan alabilirsiniz.")
            text("input: Bu soru için benim cevabım E şıkkı ve bu sorunun doğru cevabı E şıkkı. Ben, cevabımı 120 saniyede buldum ve bu sorunun, uzmanlar tarafından hesaplanmış tahmini çözüm süresi 100 saniye. Bu sene üniversite sınavına gireceğim. Beni 0 ile 5 puan arasında değerlendirebilir misin? Lütfen soruyu çözme.")
            text("output: Soruyu doğru cevapladınız, ancak cevabınızı tahmini süreden (100 saniye) daha uzun sürede (120 saniye) buldunuz. Doğru cevabı bulmanız olumlu, ancak hızınız biraz düşük. Bu nedenle, 0 ile 5 puan arasında 4 puan alabilirsiniz.")
            text("input: Bu soru için benim cevabım B şıkkı ve bu sorunun doğru cevabı A şıkkı. Ben, cevabımı 80 saniyede buldum ve bu sorunun, uzmanlar tarafından hesaplanmış tahmini çözüm süresi 70 saniye. Bu sene üniversite sınavına gireceğim. Beni 0 ile 5 puan arasında değerlendirebilir misin? Lütfen soruyu çözme.")
            text("output: Doğru cevabı bulamadınız, ancak cevabınızı tahmini süreden (70 saniye) biraz daha uzun sürede (80 saniye) buldunuz. Yanıtınız yanlış olsa da hızınız makul. Bu nedenle, 0 ile 5 puan arasında 2 puan alabilirsiniz.")
            text("input: Bu soru için benim cevabım C şıkkı ve bu sorunun doğru cevabı D şıkkı. Ben, cevabımı 110 saniyede buldum ve bu sorunun, uzmanlar tarafından hesaplanmış tahmini çözüm süresi 100 saniye. Bu sene üniversite sınavına gireceğim. Beni 0 ile 5 puan arasında değerlendirebilir misin? Lütfen soruyu çözme.")
            text("output: Doğru cevabı bulamadınız, ancak cevabınızı tahmini süreden (100 saniye) biraz daha uzun sürede (110 saniye) buldunuz. Yanıtınız yanlış olsa da hızınız iyi. Bu nedenle, 0 ile 5 puan arasında 3 puan alabilirsiniz.")
            text("input: Bu soru için benim cevabım A şıkkı ve bu sorunun doğru cevabı B şıkkı. Ben, cevabımı 40 saniyede buldum ve bu sorunun, uzmanlar tarafından hesaplanmış tahmini çözüm süresi 60 saniye. Bu sene üniversite sınavına gireceğim. Beni 0 ile 5 puan arasında değerlendirebilir misin? Lütfen soruyu çözme.")
            text("output: Doğru cevabı bulamadınız, ancak cevabınızı tahmini süreden (60 saniye) daha hızlı (40 saniye) buldunuz. Yanıtınız yanlış olsa da hızınız çok iyi. Bu nedenle, 0 ile 5 puan arasında 3 puan alabilirsiniz.")
            text("input: Bu soru için benim cevabım E şıkkı ve bu sorunun doğru cevabı D şıkkı. Ben, cevabımı 200 saniyede buldum ve bu sorunun, uzmanlar tarafından hesaplanmış tahmini çözüm süresi 150 saniye. Bu sene üniversite sınavına gireceğim. Beni 0 ile 5 puan arasında değerlendirebilir misin? Lütfen soruyu çözme.")
            text("output: Doğru cevabı bulamadınız ve cevabınızı tahmini süreden (150 saniye) daha uzun sürede (200 saniye) buldunuz. Hem yanıtınız yanlış hem de hızınız düşük. Bu nedenle, 0 ile 5 puan arasında 2 puan alabilirsiniz.")
            text("input: Bu soru için benim cevabım D şıkkı ve bu sorunun doğru cevabı D şıkkı. Ben, cevabımı 50 saniyede buldum ve bu sorunun, uzmanlar tarafından hesaplanmış tahmini çözüm süresi 90 saniye. Bu sene üniversite sınavına gireceğim. Beni 0 ile 5 puan arasında değerlendirebilir misin? Lütfen soruyu çözme.")
            text("output: Soruyu doğru cevapladınız ve cevabınızı tahmini süreden (90 saniye) daha hızlı (50 saniye) buldunuz. Hem doğru cevabı bulmanız hem de hızınız mükemmel. Bu nedenle, 0 ile 5 puan arasında 5 puan alabilirsiniz.")
            text("input: Bu soru için benim cevabım C şıkkı ve bu sorunun doğru cevabı C şıkkı. Ben, cevabımı 130 saniyede buldum ve bu sorunun, uzmanlar tarafından hesaplanmış tahmini çözüm süresi 140 saniye. Bu sene üniversite sınavına gireceğim. Beni 0 ile 5 puan arasında değerlendirebilir misin? Lütfen soruyu çözme.")
            text("output: Soruyu doğru cevapladınız ve cevabınızı tahmini süreden (140 saniye) biraz daha hızlı (130 saniye) buldunuz. Hem doğru cevabı bulmanız hem de hızınız iyi. Bu nedenle, 0 ile 5 puan arasında 4 puan alabilirsiniz.")
            text("input: Bu soru için benim cevabım ${myAnswer} şıkkı ve doğru cevap ${questionCorrectAnswer} şıkkı. Ben, cevabımı ${time} saniyede buldum ve bu sorunun, uzmanlar tarafından hesaplanmış tahmini çözüm süresi ${questionEstimatedSolvingTime}. Bu sene üniversite sınavına gireceğim. Beni 0 ile 5 puan arasında değerlendirebilir misin? Lütfen soruyu çözme.")
            text("output: ")
        }
        geminiResponse = geminiGenerativeModel.generateContent(geminiContent)
    }

    companion object {
        @JvmStatic
        fun newInstance(question: Question) =
            SolveQuestionFragment().apply {
                arguments = Bundle().apply {
                    putString(KEY_QUESTION_LESSON, question.lesson.name)
                    putString(KEY_QUESTION_TOPIC, question.topic)
                    putString(KEY_QUESTION_IMAGE, question.image)
                    putString(KEY_QUESTION_LEVEL, levelToString(question.level))
                    putString(KEY_QUESTION_SOLVING_TIME, question.estimatedSolvingTime)
                    putString(KEY_QUESTION_CORRECT_ANSWER, question.correctAnswer)
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
            ToastUtil.showToast(sendTimeToGeminiContext, "Lütfen bir cevap seçiniz.")
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
            )
            .replace(
                R.id.lesson_container_fragment,
                geminiResponseFragment,
                GeminiResponseFragment::class.java.name
            )
            .commit()
    }

    private fun changeQuestion() {
        //todo() change question
    }
}