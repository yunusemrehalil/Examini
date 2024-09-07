package com.nomaddeveloper.examini.util

import android.content.SharedPreferences
import com.google.gson.Gson
import com.nomaddeveloper.examini.data.model.profile.GoogleProfile
import javax.inject.Inject

class PreferencesUtil @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val gson: Gson
) {
    companion object {
        private const val ID_TOKEN = "ID_TOKEN"
        private const val USERNAME = "USERNAME"
        private const val GOOGLE_PROFILE = "GOOGLE_PROFILE"
        private const val LAST_ACTIVITY_TIME = "LAST_ACTIVITY_TIME"
    }

    fun setIdToken(idToken: String) {
        val editor = sharedPreferences.edit()
        editor.putString(ID_TOKEN, idToken)
        editor.apply()
    }

    fun getIdToken(): String? {
        return sharedPreferences.getString(ID_TOKEN, null)
    }

    fun setUsername(username: String) {
        val editor = sharedPreferences.edit()
        editor.putString(USERNAME, username)
        editor.apply()
    }

    fun getUsername(): String? {
        return sharedPreferences.getString(USERNAME, null)
    }

    fun setGoogleProfile(profile: GoogleProfile) {
        val editor = sharedPreferences.edit()
        val profileJson = gson.toJson(profile)
        editor.putString(GOOGLE_PROFILE, profileJson)
        editor.apply()
    }

    fun getGoogleProfile(): GoogleProfile? {
        val profileJson = sharedPreferences.getString(GOOGLE_PROFILE, null)
        return if (profileJson != null) {
            gson.fromJson(profileJson, GoogleProfile::class.java)
        } else {
            null
        }
    }

    fun clearGoogleProfile() {
        val editor = sharedPreferences.edit()
        editor.remove(GOOGLE_PROFILE)
        editor.apply()
    }

    fun setLastActivityTime() {
        val editor = sharedPreferences.edit()
        editor.putLong(LAST_ACTIVITY_TIME, System.currentTimeMillis())
        editor.apply()
    }

    fun getLastActivityTime(): Long {
        return sharedPreferences.getLong(LAST_ACTIVITY_TIME, 0)
    }
}
