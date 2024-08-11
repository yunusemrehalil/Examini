package com.nomaddeveloper.examini.util

import android.icu.util.Calendar
import com.nomaddeveloper.examini.BuildConfig
import com.nomaddeveloper.examini.util.Enums.Lesson

object Constant {
    const val GEMINI_MODEL = "gemini-1.5-flash"
    const val KEY_USERNAME = "KEY_USERNAME"
    const val KEY_LESSON = "KEY_LESSON"
    const val KEY_EXAM = "KEY_EXAM"
    const val KEY_TOPIC = "KEY_TOPIC"
    const val KEY_GEMINI_RESPONSE = "KEY_GEMINI_RESPONSE"
    const val KEY_GEMINI_POINT = "KEY_GEMINI_POINT"
    const val KEY_QUESTION_IMAGE = "KEY_QUESTION_IMAGE"
    const val KEY_QUESTION_LEVEL = "KEY_QUESTION_LEVEL"
    const val KEY_QUESTION_LESSON = "KEY_QUESTION_LESSON"
    const val KEY_QUESTION_TOPIC = "KEY_QUESTION_TOPIC"
    const val KEY_QUESTION_SOLVING_TIME = "KEY_QUESTION_SOLVING_TIME"
    const val KEY_QUESTION_CORRECT_ANSWER = "KEY_QUESTION_CORRECT_ANSWER"
    const val KEY_QUESTION_LIST = "KEY_QUESTION_LIST"
    const val DEFAULT_LESSON: String = "unknownLesson"
    const val DEFAULT_TOPIC: String = "unknownTopic"
    const val DEFAULT_IMAGE: String = "unknownImage"
    const val DEFAULT_LEVEL: String = "unknownLevel"
    const val DEFAULT_CORRECT_ANSWER: String = "unknownCorrectAnswer"
    const val DEFAULT_SOLVING_TIME: String = "unknownSolvingTime"
    const val DEFAULT_RESPONSE: String = "defaultResponse"
    const val DEFAULT_POINT: Float = -1f
    const val DECIMAL_2_CHAR = "%.2f"
    const val OPTION_A = "A"
    const val OPTION_B = "B"
    const val OPTION_C = "C"
    const val OPTION_D = "D"
    const val OPTION_E = "E"
    const val EMPTY_STRING = ""
    const val REGEX_PATTERN_WITH_POINT = """(\d+ puan|Puan: \d+|Değerlendirme: \d+/)"""
    const val REGEX_PATTERN_FOR_EXTRACT_POINT = """(\d+)(?=[^\d]*\d*/?\d*)"""
    const val REGEX_PATTERN_FOR_TUNED_MODEL = """\*+\s*(\d+)\s*puan\s+alabilirsiniz\s*\*+"""
    const val REGEX_PATTERN_FOR_REMOVE_ASTERISKS = """\*"""
    val EXAM_DATE_2024: Calendar = Calendar.getInstance().apply {
        set(2024, Calendar.JUNE, 9)
    }
    val EXAM_DATE_2025: Calendar = Calendar.getInstance().apply {
        set(2025, Calendar.JUNE, 21)
    }

    const val SKIP_LOGIN = BuildConfig.SKIP_LOGIN
    val EXAM_LIST = arrayListOf(Enums.Exam.AYT, Enums.Exam.TYT)
    const val ONE_HOUR_IN_MILLIS = 60 * 60 * 1000

    object LessonNameMap {
        val turkishNamesForLessons = mapOf(
            Lesson.TURKISH to "Türkçe",
            Lesson.MATH to "Matematik",
            Lesson.GEOMETRY to "Geometri",
            Lesson.PHYSICS to "Fizik",
            Lesson.CHEMISTRY to "Kimya",
            Lesson.BIOLOGY to "Biyoloji",
            Lesson.PHILOSOPHY to "Felsefe",
            Lesson.RELIGION_CULTURE to "Din Kültürü ve Ahlak Bilgisi",
            Lesson.HISTORY to "Tarih",
            Lesson.GEOGRAPHY to "Coğrafya"
        )
    }
}