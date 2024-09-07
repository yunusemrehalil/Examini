package com.nomaddeveloper.examini.di

import com.google.ai.client.generativeai.GenerativeModel
import com.nomaddeveloper.examini.BuildConfig
import com.nomaddeveloper.examini.util.Constant.GEMINI_MODEL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object GeminiModule {
    @Provides
    @Singleton
    fun provideGeminiModel(): GenerativeModel {
        return GenerativeModel(
            modelName = GEMINI_MODEL,
            apiKey = BuildConfig.GEMINI_API_KEY
        )
    }
}