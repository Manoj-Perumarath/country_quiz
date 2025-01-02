package com.manoj.countryquiz.di

import android.content.Context
import com.manoj.countryquiz.data.repository.DataRepository
import com.manoj.countryquiz.data.repositoryimpl.DataRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providesDataRepositoryImpl(@ApplicationContext appContext: Context): DataRepository {
        return DataRepositoryImpl(appContext)
    }

}