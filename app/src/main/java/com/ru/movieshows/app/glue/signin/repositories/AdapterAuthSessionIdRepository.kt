package com.ru.movieshows.app.glue.signin.repositories

import com.ru.movieshows.data.settings.SettingsDataSource
import com.ru.movieshows.signin.domain.repositories.AuthSessionIdRepository
import javax.inject.Inject

class AdapterAuthSessionIdRepository @Inject constructor(
    private val settingsDataSource: SettingsDataSource,
) : AuthSessionIdRepository {

    override suspend fun setCurrentSessionId(sessionId: String?) {
        settingsDataSource.setCurrentSessionId(sessionId)
    }

    override suspend fun getCurrentSessionId(): String? {
        return settingsDataSource.getCurrentSessionId()
    }
}