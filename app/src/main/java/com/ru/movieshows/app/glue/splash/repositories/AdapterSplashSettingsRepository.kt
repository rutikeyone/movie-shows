package com.ru.movieshows.app.glue.splash.repositories

import com.ru.movieshows.core.Container
import com.ru.movieshows.core.unwrapFirst
import com.ru.movieshows.data.AccountsDataRepository
import com.ru.movieshows.data.SettingsDataRepository
import com.ru.movieshows.data.accounts.models.AccountDataModel
import com.ru.movieshows.splash.domain.repositories.SplashSettingsRepository
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class AdapterSplashSettingsRepository @Inject constructor(
    private val settingsDataRepository: SettingsDataRepository,
    private val accountsDataRepository: AccountsDataRepository,
) : SplashSettingsRepository {

    override fun getFirstLaunch(): Boolean {
        return settingsDataRepository.getFirstLaunchAfterInit()
    }

    override suspend fun hasCurrentAccount(): Boolean {
        val container = accountsDataRepository.getAccount()
            .filterNot { it is Container.Pending }
            .first()
        return container is Container.Success<AccountDataModel>
    }

}