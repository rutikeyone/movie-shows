package com.ru.movieshows.app.glue.splash.di

import com.ru.movieshows.app.glue.splash.repositories.AdapterSplashSettingsRepository
import com.ru.movieshows.splash.domain.repositories.SplashSettingsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface SplashRepositoriesModule {

    @Binds
    fun provideSplashSettingsRepository(
        splashSettingsRepository: AdapterSplashSettingsRepository,
    ): SplashSettingsRepository

}