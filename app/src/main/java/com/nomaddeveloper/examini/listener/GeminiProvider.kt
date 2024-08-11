package com.nomaddeveloper.examini.listener

import com.google.ai.client.generativeai.GenerativeModel

interface GeminiProvider {
    fun getGemini(): GenerativeModel
}