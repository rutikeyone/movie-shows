package com.ru.movieshows.data.people.di

import com.ru.movieshows.data.people.api.PeopleApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
class PeopleApiModule {

    @Provides
    fun providePeopleApi(retrofit: Retrofit): PeopleApi {
        return retrofit.create(PeopleApi::class.java)
    }

}