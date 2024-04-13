package com.ru.movieshows.app.glue.navigation.di

import com.ru.movieshows.app.glue.navigation.DefaultDestinationsProvider
import com.ru.movieshows.navigation.DestinationsProvider
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface DestinationProviderModule {

    @Binds
    fun bindStartDestinationProvider(
        destinationProvider: DefaultDestinationsProvider
    ): DestinationsProvider

}