package com.nomaddeveloper.examini.di

import android.content.Context
import com.nomaddeveloper.examini.R
import com.nomaddeveloper.examini.data.model.realm.GeminiPoint
import com.nomaddeveloper.examini.data.source.local.RealmCRUD
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RealmModule {
    @Provides
    @Singleton
    fun provideRealmConfiguration(@ApplicationContext context: Context): RealmConfiguration {
        return RealmConfiguration.Builder(schema = setOf(GeminiPoint::class))
            .name(context.getString(R.string.realm_database_name))
            .deleteRealmIfMigrationNeeded()
            .schemaVersion(1)
            .build()
    }

    @Provides
    @Singleton
    fun provideRealm(realmConfiguration: RealmConfiguration): Realm {
        return Realm.open(realmConfiguration)
    }

    @Provides
    @Singleton
    fun provideRealmCRUD(realm: Realm): RealmCRUD {
        return RealmCRUD(realm)
    }
}