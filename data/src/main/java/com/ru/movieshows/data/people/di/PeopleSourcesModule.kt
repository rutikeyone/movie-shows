package com.ru.movieshows.data.people.di

import com.ru.movieshows.data.people.sources.PeopleSource
import com.ru.movieshows.data.people.sources.PeopleSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface PeopleSourcesModule {

    @Binds
    fun bindPeopleSource(
        peopleSource: PeopleSourceImpl,
    ): PeopleSource

}