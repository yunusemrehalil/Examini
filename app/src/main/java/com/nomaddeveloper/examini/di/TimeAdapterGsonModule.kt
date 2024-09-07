package com.nomaddeveloper.examini.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.nomaddeveloper.examini.util.LocalDateTimeAdapter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.time.LocalDateTime
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TimeAdapterGsonModule {
    @Provides
    @Singleton
    @Named("TimeAdapterGson")
    fun provideTimeAdapterGson(): Gson {
        return GsonBuilder()
            .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeAdapter())
            .create()
    }
}