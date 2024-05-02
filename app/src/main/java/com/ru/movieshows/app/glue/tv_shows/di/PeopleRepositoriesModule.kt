package com.ru.movieshows.app.glue.tv_shows.di

import com.ru.movieshows.app.glue.tv_shows.repositories.AdapterPeopleRepository
import com.ru.movieshows.tv_shows.domain.repositories.PeopleRepository
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