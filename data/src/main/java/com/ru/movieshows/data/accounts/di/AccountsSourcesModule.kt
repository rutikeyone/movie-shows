package com.ru.movieshows.data.accounts.di

import com.ru.movieshows.data.accounts.sources.AccountsDataSource
import com.ru.movieshows.data.accounts.sources.AccountsRetrofitDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface AccountSourcesModule {

    @Binds
    fun bindAccountSource(
        accountsDataSource: AccountsRetrofitDataSourceImpl,
    ): AccountsDataSource

}