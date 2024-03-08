package com.ru.movieshows.data.genres.di

import com.ru.movieshows.data.genres.sources.GenresSource
import com.ru.movieshows.data.genres.sources.GenresSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface GenresSourcesModule {

    @Binds
    fun bindGenresSource(
        genresSource: GenresSourceImpl
    ): GenresSource

}