package com.nomaddeveloper.examini.model.student

import java.math.BigDecimal
import java.time.LocalDateTime

data class Student(
    var id: String? = null,
    var firstName: String,
    var lastName: String,
    var gender: Gender,
    var email: String,
    var address: Address,
    var favoriteSubjects: List<String>,
    var totalSpentInBooks: BigDecimal,
    var created: LocalDateTime
)
