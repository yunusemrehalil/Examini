package com.nomaddeveloper.examini.ui.activity

import android.content.Intent
import android.icu.util.Calendar
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.google.android.material.textview.MaterialTextView
import com.nomaddeveloper.examini.R
import com.nomaddeveloper.examini.adapter.ExamRecyclerViewAdapter
import com.nomaddeveloper.examini.adapter.LessonBottomSheetRecyclerViewAdapter
import com.nomaddeveloper.examini.databinding.ActivityHomePageBinding
import com.nomaddeveloper.examini.listener.ExamRecyclerViewOnClickListener
import com.nomaddeveloper.examini.listener.LessonRecyclerViewOnClickListener
import com.nomaddeveloper.examini.util.Constant
import com.nomaddeveloper.examini.util.Enums
import com.nomaddeveloper.examini.util.PreferencesUtil
import com.nomaddeveloper.examini.util.ToastUtil
import com.nomaddeveloper.examini.util.ToastUtil.Companion.createLogoutErrorDialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class HomePageActivity : BaseActivity(), View.OnClickListener, ExamRecyclerViewOnClickListener,
    LessonRecyclerViewOnClickListener {
    private lateinit var binding: ActivityHomePageBinding
    private lateinit var credentialManager: CredentialManager
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var welcomeTV: MaterialTextView
    private lateinit var howManyDaysLeftTV: MaterialTextView
    private lateinit var howManyDaysLeftLPI: LinearProgressIndicator
    private lateinit var examRecyclerView: RecyclerView
    private lateinit var examRecyclerAdapter: ExamRecyclerViewAdapter
    private lateinit var lessonRecyclerView: RecyclerView
    private lateinit var lessonRecyclerAdapter: LessonBottomSheetRecyclerViewAdapter
    private lateinit var examRecyclerViewOmClickListener: ExamRecyclerViewOnClickListener
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        initBinding()
        setupUseful()
        checkProfile()
        initListeners()
        setupUI()
    }

    private fun initBinding() {
        binding = ActivityHomePageBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun setupUseful() {
        credentialManager = CredentialManager.create(this@HomePageActivity)
        coroutineScope = CoroutineScope(Dispatchers.Main)
    }

    private fun checkProfile() {
        if (googleProfile == null) {
            showLogoutConfirmationDialog()
        }
    }

    private fun showLogoutConfirmationDialog() {
        createLogoutErrorDialog(this@HomePageActivity) {
            performLogout()
        }.show()
    }

    private fun performLogout() {
        showLoading()
        PreferencesUtil.clearGoogleProfile(this@HomePageActivity)
        googleProfile = null
        lifecycleScope.launch {
            clearCredentialState()
            redirectToLoginActivity()
        }
    }

    private suspend fun clearCredentialState() {
        credentialManager.clearCredentialState(ClearCredentialStateRequest())
    }

    private fun redirectToLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        hideLoading()
        startActivity(intent)
        finish()
    }

    private fun initListeners() {
        examRecyclerViewOmClickListener = this
    }

    private fun setupUI() {
        binding.apply {
            welcomeTV = tvWelcomeUsername
            howManyDaysLeftTV = tvHowManyDaysUntilExam
            howManyDaysLeftLPI = lpiHowManyDays
            examRecyclerView = rvExams
        }
        val givenName = googleProfile?.givenName ?: "..."
        welcomeTV.text = getString(R.string.welcome_message, givenName)
        setHowManyDaysLeftProgressIndicator()
        examRecyclerAdapter =
            ExamRecyclerViewAdapter(Constant.EXAM_LIST, examRecyclerViewOmClickListener)
        examRecyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, true)
        examRecyclerView.adapter = examRecyclerAdapter
    }

    private fun setHowManyDaysLeftProgressIndicator() {
        val currentDate = Calendar.getInstance()
        val examDate2024 = Constant.EXAM_DATE_2024
        val examDate2025 = Constant.EXAM_DATE_2025
        val diffDate = (examDate2025.timeInMillis - examDate2024.timeInMillis)
        val currentDif = (currentDate.timeInMillis - examDate2024.timeInMillis)
        val daysLeftInMillis = examDate2025.timeInMillis - currentDate.timeInMillis
        val daysLeft = TimeUnit.MILLISECONDS.toDays(daysLeftInMillis).toInt()
        val progress = (currentDif.toFloat() / diffDate * 100)
        val daysLeftMessage = if (daysLeft > 0) {
            resources.getQuantityString(R.plurals.days_left, daysLeft, daysLeft)
        } else {
            getString(R.string.days_left_zero)
        }

        howManyDaysLeftTV.text = daysLeftMessage
        howManyDaysLeftLPI.progress = progress.toInt()
    }

    private fun openTYTBottomSheetDialog() {
        val dialog =
            BottomSheetDialog(this, R.style.ChooseTopicBottomSheetDialog)
        val view = layoutInflater.inflate(R.layout.tyt_bottom_sheet_dialog, null)
        lessonRecyclerView = view.findViewById(R.id.rv_bottom_sheet_dialog_topics)
        val lessonList =
            arrayListOf(
                Enums.Lesson.TURKISH,
                Enums.Lesson.MATH,
                Enums.Lesson.GEOMETRY,
                Enums.Lesson.GEOGRAPHY,
                Enums.Lesson.PHILOSOPHY,
                Enums.Lesson.PHYSICS,
                Enums.Lesson.RELIGION_CULTURE,
                Enums.Lesson.HISTORY,
                Enums.Lesson.BIOLOGY,
                Enums.Lesson.CHEMISTRY,
            )
        lessonRecyclerAdapter =
            LessonBottomSheetRecyclerViewAdapter(Enums.Exam.TYT, lessonList, this)
        lessonRecyclerView.layoutManager = GridLayoutManager(this, 2)
        lessonRecyclerView.adapter = lessonRecyclerAdapter
        dialog.apply {
            setCancelable(true)
            setContentView(view)
            show()
        }
    }

    private fun openAYTBottomSheetDialog() {
        val dialog =
            BottomSheetDialog(this, R.style.ChooseTopicBottomSheetDialog)
        val view = layoutInflater.inflate(R.layout.tyt_bottom_sheet_dialog, null)

        dialog.apply {
            setCancelable(true)
            setContentView(view)
            show()
        }
    }

    override fun onClick(v: View?) {
        val clickedItemId = v?.id
        clickedItemId.let {
            when (it) {
                //
            }
        }
    }

    private fun startLessonActivity(exam: Enums.Exam, lesson: Enums.Lesson) {
        val intent = Intent(this, LessonActivity::class.java)
        intent.putExtra(Constant.KEY_LESSON, lesson.name)
        intent.putExtra(Constant.KEY_EXAM, exam.name)
        val options = ActivityOptionsCompat.makeCustomAnimation(
            this,
            R.anim.slide_in_right,
            R.anim.slide_out_left
        )
        ContextCompat.startActivity(this, intent, options.toBundle())
    }

    override fun onExamShortClick(exam: Enums.Exam) {
        when (exam) {
            Enums.Exam.TYT -> openTYTBottomSheetDialog()
            Enums.Exam.AYT -> openAYTBottomSheetDialog()
            Enums.Exam.ACT -> openACTBottomSheetDialog()
            Enums.Exam.SAT -> openSATBottomSheetDialog()
            Enums.Exam.UNKNOWN -> ToastUtil.showToast(
                this,
                getString(R.string.unknown_exam_selected)
            )
        }
    }

    private fun openACTBottomSheetDialog() {
        val dialog =
            BottomSheetDialog(this, R.style.ChooseTopicBottomSheetDialog)
        val view = layoutInflater.inflate(R.layout.tyt_bottom_sheet_dialog, null)
        lessonRecyclerView = view.findViewById(R.id.rv_bottom_sheet_dialog_topics)
        val lessonList =
            arrayListOf(
                Enums.Lesson.ENGLISH,
                Enums.Lesson.MATH,
                Enums.Lesson.READING,
                Enums.Lesson.SCIENCE
            )
        lessonRecyclerAdapter =
            LessonBottomSheetRecyclerViewAdapter(Enums.Exam.ACT, lessonList, this)
        lessonRecyclerView.layoutManager = GridLayoutManager(this, 2)
        lessonRecyclerView.adapter = lessonRecyclerAdapter
        dialog.apply {
            setCancelable(true)
            setContentView(view)
            show()
        }
    }

    private fun openSATBottomSheetDialog() {
        ToastUtil.showToast(this, getString(R.string.preparing))
        //todo()
    }

    override fun onExamLongClick(exam: Enums.Exam): Boolean {
        ToastUtil.showToast(this, getString(R.string.long_click_feature_not_active))
        return true
    }

    override fun onLessonShortClick(exam: Enums.Exam, lesson: Enums.Lesson) {
        startLessonActivity(exam, lesson)
    }

    override fun onLessonLongClick(exam: Enums.Exam, lesson: Enums.Lesson): Boolean {
        ToastUtil.showToast(this, getString(R.string.long_click_feature_not_active))
        return true
    }
}