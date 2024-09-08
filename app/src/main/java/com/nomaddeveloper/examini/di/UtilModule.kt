package com.nomaddeveloper.examini.di

import android.content.SharedPreferences
import com.google.gson.Gson
import com.nomaddeveloper.examini.util.CountDownTimerUtilFactory
import com.nomaddeveloper.examini.util.PreferencesUtil
import com.nomaddeveloper.examini.util.StringUtil
import com.nomaddeveloper.examini.util.ToastUtil
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UtilModule {
    @Provides
    @Singleton
    fun provideToastUtil(): ToastUtil {
        return ToastUtil()
    }

    @Provides
    @Singleton
    fun provideStringUtil(): StringUtil {
        return StringUtil()
    }

    @Provides
    @Singleton
    fun providePreferencesUtil(
        sharedPreferences: SharedPreferences,
        @Named("DefaultGson") gson: Gson
    ): PreferencesUtil {
        return PreferencesUtil(sharedPreferences, gson)
    }

    @Provides
    @Singleton
    fun provideCountDownTimerUtilFactory(): CountDownTimerUtilFactory {
        return CountDownTimerUtilFactory()
    }
}