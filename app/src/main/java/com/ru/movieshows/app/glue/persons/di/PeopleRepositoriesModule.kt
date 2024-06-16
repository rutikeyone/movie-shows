package com.ru.movieshows.app.glue.persons.di

import com.ru.movieshows.app.glue.persons.repositories.AdapterPeopleRepository
import com.ru.movieshows.season.domain.repositories.PeopleRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface PeopleRepositoriesModule {

    @Binds
    fun bindPeopleRepository(peopleRepository: AdapterPeopleRepository): PeopleRepository

}