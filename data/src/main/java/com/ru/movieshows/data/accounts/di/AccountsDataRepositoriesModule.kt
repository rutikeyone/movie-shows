package com.ru.movieshows.data.accounts.di

import com.ru.movieshows.data.AccountsDataRepository
import com.ru.movieshows.data.accounts.AccountsRetrofitSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface AccountsDataRepositoriesModule {

    @Binds
    @Singleton
    fun bindAccountsDataRepository(
        accountsDataRepository: AccountsRetrofitSourceImpl,
    ): AccountsDataRepository

}