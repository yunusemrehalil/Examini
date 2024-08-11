package com.nomaddeveloper.examini.util

import android.content.Context
import com.google.gson.Gson
import com.nomaddeveloper.examini.model.profile.GoogleProfile

object PreferencesUtil {
    private const val PREFERENCE_NAME = "GeminiExamAssistantPreferences"
    private const val ID_TOKEN = "ID_TOKEN"
    private const val USERNAME = "USERNAME"
    private const val GOOGLE_PROFILE = "GOOGLE_PROFILE"
    private const val LAST_ACTIVITY_TIME = "LAST_ACTIVITY_TIME"
    private val gson = Gson()

    fun setIdToken(context: Context, idToken: String) {
        val prefs = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putString(ID_TOKEN, idToken)
        editor.apply()
    }

    fun getIdToken(context: Context): String? {
        val prefs = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        return prefs.getString(ID_TOKEN, null)
    }

    fun setUsername(context: Context, username: String) {
        val prefs = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putString(USERNAME, username)
        editor.apply()
    }

    fun getUsername(context: Context): String? {
        val prefs = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        return prefs.getString(USERNAME, null)
    }

    fun setGoogleProfile(context: Context, profile: GoogleProfile) {
        val prefs = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        val editor = prefs.edit()
        val profileJson = gson.toJson(profile)
        editor.putString(GOOGLE_PROFILE, profileJson)
        editor.apply()
    }

    fun getGoogleProfile(context: Context): GoogleProfile? {
        val prefs = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        val profileJson = prefs.getString(GOOGLE_PROFILE, null)
        return if (profileJson != null) {
            gson.fromJson(profileJson, GoogleProfile::class.java)
        } else {
            null
        }
    }

    fun clearGoogleProfile(context: Context) {
        val prefs = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.remove(GOOGLE_PROFILE)
        editor.apply()
    }

    fun setLastActivityTime(context: Context) {
        val prefs = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putLong(LAST_ACTIVITY_TIME, System.currentTimeMillis())
        editor.apply()
    }

    fun getLastActivityTime(context: Context): Long {
        val prefs = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        return prefs.getLong(LAST_ACTIVITY_TIME, 0)
    }
}
