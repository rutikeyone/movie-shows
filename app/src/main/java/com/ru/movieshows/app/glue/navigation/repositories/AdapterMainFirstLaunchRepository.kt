package com.ru.movieshows.app.glue.navigation.repositories

import com.ru.movieshows.data.SettingsDataRepository
import com.ru.movieshows.navigation.domain.repositories.MainFirstLaunchRepository
import javax.inject.Inject

class AdapterMainFirstLaunchRepository @Inject constructor(
    private val settingsDataRepository: SettingsDataRepository,
) : MainFirstLaunchRepository {

    override fun getFirstLaunch(): Boolean {
        return settingsDataRepository.getFirstLaunch()
    }

    override fun setFirstLaunch(value: Boolean) {
        return settingsDataRepository.setFirstLaunch(value)
    }

}