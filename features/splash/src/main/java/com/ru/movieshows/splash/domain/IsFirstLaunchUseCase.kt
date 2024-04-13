package com.ru.movieshows.splash.domain

import com.ru.movieshows.splash.domain.repositories.SplashSettingsRepository
import javax.inject.Inject

class IsFirstLaunchUseCase @Inject constructor(
    private val splashSettingsRepository: SplashSettingsRepository,
) {

    fun getFirstLaunch(): Boolean {
        return splashSettingsRepository.getFirstLaunch()
    }

}