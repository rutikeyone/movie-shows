package com.ru.movieshows.data.people.di

import com.ru.movieshows.data.people.service.PeopleService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
class PeopleServiceModule {

    @Provides
    fun providePeopleApi(retrofit: Retrofit): PeopleService {
        return retrofit.create(PeopleService::class.java)
    }

}