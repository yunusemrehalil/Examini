package com.nomaddeveloper.examini.model.realm

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import java.util.UUID

open class GeminiPoint : RealmObject {
    @PrimaryKey
    var id: String = UUID.randomUUID().toString()
    private var googleId: String = ""
    var lesson: String = ""
    var topic: String = ""
    var value: Float = 0f
    private var timestamp: Long = 0

    constructor()

    constructor(
        googleId: String,
        lesson: String,
        topic: String,
        value: Float,
        timestamp: Long
    ) : this() {
        this.googleId = googleId
        this.lesson = lesson
        this.topic = topic
        this.value = value
        this.timestamp = timestamp
    }
}