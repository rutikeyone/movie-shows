package com.ru.movieshows.data.settings.di

import com.ru.movieshows.data.settings.sources.SettingsDataSource
import com.ru.movieshows.data.settings.sources.SharedPreferencesSettingsDataSource
import dagger.hilt.components.SingletonComponent
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface AppSettingsDataSourceModule {

    @Binds
    @Singleton
    fun bindAppSettingsDataSource(
        settingsDataSource: SharedPreferencesSettingsDataSource,
    ): SettingsDataSource

}