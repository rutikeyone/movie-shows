package com.ru.movieshows.data.genres.di

import com.ru.movieshows.data.GenresDataRepository
import com.ru.movieshows.data.genres.GenresDataRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface GenresDataRepositoriesModule {

    @Binds
    fun provideGenresDataRepository(
        genresDataRepository: GenresDataRepositoryImpl,
    ): GenresDataRepository

}