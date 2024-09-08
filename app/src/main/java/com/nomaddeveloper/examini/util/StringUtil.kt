package com.nomaddeveloper.examini.util

import com.nomaddeveloper.examini.data.model.question.QuestionLevel
import com.nomaddeveloper.examini.util.Enums.Lesson
import javax.inject.Inject

class StringUtil @Inject constructor() {
    fun convertStringToLesson(lesson: String?): Lesson {
        return when (lesson) {
            Lesson.MATH.name -> Lesson.MATH
            Lesson.TURKISH.name -> Lesson.TURKISH
            Lesson.GEOMETRY.name -> Lesson.GEOMETRY
            Lesson.PHYSICS.name -> Lesson.PHYSICS
            Lesson.CHEMISTRY.name -> Lesson.CHEMISTRY
            Lesson.BIOLOGY.name -> Lesson.BIOLOGY
            Lesson.PHILOSOPHY.name -> Lesson.PHILOSOPHY
            Lesson.RELIGION_CULTURE.name -> Lesson.RELIGION_CULTURE
            Lesson.HISTORY.name -> Lesson.HISTORY
            Lesson.GEOGRAPHY.name -> Lesson.GEOGRAPHY
            Lesson.TURKISH_LECTURE.name -> Lesson.TURKISH_LECTURE
            else -> Lesson.UNKNOWN
        }
    }

    fun convertStringToExam(exam: String?): Enums.Exam {
        return when (exam) {
            Enums.Exam.TYT.name -> Enums.Exam.TYT
            Enums.Exam.AYT.name -> Enums.Exam.AYT
            Enums.Exam.ACT.name -> Enums.Exam.ACT
            Enums.Exam.SAT.name -> Enums.Exam.SAT
            else -> {
                Enums.Exam.UNKNOWN
            }
        }
    }

    fun getTurkishName(lesson: Lesson): String? {
        return Constant.LessonNameMap.turkishNamesForLessons[lesson]
    }

    fun levelToString(level: QuestionLevel): String {
        return when (level) {
            QuestionLevel.VERY_EASY -> "Very Easy"
            QuestionLevel.EASY -> "Easy"
            QuestionLevel.MEDIUM -> "Medium"
            QuestionLevel.HARD -> "Hard"
            QuestionLevel.VERY_HARD -> "Very Hard"
            QuestionLevel.DEFAULT -> "Unknown"
        }
    }
}