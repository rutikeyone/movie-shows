package com.ru.movieshows.data.genres.di

import com.ru.movieshows.data.GenresDataRepository
import com.ru.movieshows.data.genres.GenresDataRepositoryImpl
import dagger.Binds
import dagger.hilt.InstallIn
import javax.inject.Singleton

@Singleton
@InstallIn(Singleton::class)
interface GenresRepositoriesModule {

    @Binds
    fun provideGenresRepository(
        genresDataRepository: GenresDataRepositoryImpl
    ): GenresDataRepository

}