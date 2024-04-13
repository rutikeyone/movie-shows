package com.ru.movieshows.app.glue.navigation.di

import com.ru.movieshows.app.glue.navigation.repositories.AdapterMainFirstLaunchRepository
import com.ru.movieshows.navigation.domain.repositories.MainFirstLaunchRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface MainRepositoriesModule {

    @Binds
    fun bindMainFirstLaunchRepository(
        mainFirstLaunchRepository: AdapterMainFirstLaunchRepository,
    ): MainFirstLaunchRepository

}