package com.ru.movieshows.app.glue.signin.di

import com.ru.movieshows.app.glue.signin.repositories.AdapterAuthServiceRepository
import com.ru.movieshows.app.glue.signin.repositories.AdapterAuthSessionIdRepository
import com.ru.movieshows.app.glue.signin.repositories.AdapterProfileRepository
import com.ru.movieshows.signin.domain.repositories.AuthServiceRepository
import com.ru.movieshows.signin.domain.repositories.AuthSessionIdRepository
import com.ru.movieshows.signin.domain.repositories.ProfileRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface SignInRepositoriesModule {

    @Binds
    fun bindAuthRepository(
        authServiceRepository: AdapterAuthServiceRepository
    ): AuthServiceRepository

    @Binds
    fun bindAuthSessionIdRepository(
        authTokenRepository: AdapterAuthSessionIdRepository
    ): AuthSessionIdRepository

    @Binds
    fun bindProfileRepository(
        profileRepository: AdapterProfileRepository
    ): ProfileRepository

}