package com.ru.movieshows.data.settings.di

import com.ru.movieshows.data.SettingsDataRepository
import com.ru.movieshows.data.settings.SettingsDataRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface AppSettingsDataRepositoriesModule {

    @Binds
    fun provideAppSettingsDataRepository(
        appSettingsDataRepository: SettingsDataRepositoryImpl,
    ): SettingsDataRepository

}