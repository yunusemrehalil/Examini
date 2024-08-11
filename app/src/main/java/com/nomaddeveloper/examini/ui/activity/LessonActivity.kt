package com.nomaddeveloper.examini.ui.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import com.nomaddeveloper.examini.R
import com.nomaddeveloper.examini.databinding.ActivityLessonBinding
import com.nomaddeveloper.examini.ui.fragment.LessonTopicsFragment
import com.nomaddeveloper.examini.util.Constant
import com.nomaddeveloper.examini.util.StringUtil.Companion.convertStringToExam
import com.nomaddeveloper.examini.util.StringUtil.Companion.convertStringToLesson

class LessonActivity : BaseActivity() {
    private lateinit var binding: ActivityLessonBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        initBinding()
        val thisLesson = intent.getStringExtra(Constant.KEY_LESSON)
        val thisExam = intent.getStringExtra(Constant.KEY_EXAM)
        val lessonFragment =
            LessonTopicsFragment.newInstance(
                convertStringToExam(thisExam),
                convertStringToLesson(thisLesson)
            )
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(
                R.anim.slide_in_right,
                R.anim.fade_out,
                R.anim.fade_in,
                R.anim.slide_out_left
            )
            .replace(
                R.id.lesson_container_fragment,
                lessonFragment,
                LessonTopicsFragment::class.java.name
            )
            .commit()
    }

    private fun initBinding() {
        binding = ActivityLessonBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}