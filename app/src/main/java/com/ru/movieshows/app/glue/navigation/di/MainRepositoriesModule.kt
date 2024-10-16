package com.ru.movieshows.app.glue.navigation.di

import com.ru.movieshows.app.glue.navigation.repositories.AdapterFirstLaunchRepository
import com.ru.movieshows.navigation.domain.repositories.FirstLaunchRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface MainRepositoriesModule {

    @Binds
    fun bindMainFirstLaunchRepository(
        mainFirstLaunchRepository: AdapterFirstLaunchRepository,
    ): FirstLaunchRepository

}