package com.nomaddeveloper.examini.ui.lesson

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import com.nomaddeveloper.examini.R
import com.nomaddeveloper.examini.databinding.ActivityLessonBinding
import com.nomaddeveloper.examini.ui.base.BaseActivity
import com.nomaddeveloper.examini.util.Constant

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
                stringUtil.convertStringToExam(thisExam),
                stringUtil.convertStringToLesson(thisLesson)
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