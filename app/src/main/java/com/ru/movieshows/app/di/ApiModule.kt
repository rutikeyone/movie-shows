package com.ru.movieshows.app.di

import com.ru.movieshows.sources.accounts.AccountsApi
import com.ru.movieshows.sources.genres.GenresApi
import com.ru.movieshows.sources.movies.MoviesApi
import com.ru.movieshows.sources.people.PeopleApi
import com.ru.movieshows.sources.tv_shows.TvShowsApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
class ApiModule {

    @Provides
    fun provideAccountsApi(retrofit: Retrofit): AccountsApi = retrofit.create(AccountsApi::class.java)

    @Provides
    fun provideGenresApi(retrofit: Retrofit): GenresApi = retrofit.create(GenresApi::class.java)

    @Provides
    fun provideMoviesApi(retrofit: Retrofit): MoviesApi = retrofit.create(MoviesApi::class.java)

    @Provides
    fun provideTvShowApi(retrofit: Retrofit): TvShowsApi = retrofit.create(TvShowsApi::class.java)

    @Provides
    fun providePeopleApi(retrofit: Retrofit): PeopleApi = retrofit.create(PeopleApi::class.java)

}