package com.ru.movieshows.data.accounts.di

import com.ru.movieshows.data.AccountsDataRepository
import com.ru.movieshows.data.accounts.AccountsDataRepositoryImpl
import dagger.Binds
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Singleton
@InstallIn(SingletonComponent::class)
interface AccountsDataRepositoriesModule {

    @Binds
    @Singleton
    fun bindAccountsDataRepository(
        accountsDataRepository: AccountsDataRepositoryImpl,
    ): AccountsDataRepository

}