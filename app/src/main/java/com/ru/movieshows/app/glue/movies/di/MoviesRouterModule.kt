package com.ru.movieshows.app.glue.movies.di

import com.ru.movieshows.app.glue.movies.AdapterMoviesRouter
import com.ru.movieshows.movies.MoviesRouter
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface MoviesRouterModule {

    @Binds
    fun bindMoviesRouter(moviesRouter: AdapterMoviesRouter): MoviesRouter

}