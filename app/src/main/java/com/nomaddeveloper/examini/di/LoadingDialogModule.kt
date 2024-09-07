package com.nomaddeveloper.examini.di

import com.nomaddeveloper.examini.ui.loading.LoadingDialogFragment
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LoadingDialogModule {
    @Provides
    @Singleton
    fun provideLoadingDialog(): LoadingDialogFragment {
        return LoadingDialogFragment()
    }
}