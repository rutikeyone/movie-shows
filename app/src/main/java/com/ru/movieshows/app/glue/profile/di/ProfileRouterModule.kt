package com.ru.movieshows.app.glue.profile.di

import com.ru.movieshows.app.glue.profile.AdapterProfileRouter
import com.ru.movieshows.profile.presentation.ProfileRouter
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface ProfileRouterModule {

    @Binds
    fun bindProfileRouter(profileRouter: AdapterProfileRouter): ProfileRouter

}