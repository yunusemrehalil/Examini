package com.nomaddeveloper.examini.listener

import com.nomaddeveloper.examini.model.profile.GoogleProfile

interface GoogleProfileProvider {
    fun fetchGoogleProfile(): GoogleProfile?
}