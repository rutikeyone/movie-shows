package com.ru.movieshows.data.accounts.di

import com.ru.movieshows.data.accounts.sources.AccountsDataSource
import com.ru.movieshows.data.accounts.sources.AccountsDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface AccountSourcesModule {

    @Binds
    fun bindAccountSource(
        accountsDataSource: AccountsDataSourceImpl
    ): AccountsDataSource

}