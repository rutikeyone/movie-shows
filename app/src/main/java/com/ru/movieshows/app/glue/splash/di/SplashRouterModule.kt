package com.ru.movieshows.app.glue.splash.di

import com.ru.movieshows.app.glue.splash.AdapterSplashRouter
import com.ru.movieshows.splash.presentation.SplashRouter
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface SplashRouterModule {

    @Binds
    fun provideSplashRouter(
        splashRouter: AdapterSplashRouter,
    ): SplashRouter

}