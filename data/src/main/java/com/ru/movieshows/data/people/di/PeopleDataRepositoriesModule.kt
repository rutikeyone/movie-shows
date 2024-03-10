package com.ru.movieshows.data.people.di

import com.ru.movieshows.data.PeopleDataRepository
import com.ru.movieshows.data.people.PeopleDataRepositoryImpl
import dagger.Binds
import dagger.hilt.InstallIn
import javax.inject.Singleton

@Singleton
@InstallIn(Singleton::class)
interface PeopleDataRepositoriesModule {

    @Binds
    fun providePeopleDataRepository(
        peopleDataRepository: PeopleDataRepositoryImpl
    ): PeopleDataRepository

}