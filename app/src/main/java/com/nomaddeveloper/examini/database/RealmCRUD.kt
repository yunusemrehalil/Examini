package com.nomaddeveloper.examini.database

import com.nomaddeveloper.examini.model.realm.GeminiPoint
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import io.realm.kotlin.query.Sort

class RealmCRUD(private val realm: Realm) {
    fun addPointToRealm(googleId: String, lesson: String, topic: String, newPointValue: Float) {
        realm.writeBlocking {
            val newPoint =
                GeminiPoint(googleId, lesson, topic, newPointValue, System.currentTimeMillis())
            copyToRealm(newPoint)

            val allPoints = query<GeminiPoint>(
                "googleId == $0 AND lesson == $1 AND topic == $2",
                googleId,
                lesson,
                topic
            )
                .sort("timestamp", Sort.ASCENDING)
                .find()

            if (allPoints.size > 6) {
                for (i in 0 until allPoints.size - 6) {
                    delete(allPoints[i])
                }
            }
        }
    }

    fun getLast6Points(googleId: String, lesson: String, topic: String): List<GeminiPoint> {
        return realm.query<GeminiPoint>(
            "googleId == $0 AND lesson == $1 AND topic == $2",
            googleId,
            lesson,
            topic
        )
            .sort("timestamp", Sort.DESCENDING)
            .limit(6)
            .find()
            .toList()
    }
}