package com.ru.movieshows.data.people.di

import com.ru.movieshows.data.PeopleDataRepository
import com.ru.movieshows.data.people.PeopleDataRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface PeopleDataRepositoriesModule {

    @Binds
    fun providePeopleDataRepository(
        peopleDataRepository: PeopleDataRepositoryImpl
    ): PeopleDataRepository

}