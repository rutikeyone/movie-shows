package com.ru.movieshows.app.glue.profile.repositories

import com.ru.movieshows.data.settings.sources.SettingsDataSource
import com.ru.movieshows.profile.domain.repositories.SessionRepository
import javax.inject.Inject

class AdapterSessionRepository @Inject constructor(
    private val settingsDataSource: SettingsDataSource,
): SessionRepository {

    override suspend fun clearCurrentSession() {
        return settingsDataSource.setCurrentSessionId(null)
    }

}