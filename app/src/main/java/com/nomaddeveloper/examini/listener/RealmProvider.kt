package com.nomaddeveloper.examini.listener

import com.nomaddeveloper.examini.database.RealmCRUD
import io.realm.kotlin.Realm

interface RealmProvider {
    fun getRealm(): Realm
    fun getRealmCRUD(): RealmCRUD
}