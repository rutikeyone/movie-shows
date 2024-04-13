package com.ru.movieshows.app.glue.profile.di

import com.ru.movieshows.app.glue.profile.repositories.AdapterProfileRepository
import com.ru.movieshows.app.glue.profile.repositories.AdapterSessionRepository
import com.ru.movieshows.profile.domain.repositories.ProfileRepository
import com.ru.movieshows.profile.domain.repositories.SessionRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
interface ProfileRepositoriesModule {

    @Binds
    fun bindProfileRepository(
        profileRepository: AdapterProfileRepository,
    ): ProfileRepository

    @Binds
    fun bindSessionRepository(
        sessionRepository: AdapterSessionRepository,
    ): SessionRepository

}