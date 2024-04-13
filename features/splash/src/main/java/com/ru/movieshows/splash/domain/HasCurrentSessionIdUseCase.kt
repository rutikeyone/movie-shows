package com.ru.movieshows.splash.domain

import com.ru.movieshows.splash.domain.repositories.SplashSettingsRepository
import javax.inject.Inject

class HasCurrentSessionIdUseCase @Inject constructor(
    private val splashSettingsRepository: SplashSettingsRepository,
) {

    suspend fun hasCurrentAccount(): Boolean {
        return splashSettingsRepository.hasCurrentAccount()
    }

}