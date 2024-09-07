package com.nomaddeveloper.examini.di

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.nomaddeveloper.examini.BuildConfig.BASE_URL
import com.nomaddeveloper.examini.R
import com.nomaddeveloper.examini.data.repository.LessonRepository
import com.nomaddeveloper.examini.data.repository.QuestionRepository
import com.nomaddeveloper.examini.data.repository.StudentRepository
import com.nomaddeveloper.examini.data.source.remote.ExaminiAPI
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ExaminiModule {
    @Provides
    @Singleton
    fun provideApplicationContext(@ApplicationContext context: Context): Context {
        return context
    }

    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences(
            context.getString(R.string.shared_preferences_name),
            Context.MODE_PRIVATE
        )
    }

    @Provides
    @Singleton
    @Named("DefaultGson")
    fun provideGson(): Gson {
        return GsonBuilder().create()
    }

    @Provides
    @Singleton
    fun provideInterceptor(): Interceptor {
        return Interceptor { chain ->
            val contentTypeRequest = chain.request().newBuilder()
                .addHeader("Content-Type", "application/json")
                .build()
            chain.proceed(contentTypeRequest)
        }
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(interceptor: Interceptor): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(interceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        @Named("TimeAdapterGson") timeAdapterGson: Gson
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(timeAdapterGson))
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    fun provideExaminiAPI(retrofit: Retrofit): ExaminiAPI {
        return retrofit.create(ExaminiAPI::class.java)
    }

    @Provides
    @Singleton
    fun provideQuestionRepository(examiniAPI: ExaminiAPI): QuestionRepository {
        return QuestionRepository(examiniAPI)
    }

    @Provides
    @Singleton
    fun provideStudentRepository(examiniAPI: ExaminiAPI): StudentRepository {
        return StudentRepository(examiniAPI)
    }

    @Provides
    @Singleton
    fun provideTopicRepository(examiniAPI: ExaminiAPI): LessonRepository {
        return LessonRepository(examiniAPI)
    }
}