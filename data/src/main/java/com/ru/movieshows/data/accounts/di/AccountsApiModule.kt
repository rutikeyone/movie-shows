package com.ru.movieshows.data.accounts.di

import com.ru.movieshows.data.accounts.api.AccountsApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
class AccountsApiModule {

    @Provides
    fun provideAccountsApi(retrofit: Retrofit): AccountsApi {
        return retrofit.create(AccountsApi::class.java)
    }

}