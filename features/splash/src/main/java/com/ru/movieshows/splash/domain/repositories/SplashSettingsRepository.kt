package com.ru.movieshows.splash.domain.repositories

interface SplashSettingsRepository {

    suspend fun hasCurrentAccount(): Boolean

    fun getFirstLaunch(): Boolean

}