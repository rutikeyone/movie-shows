package com.ru.movieshows.data.accounts.di

import com.ru.movieshows.data.accounts.services.AccountsService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
class AccountsServiceModule {

    @Provides
    fun provideAccountsService(retrofit: Retrofit): AccountsService {
        return retrofit.create(AccountsService::class.java)
    }

}