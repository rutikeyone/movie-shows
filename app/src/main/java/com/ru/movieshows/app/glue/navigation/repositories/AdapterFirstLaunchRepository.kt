package com.ru.movieshows.app.glue.navigation.repositories

import com.ru.movieshows.data.SettingsDataRepository
import com.ru.movieshows.navigation.domain.repositories.FirstLaunchRepository
import javax.inject.Inject

class AdapterFirstLaunchRepository @Inject constructor(
    private val settingsDataRepository: SettingsDataRepository,
) : FirstLaunchRepository {

    override fun getFirstLaunch(): Boolean {
        return settingsDataRepository.getFirstLaunch()
    }

    override fun setFirstLaunch(value: Boolean) {
        return settingsDataRepository.setFirstLaunch(value)
    }

}